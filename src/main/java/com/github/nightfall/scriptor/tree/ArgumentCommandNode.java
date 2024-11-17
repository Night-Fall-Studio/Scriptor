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

import com.github.nightfall.scriptor.interfaces.Command;
import com.github.nightfall.scriptor.interfaces.RedirectModifier;
import com.github.nightfall.scriptor.StringReader;
import com.github.nightfall.scriptor.args.ArgumentType;
import com.github.nightfall.scriptor.crafter.RequiredArgumentCrafter;
import com.github.nightfall.scriptor.context.CommandContext;
import com.github.nightfall.scriptor.context.CommandContextBuilder;
import com.github.nightfall.scriptor.context.ParsedArgument;
import com.github.nightfall.scriptor.exceptions.CommandSyntaxException;
import com.github.nightfall.scriptor.suggestion.SuggestionProvider;
import com.github.nightfall.scriptor.suggestion.Suggestions;
import com.github.nightfall.scriptor.suggestion.SuggestionsBuilder;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

/**
 * Represents a command node that takes a single argument, which is parsed and processed.
 * It supports argument types and custom suggestions for auto-completion.
 *
 * @param <S> The type of the command source.
 * @param <T> The type of the argument value.
 * @author M4ximumpizza
 * @since 1.0
 */
public class ArgumentCommandNode<S, T> extends CommandNode<S> {
    private static final String USAGE_ARGUMENT_OPEN = "<";
    private static final String USAGE_ARGUMENT_CLOSE = ">";

    private final String name;
    private final ArgumentType<T> type;
    private final SuggestionProvider<S> customSuggestions;
    private Predicate<S> requirement;

    /**
     * Constructs an ArgumentCommandNode with the provided parameters.
     *
     * @param name The name of the argument.
     * @param type The type of the argument.
     * @param command The command associated with this node.
     * @param requirement A predicate determining if the argument is required for the command.
     * @param redirect The redirect node to be followed if necessary.
     * @param modifier The redirect modifier.
     * @param forks Whether this node can fork or not.
     * @param customSuggestions A custom suggestion provider for this argument.
     */
    public ArgumentCommandNode(final String name, final ArgumentType<T> type, final Command<S> command, final Predicate<S> requirement, final CommandNode<S> redirect, final RedirectModifier<S> modifier, final boolean forks, final SuggestionProvider<S> customSuggestions) {
        super(command, requirement, redirect, modifier, forks);
        this.name = name;
        this.type = type;
        this.customSuggestions = customSuggestions;
    }

    /**
     * Returns the type of the argument.
     *
     * @return The argument type.
     */
    public ArgumentType<T> getType() {
        return type;
    }

    /**
     * Returns the name of the argument.
     *
     * @return The argument name.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Returns the usage text for the argument, enclosed in angle brackets.
     *
     * @return The usage text for the argument.
     */
    @Override
    public String getUsageText() {
        return USAGE_ARGUMENT_OPEN + name + USAGE_ARGUMENT_CLOSE;
    }

    /**
     * Returns the custom suggestions provider for this argument.
     *
     * @return The custom suggestions provider, or null if none is provided.
     */
    public SuggestionProvider<S> getCustomSuggestions() {
        return customSuggestions;
    }

    /**
     * Parses the argument from the reader and adds it to the context builder.
     *
     * @param reader The reader containing the input to be parsed.
     * @param contextBuilder The context builder to store the parsed argument.
     * @throws CommandSyntaxException If parsing fails.
     */
    @Override
    public void parse(final StringReader reader, final CommandContextBuilder<S> contextBuilder) throws CommandSyntaxException {
        final int start = reader.getCursor();
        final T result = type.parse(reader, contextBuilder.getSource());
        final ParsedArgument<S, T> parsed = new ParsedArgument<>(start, reader.getCursor(), result);

        contextBuilder.withArgument(name, parsed);
        contextBuilder.withNode(this, parsed.getRange());
    }

    /**
     * Returns a CompletableFuture that provides suggestions based on the current input.
     *
     * @param context The command context.
     * @param builder The suggestions builder.
     * @return A CompletableFuture containing suggestions.
     * @throws CommandSyntaxException If an error occurs during suggestion generation.
     */
    @Override
    public CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> context, final SuggestionsBuilder builder) throws CommandSyntaxException {
        if (customSuggestions == null) {
            return type.listSuggestions(context, builder);
        } else {
            return customSuggestions.getSuggestions(context, builder);
        }
    }

    /**
     * Creates a builder for this argument node. Currently returns null.
     *
     * @return null as builders are not implemented.
     */
    @Override
    public RequiredArgumentCrafter<S, T> createBuilder() {
        return null;
    }

    /**
     * Returns the requirement predicate for this argument, which determines whether the argument is required.
     *
     * @return The requirement predicate.
     */
    private Predicate<S> getRequirement() {
        return requirement;
    }

    /**
     * Checks if the input is valid for this argument by parsing it and ensuring no extra characters remain.
     *
     * @param input The input string to check.
     * @return true if the input is valid, false otherwise.
     */
    @Override
    public boolean isValidInput(final String input) {
        try {
            final StringReader reader = new StringReader(input);
            type.parse(reader);
            return !reader.canRead() || reader.peek() == ' ';
        } catch (final CommandSyntaxException ignored) {
            return false;
        }
    }

    /**
     * Checks if this node is equal to another object.
     *
     * @param o The object to compare with.
     * @return true if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof ArgumentCommandNode)) return false;

        final ArgumentCommandNode that = (ArgumentCommandNode) o;

        if (!name.equals(that.name)) return false;
        if (!type.equals(that.type)) return false;
        return super.equals(o);
    }

    /**
     * Returns a hash code for this node based on its name and type.
     *
     * @return The hash code for this node.
     */
    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }

    /**
     * Returns a key used to sort the argument, which is the argument name.
     *
     * @return The sorting key (argument name).
     */
    @Override
    protected String getSortedKey() {
        return name;
    }

    /**
     * Returns a collection of example values for this argument, as provided by the argument type.
     *
     * @return A collection of example values.
     */
    @Override
    public Collection<String> getExamples() {
        return type.getExamples();
    }

    /**
     * Returns a string representation of this argument node.
     *
     * @return A string representing this argument node.
     */
    @Override
    public String toString() {
        return "<argument " + name + ":" + type + ">";
    }
}
