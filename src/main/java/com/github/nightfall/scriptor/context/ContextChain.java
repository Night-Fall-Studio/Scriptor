/*
 * Project Name: Scriptor
 *
 * Licensed under the PolyForm Shield License 1.0.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 * https://polyformproject.org/wp-content/uploads/2020/06/PolyForm-Shield-1.0.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.nightfall.scriptor.context;

import com.github.nightfall.scriptor.interfaces.RedirectModifier;
import com.github.nightfall.scriptor.interfaces.ResultConsumer;
import com.github.nightfall.scriptor.exceptions.CommandSyntaxException;

import java.util.*;

/**
 * Represents a chain of command contexts, where modifiers modify the source context and
 * an executable command is run at the end of the chain.
 *
 * @param <S> The type of context the chain operates on.
 * @author M4ximumpizza
 * @since 1.0
 */
public class ContextChain<S> {
    private final List<CommandContext<S>> modifiers;
    private final CommandContext<S> executable;
    private ContextChain<S> nextStageCache = null;

    /**
     * Constructs a new ContextChain with the specified modifiers and executable.
     *
     * @param modifiers The list of modifier contexts that modify the command context.
     * @param executable The executable context to run after modifiers are applied.
     * @throws IllegalArgumentException if the executable command is null.
     */
    public ContextChain(final List<CommandContext<S>> modifiers, final CommandContext<S> executable) {
        if (executable.getCommand() == null) {
            throw new IllegalArgumentException("Last command in chain must be executable");
        }
        this.modifiers = modifiers;
        this.executable = executable;
    }

    /**
     * Attempts to flatten a chain of command contexts into a list of modifiers and an executable.
     *
     * @param rootContext The root context to attempt flattening.
     * @param <S> The type of context the chain operates on.
     * @return An Optional containing the flattened ContextChain, or empty if the root context is invalid.
     */
    public static <S> Optional<ContextChain<S>> tryFlatten(final CommandContext<S> rootContext) {
        List<CommandContext<S>> modifiers = new ArrayList<>();
        CommandContext<S> current = rootContext;

        while (current.getChild() != null) {
            modifiers.add(current);
            current = current.getChild();
        }

        // Ensure the final command is executable
        if (current.getCommand() == null) {
            return Optional.empty();
        }

        return Optional.of(new ContextChain<>(modifiers, current));
    }

    /**
     * Runs a modifier command on a source context.
     *
     * @param modifier The modifier context to apply.
     * @param source The source context to modify.
     * @param resultConsumer The consumer to handle the result of the command.
     * @param forkedMode Whether or not to operate in forked mode.
     * @param <S> The type of context the chain operates on.
     * @return A collection of modified sources.
     * @throws CommandSyntaxException If an error occurs while running the modifier.
     */
    public static <S> Collection<S> runModifier(final CommandContext<S> modifier, final S source, final ResultConsumer<S> resultConsumer, final boolean forkedMode) throws CommandSyntaxException {
        RedirectModifier<S> sourceModifier = modifier.getRedirectModifier();

        // If no redirect modifier, return the source as is
        if (sourceModifier == null) {
            return Collections.singleton(source);
        }

        CommandContext<S> contextToUse = modifier.copyFor(source);
        try {
            return sourceModifier.apply(contextToUse);
        } catch (CommandSyntaxException ex) {
            resultConsumer.onCommandComplete(contextToUse, false, 0);
            if (forkedMode) {
                return Collections.emptyList();
            }
            throw ex;
        }
    }

    /**
     * Executes the final command in the chain.
     *
     * @param executable The executable context to run.
     * @param source The source context to execute on.
     * @param resultConsumer The consumer to handle the result of the command.
     * @param forkedMode Whether or not to operate in forked mode.
     * @param <S> The type of context the chain operates on.
     * @return The result of executing the command.
     * @throws CommandSyntaxException If an error occurs while running the executable.
     */
    public static <S> int runExecutable(
            final CommandContext<S> executable, final S source, final ResultConsumer<S> resultConsumer, final boolean forkedMode
    ) throws CommandSyntaxException {
        CommandContext<S> contextToUse = executable.copyFor(source);
        try {
            int result = executable.getCommand().run(contextToUse);
            resultConsumer.onCommandComplete(contextToUse, true, result);
            return forkedMode ? 1 : result;
        } catch (CommandSyntaxException ex) {
            resultConsumer.onCommandComplete(contextToUse, false, 0);
            if (forkedMode) {
                return 0;
            }
            throw ex;
        }
    }

    /**
     * Executes all modifiers in the chain and the final executable, in sequence.
     *
     * @param source The source context to operate on.
     * @param resultConsumer The consumer to handle the result of the command.
     * @return The cumulative result of all executed commands.
     * @throws CommandSyntaxException If an error occurs while executing any command.
     */
    public int executeAll(final S source, final ResultConsumer<S> resultConsumer) throws CommandSyntaxException {
        if (modifiers.isEmpty()) {
            // Fast path: directly execute the executable command if no modifiers
            return runExecutable(executable, source, resultConsumer, false);
        }

        boolean forkedMode = false;
        List<S> currentSources = Collections.singletonList(source);

        // Apply all modifiers
        for (final CommandContext<S> modifier : modifiers) {
            forkedMode |= modifier.isForked();

            List<S> nextSources = new ArrayList<>();
            for (final S sourceToRun : currentSources) {
                nextSources.addAll(runModifier(modifier, sourceToRun, resultConsumer, forkedMode));
            }

            if (nextSources.isEmpty()) {
                return 0;
            }

            currentSources = nextSources;
        }

        // Execute the final command
        int result = 0;
        for (final S executionSource : currentSources) {
            result += runExecutable(executable, executionSource, resultConsumer, forkedMode);
        }

        return result;
    }

    /**
     * Returns the current stage of the context chain (either MODIFY or EXECUTE).
     *
     * @return The current stage of the chain.
     */
    public Stage getStage() {
        return modifiers.isEmpty() ? Stage.EXECUTE : Stage.MODIFY;
    }

    /**
     * Returns the top context in the chain (the first modifier, or the executable if no modifiers).
     *
     * @return The top command context.
     */
    public CommandContext<S> getTopContext() {
        return modifiers.isEmpty() ? executable : modifiers.get(0);
    }

    /**
     * Returns the next stage of the context chain, or null if no further stages exist.
     *
     * @return The next stage of the chain.
     */
    public ContextChain<S> nextStage() {
        if (modifiers.isEmpty()) {
            return null;
        }

        if (nextStageCache == null) {
            nextStageCache = new ContextChain<>(modifiers.subList(1, modifiers.size()), executable);
        }
        return nextStageCache;
    }

    /**
     * Enum representing the possible stages in the context chain: MODIFY (modifiers still exist) or EXECUTE (only executable remains).
     */
    public enum Stage {
        MODIFY,
        EXECUTE,
    }
}

