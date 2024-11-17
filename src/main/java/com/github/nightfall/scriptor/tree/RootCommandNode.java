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

import com.github.nightfall.scriptor.StringReader;
import com.github.nightfall.scriptor.context.CommandContext;
import com.github.nightfall.scriptor.context.CommandContextBuilder;
import com.github.nightfall.scriptor.crafter.ArgumentCrafter;
import com.github.nightfall.scriptor.exceptions.CommandSyntaxException;
import com.github.nightfall.scriptor.suggestion.Suggestions;
import com.github.nightfall.scriptor.suggestion.SuggestionsBuilder;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

/**
 * Represents the root command node in the command tree.
 * This node does not perform any operations and serves as the base node for command parsing.
 *
 * @param <S> The source type that the command is executed with (e.g., a player, console, etc.)
 * @author M4ximumpizza
 * @since 1.0
 */
public class RootCommandNode<S> extends CommandNode<S> {

    /**
     * Constructs a new root command node with default behavior.
     */
    public RootCommandNode() {
        super(null, c -> true, null, s -> Collections.singleton(s.getSource()), false);
    }

    /**
     * Gets the name of this node. For the root node, this is an empty string.
     *
     * @return An empty string as the name of the root node
     */
    @Override
    public String getName() {
        return "";
    }

    /**
     * Gets the usage text for this node. For the root node, this is an empty string.
     *
     * @return An empty string as the usage text for the root node
     */
    @Override
    public String getUsageText() {
        return "";
    }

    /**
     * Parses the input string. The root command node does not perform any parsing itself.
     *
     * @param reader The reader to parse input from
     * @param contextBuilder The context builder to store the parsed result
     * @throws CommandSyntaxException This method does not throw any exceptions as no parsing is done
     */
    @Override
    public void parse(final StringReader reader, final CommandContextBuilder<S> contextBuilder) throws CommandSyntaxException {
        // No operation in root node
    }

    /**
     * Lists suggestions for the root node. The root node does not provide any suggestions.
     *
     * @param context The command context
     * @param builder The suggestions builder to add suggestions to
     * @return An empty future, as no suggestions are provided by the root node
     */
    @Override
    public CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> context, final SuggestionsBuilder builder) {
        return Suggestions.empty();
    }

    /**
     * Checks if the given input is valid. For the root node, no input is considered valid.
     *
     * @param input The input string to validate
     * @return False, as no input is valid for the root node
     */
    @Override
    public boolean isValidInput(final String input) {
        return false;
    }

    /**
     * Compares this root command node with another object. The root node is equal to another root node
     * only if they are the same instance.
     *
     * @param o The object to compare to
     * @return True if the objects are the same instance, false otherwise
     */
    @Override
    public boolean equals(final Object o) {
        return this == o || o instanceof RootCommandNode && super.equals(o);
    }

    /**
     * Creates an argument builder for the root node. This method throws an exception as the root node cannot be converted into a crafter.
     *
     * @return This method always throws an IllegalStateException
     * @throws IllegalStateException Since the root node cannot be converted into a crafter
     */
    @Override
    public ArgumentCrafter<S, ?> createBuilder() {
        throw new IllegalStateException("Cannot convert root into a crafter");
    }

    /**
     * Gets the sorted key for this node. The sorted key is an empty string for the root node.
     *
     * @return An empty string as the sorted key for the root node
     */
    @Override
    protected String getSortedKey() {
        return "";
    }

    /**
     * Gets a collection of example strings for this command node. The root node has no examples.
     *
     * @return An empty list as the root node has no examples
     */
    @Override
    public Collection<String> getExamples() {
        return Collections.emptyList();
    }

    /**
     * Provides a string representation of the root command node.
     *
     * @return A string representation of the root node.
     */
    @Override
    public String toString() {
        return "<root>";
    }
}

