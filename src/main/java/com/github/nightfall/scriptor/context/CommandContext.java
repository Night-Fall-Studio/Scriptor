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
import com.github.nightfall.scriptor.interfaces.RedirectModifier;
import com.github.nightfall.scriptor.tree.CommandNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CommandContext holds the context of a command execution, including its source, arguments, command details,
 * redirection modifiers, and execution state.
 *
 * @param <S> The type of the source (e.g., the command sender).
 * @author M4ximumpizza
 * @since 1.0
 */
public class CommandContext<S> {

    // Map to convert primitive types to their wrapper equivalents for type compatibility.
    private static final Map<Class<?>, Class<?>> PRIMITIVE_TO_WRAPPER = new HashMap<>();

    static {
        // Initialize the mapping for primitive types to their corresponding wrapper classes.
        PRIMITIVE_TO_WRAPPER.put(boolean.class, Boolean.class);
        PRIMITIVE_TO_WRAPPER.put(byte.class, Byte.class);
        PRIMITIVE_TO_WRAPPER.put(short.class, Short.class);
        PRIMITIVE_TO_WRAPPER.put(char.class, Character.class);
        PRIMITIVE_TO_WRAPPER.put(int.class, Integer.class);
        PRIMITIVE_TO_WRAPPER.put(long.class, Long.class);
        PRIMITIVE_TO_WRAPPER.put(float.class, Float.class);
        PRIMITIVE_TO_WRAPPER.put(double.class, Double.class);
    }

    // Instance variables for the context's data
    private final S source; // The source of the command (e.g., the sender)
    private final String input; // The raw input string of the command
    private final Command<S> command; // The executable command
    private final Map<String, ParsedArgument<S, ?>> arguments; // Arguments parsed from the command
    private final CommandNode<S> rootNode; // Root command node
    private final List<ParsedCommandNode<S>> nodes; // List of command nodes representing the parsed structure
    private final StringRange range; // Range of the input string that this command represents
    private final CommandContext<S> child; // Child context for nested command chains
    private final RedirectModifier<S> modifier; // Modifier for redirecting command execution
    private final boolean forks; // Whether this command context forks (runs in parallel)

    /**
     * Constructor for CommandContext, initializes all instance variables.
     */
    public CommandContext(final S source, final String input, final Map<String, ParsedArgument<S, ?>> arguments,
                          final Command<S> command, final CommandNode<S> rootNode, final List<ParsedCommandNode<S>> nodes,
                          final StringRange range, final CommandContext<S> child, final RedirectModifier<S> modifier, boolean forks) {
        this.source = source;
        this.input = input;
        this.arguments = arguments;
        this.command = command;
        this.rootNode = rootNode;
        this.nodes = nodes;
        this.range = range;
        this.child = child;
        this.modifier = modifier;
        this.forks = forks;
    }

    /**
     * Creates a copy of the CommandContext with a new source.
     */
    public CommandContext<S> copyFor(final S source) {
        // If the source is the same, return the current context to avoid unnecessary object creation.
        if (this.source == source) {
            return this;
        }
        // Otherwise, create a new context with the updated source.
        return new CommandContext<>(source, input, arguments, command, rootNode, nodes, range, child, modifier, forks);
    }

    // Getter methods for various components of the context.

    public CommandContext<S> getChild() {
        return child;
    }

    /**
     * Retrieves the last child in the chain of command contexts.
     */
    public CommandContext<S> getLastChild() {
        CommandContext<S> result = this;
        while (result.getChild() != null) {
            result = result.getChild();
        }
        return result;
    }

    public Command<S> getCommand() {
        return command;
    }

    public S getSource() {
        return source;
    }

    /**
     * Retrieves the argument with the specified name, casting it to the specified class.
     */
    @SuppressWarnings("unchecked")
    public <V> V getArgument(final String name, final Class<V> clazz) {
        final ParsedArgument<S, ?> argument = arguments.get(name);

        if (argument == null) {
            throw new IllegalArgumentException("No such argument '" + name + "' exists on this command");
        }

        final Object result = argument.getResult();
        // Check if the argument type is compatible with the expected class type.
        if (PRIMITIVE_TO_WRAPPER.getOrDefault(clazz, clazz).isAssignableFrom(result.getClass())) {
            return (V) result;
        } else {
            throw new IllegalArgumentException("Argument '" + name + "' is defined as " + result.getClass().getSimpleName() + ", not " + clazz);
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof CommandContext)) return false;

        final CommandContext<?> that = (CommandContext<?>) o;

        // Check equality based on relevant fields (arguments, rootNode, nodes, etc.).
        if (!arguments.equals(that.arguments)) return false;
        if (!rootNode.equals(that.rootNode)) return false;
        if (nodes.size() != that.nodes.size() || !nodes.equals(that.nodes)) return false;
        if (command != null ? !command.equals(that.command) : that.command != null) return false;
        if (!source.equals(that.source)) return false;
        if (child != null ? !child.equals(that.child) : that.child != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = source.hashCode();
        result = 31 * result + arguments.hashCode();
        result = 31 * result + (command != null ? command.hashCode() : 0);
        result = 31 * result + rootNode.hashCode();
        result = 31 * result + nodes.hashCode();
        result = 31 * result + (child != null ? child.hashCode() : 0);
        return result;
    }

    // Additional getter methods for redirect modifier, range, and input.

    public RedirectModifier<S> getRedirectModifier() {
        return modifier;
    }

    public StringRange getRange() {
        return range;
    }

    public String getInput() {
        return input;
    }

    public CommandNode<S> getRootNode() {
        return rootNode;
    }

    public List<ParsedCommandNode<S>> getNodes() {
        return nodes;
    }

    public boolean hasNodes() {
        return !nodes.isEmpty();
    }

    public boolean isForked() {
        return forks;
    }

    public void get() {
        return;
    }
}

