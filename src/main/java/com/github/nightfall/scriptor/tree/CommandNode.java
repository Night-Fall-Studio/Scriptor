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

package com.github.nightfall.scriptor.tree;

import com.github.nightfall.scriptor.interfaces.AmbiguityConsumer;
import com.github.nightfall.scriptor.interfaces.Command;
import com.github.nightfall.scriptor.interfaces.RedirectModifier;
import com.github.nightfall.scriptor.StringReader;
import com.github.nightfall.scriptor.context.CommandContext;
import com.github.nightfall.scriptor.context.CommandContextBuilder;
import com.github.nightfall.scriptor.crafter.ArgumentCrafter;
import com.github.nightfall.scriptor.exceptions.CommandSyntaxException;
import com.github.nightfall.scriptor.suggestion.Suggestions;
import com.github.nightfall.scriptor.suggestion.SuggestionsBuilder;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

/**
 * Abstract base class representing a command node in a command tree structure.
 * This class is used to store and manage command nodes and their relationships.
 * It supports child nodes, command redirection, requirements, and command validation.
 *
 * @param <S> The type representing the source (such as the player or system executing the command).
 * @author M4ximumpizza
 * @since 1.0
 */
public abstract class CommandNode<S> implements Comparable<CommandNode<S>> {

    private final Map<String, CommandNode<S>> children = new LinkedHashMap<>();
    private final Map<String, LiteralCommandNode<S>> literals = new LinkedHashMap<>();
    private final Map<String, ArgumentCommandNode<S, ?>> arguments = new LinkedHashMap<>();
    private final Predicate<S> requirement;
    private final CommandNode<S> redirect;
    private final RedirectModifier<S> modifier;
    private final boolean forks;
    private Command<S> command;

    /**
     * Constructs a new CommandNode.
     *
     * @param command   The command associated with this node.
     * @param requirement The requirement that must be fulfilled to use this command.
     * @param redirect The node to which this command redirects, if any.
     * @param modifier Modifier for redirection behavior.
     * @param forks Whether the command is a "forking" command.
     */
    protected CommandNode(final Command<S> command, final Predicate<S> requirement, final CommandNode<S> redirect, final RedirectModifier<S> modifier, final boolean forks) {
        this.command = command;
        this.requirement = Objects.requireNonNull(requirement, "requirement");
        this.redirect = redirect;
        this.modifier = modifier;
        this.forks = forks;
    }

    /**
     * Returns the command associated with this node.
     *
     * @return The command for this node.
     */
    public Command<S> getCommand() {
        return command;
    }

    /**
     * Returns the collection of child nodes under this command node.
     *
     * @return Collection of child nodes.
     */
    public Collection<CommandNode<S>> getChildren() {
        return children.values();
    }

    /**
     * Returns a specific child node by name.
     *
     * @param name The name of the child node to retrieve.
     * @return The child node associated with the given name, or null if not found.
     */
    public CommandNode<S> getChild(final String name) {
        return children.get(name);
    }

    /**
     * Returns the redirect target node if this command has redirection.
     *
     * @return The redirect node, or null if no redirection exists.
     */
    public CommandNode<S> getRedirect() {
        return redirect;
    }

    /**
     * Returns the modifier for command redirection behavior.
     *
     * @return The redirect modifier.
     */
    public RedirectModifier<S> getRedirectModifier() {
        return modifier;
    }

    /**
     * Checks whether the command can be used based on the provided source.
     *
     * @param source The source (such as a player or system) to check.
     * @return True if the command can be used by the given source, false otherwise.
     */
    public boolean canUse(final S source) {
        return requirement.test(source);
    }

    /**
     * Adds a child node to this command node. If the node already exists, it merges the command and child nodes.
     *
     * @param node The command node to add as a child.
     * @throws UnsupportedOperationException If attempting to add a RootCommandNode as a child.
     */
    public void addChild(final CommandNode<S> node) {
        if (node instanceof RootCommandNode) {
            throw new UnsupportedOperationException("Cannot add a RootCommandNode as a child to any other CommandNode");
        }

        final CommandNode<S> child = children.get(node.getName());
        if (child != null) {
            // Merge the command and children if the node already exists
            if (node.getCommand() != null) {
                child.command = node.getCommand();
            }
            for (final CommandNode<S> grandchild : node.getChildren()) {
                child.addChild(grandchild);
            }
        } else {
            children.put(node.getName(), node);
            if (node instanceof LiteralCommandNode) {
                literals.put(node.getName(), (LiteralCommandNode<S>) node);
            } else if (node instanceof ArgumentCommandNode) {
                arguments.put(node.getName(), (ArgumentCommandNode<S, ?>) node);
            }
        }
    }

    /**
     * Finds any ambiguities between command nodes and invokes the provided consumer to handle them.
     *
     * @param consumer The ambiguity consumer to handle any found ambiguities.
     */
    public void findAmbiguities(final AmbiguityConsumer<S> consumer) {
        Set<String> matches = new HashSet<>();

        for (final CommandNode<S> child : children.values()) {
            for (final CommandNode<S> sibling : children.values()) {
                if (child == sibling) {
                    continue;
                }

                for (final String input : child.getExamples()) {
                    if (sibling.isValidInput(input)) {
                        matches.add(input);
                    }
                }

                if (!matches.isEmpty()) {
                    consumer.ambiguous(this, child, sibling, matches);
                    matches.clear();
                }
            }

            child.findAmbiguities(consumer);
        }
    }

    /**
     * Checks whether the input is valid for this node.
     *
     * @param input The input string to check.
     * @return True if the input is valid for this node, false otherwise.
     */
    protected abstract boolean isValidInput(final String input);

    /**
     * Compares two CommandNode objects based on their sorted key.
     *
     * @param o The other CommandNode to compare to.
     * @return A negative integer, zero, or a positive integer if this node is less than, equal to, or greater than the other node, respectively.
     */
    @Override
    public int compareTo(final CommandNode<S> o) {
        if (this instanceof LiteralCommandNode == o instanceof LiteralCommandNode) {
            return getSortedKey().compareTo(o.getSortedKey());
        }

        return (o instanceof LiteralCommandNode) ? 1 : -1;
    }

    /**
     * Returns whether this command node is a "fork" (i.e., spawns a new execution thread).
     *
     * @return True if this node is a fork, false otherwise.
     */
    public boolean isFork() {
        return forks;
    }

    /**
     * Returns the examples for this command node.
     *
     * @return A collection of example inputs for this node.
     */
    public abstract Collection<String> getExamples();

    // Abstract methods

    /**
     * Returns the name of the command node.
     *
     * @return The name of the command node.
     */
    public abstract String getName();

    /**
     * Returns the usage text for the command.
     *
     * @return The usage text of the command.
     */
    public abstract String getUsageText();

    /**
     * Parses the input from a reader and builds a command context.
     *
     * @param reader The input reader to parse.
     * @param contextBuilder The context builder to fill with parsed data.
     * @throws CommandSyntaxException If an error occurs during parsing.
     */
    public abstract void parse(StringReader reader, CommandContextBuilder<S> contextBuilder) throws CommandSyntaxException;

    /**
     * Lists suggestions for the current input based on the command context.
     *
     * @param context The command context.
     * @param builder The suggestion builder to fill with possible suggestions.
     * @return A CompletableFuture representing the suggestions.
     * @throws CommandSyntaxException If an error occurs during suggestion generation.
     */
    public abstract CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) throws CommandSyntaxException;

    /**
     * Creates a new argument builder for this node.
     *
     * @return The argument builder for this node.
     */
    public abstract ArgumentCrafter<S, ?> createBuilder();

    /**
     * Returns a sorted key to use for comparison.
     *
     * @return The sorted key.
     */
    protected abstract String getSortedKey();

    /**
     * Retrieves the relevant nodes for a given input reader.
     *
     * @param input The input reader to check.
     * @return A collection of relevant nodes based on the input.
     */
    public Collection<? extends CommandNode<S>> getRelevantNodes(final StringReader input) {
        if (!literals.isEmpty()) {
            final int cursor = input.getCursor();
            while (input.canRead() && input.peek() != ' ') {
                input.skip();
            }
            final String text = input.getString().substring(cursor, input.getCursor());
            input.setCursor(cursor);
            final LiteralCommandNode<S> literal = literals.get(text);
            if (literal != null) {
                return Collections.singleton(literal);
            } else {
                return arguments.values();
            }
        } else {
            return arguments.values();
        }
    }
}

