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

import com.github.nightfall.scriptor.interfaces.Command;
import com.github.nightfall.scriptor.CommandDispatcher;
import com.github.nightfall.scriptor.interfaces.RedirectModifier;
import com.github.nightfall.scriptor.tree.CommandNode;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A builder for creating command contexts. This builder is used to create a command context, which is used to store
 * the parsed arguments and nodes for a command. The command context is used to execute the command and store the
 * results of the command execution.
 *
 * @param <S> The type of source for the command context.
 * @author M4ximumpizza
 * @since 1.0
 */
public class CommandContextBuilder<S> {
    // Data structure to store arguments parsed for the command context.
    private final Map<String, ParsedArgument<S, ?>> arguments = new LinkedHashMap<>();

    // Root node in the command node hierarchy.
    private final CommandNode<S> rootNode;

    // List of parsed command nodes which are part of the command context.
    private final List<ParsedCommandNode<S>> nodes = new ArrayList<>();

    // Dispatcher responsible for dispatching commands.
    private final CommandDispatcher<S> dispatcher;

    // The source from which the command context is created.
    private S source;

    // Command that is part of this context.
    private Command<S> command;

    // Child builder of the command context.
    private CommandContextBuilder<S> child;

    // Range of input string associated with the command context.
    private StringRange range;

    // Modifier that can modify the source during command execution.
    private RedirectModifier<S> modifier = null;

    // Flag to indicate if the command context is a fork (i.e., multiple commands).
    private boolean forks;

    /**
     * Constructor for initializing the command context builder.
     *
     * @param dispatcher The command dispatcher managing command execution.
     * @param source The source from which the context is created (e.g., sender).
     * @param rootNode The root node for the command context.
     * @param start The start position of the command input.
     */
    public CommandContextBuilder(final CommandDispatcher<S> dispatcher, final S source, final CommandNode<S> rootNode, final int start) {
        this.rootNode = rootNode;
        this.dispatcher = dispatcher;
        this.source = source;
        this.range = StringRange.at(start);
    }

    /**
     * Sets the source for the command context.
     *
     * @param source The source object.
     * @return The updated builder.
     */
    public CommandContextBuilder<S> withSource(final S source) {
        this.source = source;
        return this;
    }

    /**
     * Returns the source of the command context.
     *
     * @return The source.
     */
    public S getSource() {
        return source;
    }

    /**
     * Returns the root node of the command context.
     *
     * @return The root node.
     */
    public CommandNode<S> getRootNode() {
        return rootNode;
    }

    /**
     * Adds an argument to the command context.
     *
     * @param name The name of the argument.
     * @param argument The parsed argument.
     * @return The updated builder.
     */
    public CommandContextBuilder<S> withArgument(final String name, final ParsedArgument<S, ?> argument) {
        this.arguments.put(name, argument);
        return this;
    }

    /**
     * Returns the arguments for the command context.
     *
     * @return A map of arguments.
     */
    public Map<String, ParsedArgument<S, ?>> getArguments() {
        return arguments;
    }

    /**
     * Sets the command associated with this context.
     *
     * @param command The command.
     * @return The updated builder.
     */
    public CommandContextBuilder<S> withCommand(final Command<S> command) {
        this.command = command;
        return this;
    }

    /**
     * Adds a parsed node to the command context, and adjusts the range accordingly.
     *
     * @param node The command node.
     * @param range The range for the node.
     * @return The updated builder.
     */
    public CommandContextBuilder<S> withNode(final CommandNode<S> node, final StringRange range) {
        nodes.add(new ParsedCommandNode<>(node, range));
        this.range = StringRange.encompassing(this.range, range);
        this.modifier = node.getRedirectModifier();
        this.forks = node.isFork();
        return this;
    }

    /**
     * Creates a copy of this builder, preserving the current state.
     *
     * @return A copy of this builder.
     */
    public CommandContextBuilder<S> copy() {
        final CommandContextBuilder<S> copy = new CommandContextBuilder<>(dispatcher, source, rootNode, range.start());
        copy.command = command;
        copy.arguments.putAll(arguments);
        copy.nodes.addAll(nodes);
        copy.child = child;
        copy.range = range;
        copy.forks = forks;
        return copy;
    }

    /**
     * Sets the child builder for this command context.
     *
     * @param child The child builder.
     * @return The updated builder.
     */
    public CommandContextBuilder<S> withChild(final CommandContextBuilder<S> child) {
        this.child = child;
        return this;
    }

    /**
     * Returns the child builder.
     *
     * @return The child builder.
     */
    public CommandContextBuilder<S> getChild() {
        return child;
    }

    /**
     * Finds the last child in the command context builder chain.
     *
     * @return The last child builder.
     */
    public CommandContextBuilder<S> getLastChild() {
        CommandContextBuilder<S> result = this;
        while (result.getChild() != null) {
            result = result.getChild();
        }
        return result;
    }

    /**
     * Returns the command associated with this context builder.
     *
     * @return The command.
     */
    public Command<S> getCommand() {
        return command;
    }

    /**
     * Returns the list of parsed command nodes.
     *
     * @return A list of parsed command nodes.
     */
    public List<ParsedCommandNode<S>> getNodes() {
        return nodes;
    }

    /**
     * Builds the command context from this builder, setting the input string.
     *
     * @param input The input string.
     * @return The created command context.
     */
    public CommandContext<S> build(final String input) {
        return new CommandContext<>(source, input, arguments, command, rootNode, nodes, range, child == null ? null : child.build(input), modifier, forks);
    }

    /**
     * Returns the dispatcher associated with this builder.
     *
     * @return The command dispatcher.
     */
    public CommandDispatcher<S> getDispatcher() {
        return dispatcher;
    }

    /**
     * Returns the range for this command context.
     *
     * @return The range.
     */
    public StringRange getRange() {
        return range;
    }

    /**
     * Finds the suggestion context for the current position of the cursor in the input string.
     *
     * @param cursor The cursor position.
     * @return The suggestion context.
     * @throws IllegalStateException If no suggestion context can be found.
     */
    public SuggestionContext<S> findSuggestionContext(final int cursor) {
        if (range.start() <= cursor) {
            if (range.end() < cursor) {
                if (child != null) {
                    return child.findSuggestionContext(cursor);
                } else if (!nodes.isEmpty()) {
                    final ParsedCommandNode<S> last = nodes.get(nodes.size() - 1);
                    return new SuggestionContext<>(last.node(), last.range().end() + 1);
                } else {
                    return new SuggestionContext<>(rootNode, range.start());
                }
            } else {
                CommandNode<S> prev = rootNode;
                for (final ParsedCommandNode<S> node : nodes) {
                    final StringRange nodeRange = node.range();
                    if (nodeRange.start() <= cursor && cursor <= nodeRange.end()) {
                        return new SuggestionContext<>(prev, nodeRange.start());
                    }
                    prev = node.node();
                }
                if (prev == null) {
                    throw new IllegalStateException("Can't find node before cursor");
                }
                return new SuggestionContext<>(prev, range.start());
            }
        }
        throw new IllegalStateException("Can't find node before cursor");
    }
}

