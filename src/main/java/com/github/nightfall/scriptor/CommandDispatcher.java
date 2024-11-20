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

package com.github.nightfall.scriptor;

import com.github.nightfall.scriptor.crafter.LiteralArgumentCrafter;
import com.github.nightfall.scriptor.context.CommandContext;
import com.github.nightfall.scriptor.context.CommandContextBuilder;
import com.github.nightfall.scriptor.context.ContextChain;
import com.github.nightfall.scriptor.context.SuggestionContext;
import com.github.nightfall.scriptor.exceptions.CommandSyntaxException;
import com.github.nightfall.scriptor.interfaces.AmbiguityConsumer;
import com.github.nightfall.scriptor.interfaces.RedirectModifier;
import com.github.nightfall.scriptor.interfaces.ResultConsumer;
import com.github.nightfall.scriptor.suggestion.Suggestions;
import com.github.nightfall.scriptor.suggestion.SuggestionsBuilder;
import com.github.nightfall.scriptor.tree.CommandNode;
import com.github.nightfall.scriptor.tree.LiteralCommandNode;
import com.github.nightfall.scriptor.tree.RootCommandNode;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * The core command dispatcher responsible for registering, parsing, and executing commands.
 *
 * <p>This class acts as the central component for managing commands in a command-line interface (CLI).
 * It allows commands to be registered, parsed from input strings, and executed. The dispatcher manages
 * a tree of command nodes and facilitates the handling of command execution logic.</p>
 *
 * <p>The dispatcher is designed to be flexible by supporting a custom "source" type, which can represent
 * the originator or executor of a command (e.g., a user, system, or other entities).</p>
 *
 * @param <S> a custom "source" type that represents the originator or executor of the command.
 *            This allows commands to be executed within a context defined by the type {@code S}.
 *
 * @author M4ximumpizza
 * @version 1.0
 */
public class CommandDispatcher<S> {

    /**
     * The string required to separate individual arguments in an input string.
     *
     * <p>This constant defines the separator used between individual arguments when parsing a command string.
     * The separator is a space character, which is commonly used in command-line interfaces.</p>
     *
     * @see #ARGUMENT_SEPARATOR_CHAR
     */
    public static final String ARGUMENT_SEPARATOR = " ";

    /**
     * The character required to separate individual arguments in an input string.
     *
     * <p>This constant defines the character used between individual arguments when parsing a command string.
     * The separator is a space character (' '), typically employed in command-line interfaces to distinguish between arguments.</p>
     *
     * @see #ARGUMENT_SEPARATOR
     */
    public static final char ARGUMENT_SEPARATOR_CHAR = ' ';

    private static final String USAGE_OPTIONAL_OPEN = "[";
    private static final String USAGE_OPTIONAL_CLOSE = "]";
    private static final String USAGE_REQUIRED_OPEN = "(";
    private static final String USAGE_REQUIRED_CLOSE = ")";
    private static final String USAGE_OR = "|";

    /**
     * The root command node that serves as the entry point for the command dispatcher.
     *
     * <p>This field stores the root of the command tree structure. It is used to manage the execution of
     * commands and arguments by holding the initial node of the command hierarchy.</p>
     */
    private final RootCommandNode<S> root;

    /**
     * A predicate to determine whether a command node contains a command or any child nodes that do.
     *
     * <p>This predicate checks whether the given command node has an associated command or has any child nodes
     * that themselves contain a command. It is used for traversing and validating the command tree.</p>
     */
    private final Predicate<CommandNode<S>> hasCommand = new Predicate<CommandNode<S>>() {
        @Override
        public boolean test(final CommandNode<S> input) {
            return input != null && (input.getCommand() != null || input.getChildren().stream().anyMatch(hasCommand));
        }
    };

    /**
     * A consumer function to handle the result of a command execution.
     *
     * <p>This is a default implementation that does nothing. It is used as a placeholder for custom logic
     * that can be set later using {@link #setConsumer(ResultConsumer)}. The consumer will be notified after
     * the execution of each command, providing the outcome of the execution.</p>
     */
    private ResultConsumer<S> consumer = (c, s, r) -> {
    };

    /**
     * Creates a new {@link CommandDispatcher} with the specified root node.
     *
     * <p>This constructor is useful when you want to initialize the dispatcher with an existing or pre-defined command tree.
     * The provided {@link RootCommandNode} will serve as the root node for the new command dispatcher, allowing you to
     * reuse or modify an existing command structure.</p>
     *
     * @param root the existing {@link RootCommandNode} to use as the root of the command tree for this dispatcher
     */
    public CommandDispatcher(final RootCommandNode<S> root) {
        this.root = root;
    }

    /**
     * Creates a new {@link CommandDispatcher} with an empty command tree.
     *
     * <p>This constructor initializes the dispatcher with a new {@link RootCommandNode}, which serves as the root of
     * the command tree. This root node will be empty, and commands can be registered using other methods.</p>
     */
    public CommandDispatcher() {
        this(new RootCommandNode<>());
    }

    /**
     * Registers a new command by adding a literal argument to the command tree.
     *
     * <p>This is a shortcut for building the provided {@link LiteralArgumentCrafter} and then adding the resulting
     * {@link LiteralCommandNode} to the {@link RootCommandNode} using {@link RootCommandNode#addChild(CommandNode)}.</p>
     *
     * <p>Note that {@link RootCommandNode} can only hold literal arguments, so this method only allows literal arguments
     * and will not work for non-literal or dynamic arguments.</p>
     *
     * @param command the literal argument crafter to build and register in the command tree
     * @return the {@link LiteralCommandNode} that was added to the command tree
     * @throws IllegalArgumentException if the provided command is null
     */
    public LiteralCommandNode<S> register(final LiteralArgumentCrafter<S> command) {
        if (command == null) {
            throw new IllegalArgumentException("Command cannot be null");
        }

        final LiteralCommandNode<S> node = (LiteralCommandNode<S>) command.build();
        root.addChild(node);
        return node;
    }

    /**
     * Sets a callback to be informed of the result of every command execution.
     *
     * <p>This method allows you to register a {@link ResultConsumer} that will be called after each command is
     * executed. The consumer will receive details about the command's execution, including the result and any success
     * or failure indications. This is useful for handling the outcome of commands, especially in scenarios where
     * you want to perform additional actions based on the results of the command.</p>
     *
     * @param consumer the new result consumer that will be invoked after each command execution
     * @throws IllegalArgumentException if the consumer is null
     */
    public void setConsumer(final ResultConsumer<S> consumer) {
        if (consumer == null) {
            throw new IllegalArgumentException("Consumer cannot be null");
        }
        this.consumer = consumer;
    }

    /**
     * Parses and executes a given command string.
     *
     * <p>This method is a shortcut that combines both {@link #parse(StringReader, Object)} and
     * {@link #execute(ParseResults)} to provide a single step for parsing and executing the command.</p>
     *
     * <p>It is generally recommended to separate parsing and execution into distinct steps, as parsing is typically
     * the most expensive operation and can be cached for better performance.</p>
     *
     * <p>If the command executes successfully, a numeric result is returned, indicating the outcome of the execution.
     * If the command fails to parse or execute, an exception will be thrown. Most exceptions will be of type
     * {@link CommandSyntaxException}, but a {@link RuntimeException} could also be thrown if an unexpected error occurs
     * during execution.</p>
     *
     * <p>If the command passes through a node that is {@link CommandNode#isFork()}, it will be 'forked'.
     * In this case, the command will not propagate any {@link CommandSyntaxException}s, and the result will be the
     * number of successful commands executed.</p>
     *
     * <p>After executing the command, a registered {@link ResultConsumer} will be notified of the result and success
     * of the command. This callback can provide more detailed information, particularly in the case of forked commands.</p>
     *
     * @param input the command string to parse and execute
     * @param source a custom "source" object, typically representing the originator of the command
     * @return a numeric result indicating the outcome of the executed command
     * @throws CommandSyntaxException if the command failed to parse or execute
     * @throws RuntimeException if the command failed to execute and was not handled gracefully
     * @see #parse(String, Object)
     * @see #parse(StringReader, Object)
     * @see #execute(ParseResults)
     * @see #execute(StringReader, Object)
     */
    public int execute(final String input, final S source) throws CommandSyntaxException {
        // Parse the command string using the input and source
        return execute(new StringReader(input), source);
    }

    /**
     * Parses and executes a given command in a single step.
     *
     * <p>This method is a convenient shortcut that first calls {@link #parse(StringReader, Object)} to parse the
     * command and then calls {@link #execute(ParseResults)} to execute the parsed command.</p>
     *
     * <p>It is generally recommended to parse and execute commands as separate steps, since parsing is often
     * the most computationally expensive part and can be easily cached for performance optimizations.</p>
     *
     * <p>If the command executes successfully, it will return a numeric result indicating the outcome of the execution.
     * If the command could not be parsed, or if the execution fails, an exception will be thrown. Most exceptions will
     * be of type {@link CommandSyntaxException}, though {@link RuntimeException} may also be thrown depending on the
     * nature of the command execution.</p>
     *
     * <p>If the command passes through a node that is {@link CommandNode#isFork()}, it will be 'forked'.
     * Forked commands do not propagate {@link CommandSyntaxException}s, and instead, the returned result represents
     * the number of successful commands that were executed.</p>
     *
     * <p>Once the command is executed, any registered {@link ResultConsumer} will be notified of the result and success
     * of the command execution. This callback can be used to gather more detailed results, especially in the case of
     * forked commands.</p>
     *
     * @param input the command string to parse and execute
     * @param source a custom "source" object, usually representing the originator of the command
     * @return a numeric result representing the outcome of the executed command
     * @throws CommandSyntaxException if the command failed to parse or execute due to syntax errors
     * @throws RuntimeException if the command failed to execute due to an unexpected runtime error
     * @see #parse(String, Object)
     * @see #parse(StringReader, Object)
     * @see #execute(ParseResults)
     * @see #execute(String, Object)
     */
    public int execute(final StringReader input, final S source) throws CommandSyntaxException {
        // Parse the input command string
        final ParseResults<S> parse = parse(input, source);

        // Execute the parsed command and return the result
        return execute(parse);
    }

    /**
     * Executes a given pre-parsed command and returns a numeric result indicating the outcome of the command execution.
     *
     * <p>This method performs the execution of a command that has already been parsed. If the command was successfully
     * executed, it returns a numeric result that represents the outcome of the command. If the execution fails, an
     * exception will be thrown. Typically, failures will result in a {@link CommandSyntaxException}, but there may
     * also be instances where a {@link RuntimeException} is thrown if the command execution was not handled properly.</p>
     *
     * <p>If the command traverses a node that is {@link CommandNode#isFork()}, it will be considered 'forked'.
     * Forked commands do not propagate {@link CommandSyntaxException}s, and the 'result' returned will instead
     * represent the number of successful commands executed.</p>
     *
     * <p>After the command is executed, any registered {@link ResultConsumer} will be notified of the result and
     * success of the command execution. You can use this callback to receive more detailed feedback, especially
     * when the command involves forking.</p>
     *
     * @param parse the result of a successful {@link #parse(StringReader, Object)} execution
     * @return a numeric result representing the outcome of the executed command
     * @throws CommandSyntaxException if the command failed to parse or execute due to syntax errors
     * @throws RuntimeException if the command failed to execute due to an unexpected runtime error
     * @see #parse(String, Object)
     * @see #parse(StringReader, Object)
     * @see #execute(String, Object)
     * @see #execute(StringReader, Object)
     */
    public int execute(final ParseResults<S> parse) throws CommandSyntaxException {
        // Check if there are unread characters in the reader, indicating an incomplete command
        if (parse.getReader().canRead()) {
            // If there is a single exception, throw it
            if (parse.getExceptions().size() == 1) {
                throw parse.getExceptions().values().iterator().next();
            }
            // If no context range is available, throw an unknown command exception
            else if (parse.getContext().getRange().isEmpty()) {
                throw CommandSyntaxException.BUILT_IN_EXCEPTIONS
                        .dispatcherUnknownCommand()
                        .createWithContext(parse.getReader());
            }
            // Otherwise, throw an unknown argument exception
            else {
                throw CommandSyntaxException.BUILT_IN_EXCEPTIONS
                        .dispatcherUnknownArgument()
                        .createWithContext(parse.getReader());
            }
        }

        // Retrieve the original command string from the parse result
        final String command = parse.getReader().getString();
        final CommandContext<S> original = parse.getContext().build(command);

        // Attempt to flatten the context chain for execution
        final Optional<ContextChain<S>> flatContext = ContextChain.tryFlatten(original);
        if (!flatContext.isPresent()) {
            // Notify the consumer of the failed command and throw an unknown command exception
            consumer.onCommandComplete(original, false, 0);
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS
                    .dispatcherUnknownCommand()
                    .createWithContext(parse.getReader());
        }

        // Execute all commands in the flattened context and return the result
        return flatContext.get().executeAll(original.getSource(), consumer);
    }

    /**
     * Parses a given command string and returns the result as {@link ParseResults}.
     *
     * <p>This method allows you to easily parse a command string, starting from the root of the command structure.
     * Internally, it converts the command string into a {@link StringReader} and delegates the parsing process to
     * the {@link #parse(StringReader, Object)} method. This approach enables flexibility, allowing users to provide
     * either a raw command string or a pre-constructed {@link StringReader}.</p>
     *
     * <p>It is recommended to cache the result of this method when parsing the same command string multiple times,
     * as the parsing process can be computationally expensive. Caching allows you to essentially "precompile" the
     * command, significantly improving performance when the command is frequently executed.</p>
     *
     * <p>If the command traverses through a node that is {@link CommandNode#isFork()}, the resulting context will
     * be marked as 'forked'. Forked contexts may contain child contexts, which can be modified by the
     * {@link RedirectModifier} attached to the fork.</p>
     *
     * <p>Although parsing a command will never throw an exception, it is important to validate the returned result.
     * The returned {@link ParseResults} provides detailed feedback on whether the command was parsed successfully.
     * If the {@link ParseResults#getReader()} indicates that it can still read, this means the command did not fully
     * parse, and the position where parsing stopped can be used to highlight the issue to the user.</p>
     *
     * <p>Additionally, you may inspect {@link ParseResults#getExceptions()} for any encountered errors during the
     * parsing process. This list will explain why parsing could not continue down certain paths, providing context
     * for the failure and allowing for better error handling.</p>
     *
     * <p>When you later call {@link #execute(ParseResults)} with the result from this method, the relevant error
     * checking will be performed. You only need to inspect the parse results yourself if you want to handle any
     * potential issues before execution.</p>
     *
     * @param command the command string to parse
     * @param source the custom "source" object, representing the origin of the command
     * @return the result of parsing the command, encapsulated in a {@link ParseResults} object
     * @see #parse(StringReader, Object)
     * @see #execute(ParseResults)
     * @see #execute(String, Object)
     */
    public ParseResults<S> parse(final String command, final S source) {
        // Convert the command string to a StringReader and delegate parsing
        return parse(new StringReader(command), source);
    }

    /**
     * Parses a given command string and returns the result as {@link ParseResults}.
     *
     * <p>This method is typically used to parse user input or command strings. It constructs the initial context for
     * the command parsing process and delegates the actual parsing to the {@link #parseNodes(CommandNode, StringReader, CommandContextBuilder)}
     * method, starting from the root command node.</p>
     *
     * <p>It is recommended to cache the result of this method when parsing the same command frequently, as the
     * parsing step is often the most expensive operation. Caching allows you to "precompile" the command, making
     * subsequent invocations more efficient.</p>
     *
     * <p>If the command traverses through a {@link CommandNode#isFork()} node during parsing, the resulting context
     * will be marked as 'forked'. Forked contexts may contain child contexts, which can be further modified by the
     * {@link RedirectModifier} associated with the fork.</p>
     *
     * <p>Although parsing a command cannot fail in terms of throwing an exception, the parsing process may result in
     * incomplete or invalid command parsing. You should check the {@link ParseResults#getReader()} to see if the
     * parsing completed successfully. If the reader is still able to read, it indicates the command did not fully
     * parse. The position where the reader stopped can be used to provide feedback to the user.</p>
     *
     * <p>Additionally, you may inspect {@link ParseResults#getExceptions()} for any encountered errors during parsing.
     * This will provide insights into why the command could not be parsed and may contain multiple exceptions if
     * several potential paths were considered and failed.</p>
     *
     * <p>When you eventually call {@link #execute(ParseResults)} using the result of this method, the necessary error
     * checking will occur. You only need to inspect the parse results yourself if you want to handle errors before execution.</p>
     *
     * @param command the command string to parse
     * @param source the custom "source" object representing the origin of this command
     * @return the result of parsing the command, encapsulated in a {@link ParseResults} object
     * @see #parse(String, Object)
     * @see #execute(ParseResults)
     * @see #execute(String, Object)
     */
    public ParseResults<S> parse(final StringReader command, final S source) {
        // Create a new command context for parsing, starting at the root node
        final CommandContextBuilder<S> context = new CommandContextBuilder<>(this, source, root, command.getCursor());

        // Delegate parsing to the parseNodes method and return the result
        return parseNodes(root, command, context);
    }

    /**
     * Parses the nodes starting from the given node, processing each relevant child node in the command tree.
     *
     * <p>This method iterates through the child nodes, attempting to parse each node and its arguments from the
     * input provided by {@code originalReader}. If a node is successfully parsed, it is added to the list of
     * potential results. The method also handles cases where an argument separator is expected and
     * throws the appropriate exception if it is missing. In case of any parsing errors, the method accumulates
     * those exceptions and continues parsing the other children.</p>
     *
     * <p>The results are returned as a {@link ParseResults} object, which contains the context, the reader,
     * and any parsing exceptions that may have occurred.</p>
     *
     * @param node the starting node to begin parsing from
     * @param originalReader the reader containing the command input to parse
     * @param contextSoFar the context that has been accumulated so far during the parsing process
     * @return the parsing results, containing the context, reader, and exceptions encountered
     * @throws CommandSyntaxException if the parsing encounters an error that cannot be recovered from
     */
    private ParseResults<S> parseNodes(final CommandNode<S> node, final StringReader originalReader, final CommandContextBuilder<S> contextSoFar) {
        // Get the source from the current context
        final S source = contextSoFar.getSource();

        // Store errors and potential results for later processing
        Map<CommandNode<S>, CommandSyntaxException> errors = null;
        List<ParseResults<S>> potentials = null;

        // Remember the original cursor position for error recovery
        final int cursor = originalReader.getCursor();

        // Iterate over all relevant child nodes of the current node
        for (final CommandNode<S> child : node.getRelevantNodes(originalReader)) {

            // Skip nodes that cannot be used by the current source
            if (!child.canUse(source)) {
                continue;
            }

            // Create a copy of the context for this child node
            final CommandContextBuilder<S> context = contextSoFar.copy();
            final StringReader reader = new StringReader(originalReader);

            try {
                // Attempt to parse the child node
                try {
                    child.parse(reader, context);
                } catch (final RuntimeException ex) {
                    // Wrap any runtime exceptions in a command syntax exception
                    throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherParseException().createWithContext(reader, ex.getMessage());
                }

                // Ensure that an argument separator is present if there is more to read
                if (reader.canRead() && reader.peek() != ARGUMENT_SEPARATOR_CHAR) {
                    throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherExpectedArgumentSeparator().createWithContext(reader);
                }

            } catch (final CommandSyntaxException ex) {
                // Collect any exceptions encountered during parsing
                if (errors == null) {
                    errors = new LinkedHashMap<>();
                }
                errors.put(child, ex);

                // Reset the reader cursor to continue with other potential parses
                reader.setCursor(cursor);
                continue;
            }

            // Associate the parsed command with the context
            context.withCommand(child.getCommand());

            // Check if there is more input to read, or if the child node redirects to another node
            if (reader.canRead(child.getRedirect() == null ? 2 : 1)) {
                reader.skip(); // Move the cursor forward

                // Handle redirection to another command node
                if (child.getRedirect() != null) {
                    final CommandContextBuilder<S> childContext = new CommandContextBuilder<>(this, source, child.getRedirect(), reader.getCursor());
                    final ParseResults<S> parse = parseNodes(child.getRedirect(), reader, childContext);
                    context.withChild(parse.getContext());
                    return new ParseResults<>(context, parse.getReader(), parse.getExceptions());
                } else {
                    // Recursively parse the child node
                    final ParseResults<S> parse = parseNodes(child, reader, context);
                    if (potentials == null) {
                        potentials = new ArrayList<>(1);
                    }
                    potentials.add(parse);
                }
            } else {
                // If no more input is available, consider the current context as a potential result
                if (potentials == null) {
                    potentials = new ArrayList<>(1);
                }
                potentials.add(new ParseResults<>(context, reader, Collections.emptyMap()));
            }
        }

        // If multiple potential results were found, sort them based on criteria
        if (potentials != null) {
            if (potentials.size() > 1) {
                potentials.sort((a, b) -> {
                    // Sort by whether the reader can still read and the presence of exceptions
                    if (!a.getReader().canRead() && b.getReader().canRead()) {
                        return -1;
                    }
                    if (a.getReader().canRead() && !b.getReader().canRead()) {
                        return 1;
                    }
                    if (a.getExceptions().isEmpty() && !b.getExceptions().isEmpty()) {
                        return -1;
                    }
                    if (!a.getExceptions().isEmpty() && b.getExceptions().isEmpty()) {
                        return 1;
                    }
                    return 0;
                });
            }
            return potentials.get(0);
        }

        // If no potentials were found, return the context with the accumulated errors
        return new ParseResults<>(contextSoFar, originalReader, errors == null ? Collections.emptyMap() : errors);
    }

    /**
     * Gets all possible executable commands following the given node.
     *
     * <p>You may use {@link #getRoot()} as a target to get all usage data for the entire command tree.</p>
     *
     * <p>The returned syntax will be in "simple" form: {@code <param>} and {@code literal}. "Optional" nodes will be
     * listed as multiple entries: the parent node, and the child nodes.
     * For example, a required literal "foo" followed by an optional param "int" will be two nodes:</p>
     * <ul>
     *     <li>{@code foo}</li>
     *     <li>{@code foo <int>}</li>
     * </ul>
     *
     * <p>The path to the specified node will <b>not</b> be prepended to the output, as there can theoretically be many
     * ways to reach a given node. It will only give you paths relative to the specified node, not absolute from root.</p>
     *
     * @param node target node to get child usage strings for
     * @param source a custom "source" object, usually representing the originator of this command
     * @param restricted if true, commands that the {@code source} cannot access will not be mentioned
     * @return array of full usage strings under the target node
     */
    public String[] getAllUsage(final CommandNode<S> node, final S source, final boolean restricted) {
        final ArrayList<String> result = new ArrayList<>();
        getAllUsage(node, source, result, "", restricted);
        return result.toArray(new String[result.size()]);
    }

    /**
     * Retrieves all possible usage strings for commands under a specified node.
     *
     * <p>This method recursively collects usage strings for all command nodes, considering their children and any redirects.
     * If the {@code restricted} flag is set, it will only include commands the {@code source} is allowed to use.</p>
     *
     * <p>If the node has a command, it will add the usage string to the result list. Additionally, if the node has a redirect,
     * it will append the usage of the redirect node in a specific format.</p>
     *
     * <p>The usage strings are prefixed with the given {@code prefix}, and child nodes' usages are nested under the parent's usage.</p>
     *
     * @param node the current command node to collect usage data for
     * @param source the source object representing the originator of this command, used for access control
     * @param result the list to accumulate the resulting usage strings
     * @param prefix the current prefix to add to the usage string
     * @param restricted if {@code true}, restricts the results to only commands that the {@code source} can use
     */
    private void getAllUsage(final CommandNode<S> node, final S source, final ArrayList<String> result, final String prefix, final boolean restricted) {
        // Return early if the node is restricted and cannot be used by the source
        if (restricted && !node.canUse(source)) {
            return;
        }

        // Add the usage text for the current node if it has a command
        if (node.getCommand() != null) {
            result.add(prefix);
        }

        // If the node has a redirect, handle it by appending the redirect usage
        if (node.getRedirect() != null) {
            final String redirect = node.getRedirect() == root ? "..." : "-> " + node.getRedirect().getUsageText();
            result.add(formatUsage(prefix, node.getUsageText(), redirect));
        } else if (!node.getChildren().isEmpty()) {
            // Recursively get usage for child nodes
            for (final CommandNode<S> child : node.getChildren()) {
                final String newPrefix = prefix.isEmpty() ? child.getUsageText() : prefix + ARGUMENT_SEPARATOR + child.getUsageText();
                getAllUsage(child, source, result, newPrefix, restricted);
            }
        }
    }

    /**
     * Helper method to format a usage string with an optional prefix and redirect.
     *
     * @param prefix the prefix to add to the usage string
     * @param usageText the usage text of the current node
     * @param redirect the redirect string (if any)
     * @return the formatted usage string
     */
    private String formatUsage(final String prefix, final String usageText, final String redirect) {
        return prefix.isEmpty() ? usageText + ARGUMENT_SEPARATOR + redirect : prefix + ARGUMENT_SEPARATOR + redirect;
    }

    /**
     * Retrieves the possible executable commands from a specified node in a "smart" form.
     *
     * <p>This method provides a way to get usage data for child nodes under a specific command node. You can use {@link #getRoot()}
     * to get usage data for the entire command tree.</p>
     *
     * <p>The returned syntax will be in "smart" form: {@code <param>}, {@code literal}, {@code [optional]} and {@code (either|or)}.
     * These forms are mixed to provide clear information about the child nodes while avoiding unnecessary verbosity.
     * For example, a required literal "foo" followed by an optional parameter "int" can be expressed as:</p>
     * <ul>
     *     <li>{@code foo [<int>]}</li>
     * </ul>
     *
     * <p>The path to the specified node is not prepended to the output, as there may be multiple ways to reach a node.
     * Only relative paths to the target node are provided, not absolute paths from the root.</p>
     *
     * <p>The returned usage strings are restricted to commands that the provided {@code source} can execute.</p>
     *
     * @param node the target node for which to retrieve child usage strings
     * @param source the source object representing the origin of this command
     * @return a map containing the command nodes as keys and their respective usage strings as values
     */
    public Map<CommandNode<S>, String> getSmartUsage(final CommandNode<S> node, final S source) {
        final Map<CommandNode<S>, String> result = new LinkedHashMap<>();

        // Iterate over the children of the current node
        for (final CommandNode<S> child : node.getChildren()) {
            // Determine if the child can be used by the source
            if (child.canUse(source)) {
                // Get the smart usage string for the child node
                final String usage = getSmartUsage(child, source, node.getCommand() != null, false);
                // If the usage is not null, add it to the result map
                if (usage != null) {
                    result.put(child, usage);
                }
            }
        }
        return result;
    }

    /**
     * Generates a smart usage string for the provided command node.
     *
     * <p>This method recursively constructs a usage string for a command node, considering the optionality
     * and the presence of child nodes or redirection. It also accounts for whether the node is deep within the tree.</p>
     *
     * @param node the command node for which to generate the usage string
     * @param source the source that will be used to check command accessibility
     * @param optional whether the command argument is optional
     * @param deep whether to recursively process child nodes
     * @return a smart usage string for the node, or {@code null} if the node is not usable by the source
     */
    private String getSmartUsage(final CommandNode<S> node, final S source, final boolean optional, final boolean deep) {
        // Return null if the node cannot be used by the source
        if (!node.canUse(source)) {
            return null;
        }

        // Construct the base usage text
        final String self = optional ? USAGE_OPTIONAL_OPEN + node.getUsageText() + USAGE_OPTIONAL_CLOSE : node.getUsageText();
        final boolean childOptional = node.getCommand() != null; // Children are optional if the node has a command
        final String open = childOptional ? USAGE_OPTIONAL_OPEN : USAGE_REQUIRED_OPEN;
        final String close = childOptional ? USAGE_OPTIONAL_CLOSE : USAGE_REQUIRED_CLOSE;

        // If we are not processing deeply, handle redirection and children nodes
        if (!deep) {
            // Handle redirection if applicable
            if (node.getRedirect() != null) {
                final String redirect = node.getRedirect() == root ? "..." : "-> " + node.getRedirect().getUsageText();
                return self + ARGUMENT_SEPARATOR + redirect;
            }

            // Handle children nodes
            final Collection<CommandNode<S>> children = node.getChildren().stream()
                    .filter(c -> c.canUse(source)) // Only include children that can be used by the source
                    .toList();

            // If there is exactly one usable child node, recursively get its usage
            if (children.size() == 1) {
                final String usage = getSmartUsage(children.iterator().next(), source, childOptional, childOptional);
                if (usage != null) {
                    return self + ARGUMENT_SEPARATOR + usage;
                }
            }
            // If there are multiple children, handle their usage strings
            else if (children.size() > 1) {
                final Set<String> childUsage = new LinkedHashSet<>();
                for (final CommandNode<S> child : children) {
                    final String usage = getSmartUsage(child, source, childOptional, true);
                    if (usage != null) {
                        childUsage.add(usage);
                    }
                }

                // If there is exactly one child usage, append it to the current usage
                if (childUsage.size() == 1) {
                    final String usage = childUsage.iterator().next();
                    return self + ARGUMENT_SEPARATOR + (childOptional ? USAGE_OPTIONAL_OPEN + usage + USAGE_OPTIONAL_CLOSE : usage);
                }
                // If there are multiple child usages, combine them using 'or' notation
                else if (childUsage.size() > 1) {
                    final StringBuilder builder = new StringBuilder(open);
                    int count = 0;
                    for (final CommandNode<S> child : children) {
                        if (count > 0) {
                            builder.append(USAGE_OR);
                        }
                        builder.append(child.getUsageText());
                        count++;
                    }
                    builder.append(close);
                    return self + ARGUMENT_SEPARATOR + builder.toString();
                }
            }
        }

        // Return the usage string for the current node
        return self;
    }

    /**
     * Gets suggestions for a parsed input string on what comes next.
     *
     * <p>As the suggestions are typically provided by custom argument types, which might involve
     * asynchronous operations (such as querying in-game data or player names), this method returns
     * a {@link CompletableFuture} that will resolve at an indeterminate time.</p>
     *
     * <p>Suggestions will be relevant to the position in the parsed input string where the cursor is
     * currently placed. However, it is possible that suggestions could include full replacements for parts
     * of the input string. For instance, if the input is {@code foobar}, a suggestion might replace it
     * entirely with {@code minecraft:foobar} if that is the preferred format for a given argument type.</p>
     *
     * @param parse the parsed results obtained from a prior {@link #parse(StringReader, Object)}
     * @return a {@link CompletableFuture} that resolves into a {@link Suggestions} object with completion options
     */
    public CompletableFuture<Suggestions> getCompletionSuggestions(final ParseResults<S> parse) {
        // Delegate to the other getCompletionSuggestions method, passing the current input length
        return getCompletionSuggestions(parse, parse.getReader().getTotalLength());
    }

    /**
     * Retrieves completion suggestions for the input at the specified cursor position.
     *
     * <p>This method processes the current parse results and attempts to find the possible suggestions
     * based on the provided cursor position. It triggers suggestions for each child node of the parent
     * and merges them into a single result.</p>
     *
     * @param parse the current parse results containing the context
     * @param cursor the current position in the input string where the cursor is located
     * @return a {@link CompletableFuture} that completes with the suggestions at the cursor position
     */
    public CompletableFuture<Suggestions> getCompletionSuggestions(final ParseResults<S> parse, int cursor) {
        final CommandContextBuilder<S> context = parse.getContext();

        final SuggestionContext<S> nodeBeforeCursor = context.findSuggestionContext(cursor);
        final CommandNode<S> parent = nodeBeforeCursor.parent();
        final int start = Math.min(nodeBeforeCursor.startPos(), cursor);

        final String fullInput = parse.getReader().getString();
        final String truncatedInput = fullInput.substring(0, cursor);
        final String truncatedInputLowerCase = truncatedInput.toLowerCase(Locale.ROOT);

        @SuppressWarnings("unchecked")
        final CompletableFuture<Suggestions>[] futures = new CompletableFuture[parent.getChildren().size()];
        int i = 0;

        for (final CommandNode<S> node : parent.getChildren()) {
            CompletableFuture<Suggestions> future = Suggestions.empty();
            try {
                future = node.listSuggestions(context.build(truncatedInput), new SuggestionsBuilder(truncatedInput, truncatedInputLowerCase, start));
            } catch (final CommandSyntaxException ignored) {
            }
            futures[i++] = future;
        }

        final CompletableFuture<Suggestions> result = new CompletableFuture<>();
        CompletableFuture.allOf(futures).thenRun(() -> {
            final List<Suggestions> suggestions = new ArrayList<>();
            for (final CompletableFuture<Suggestions> future : futures) {
                suggestions.add(future.join());
            }
            result.complete(Suggestions.merge(fullInput, suggestions));
        });

        return result;
    }

    /**
     * Retrieves the root of the command tree.
     *
     * <p>This method is commonly used to access the root for various operations such as:
     * <ul>
     *   <li>Redirecting to the root with {@link com.github.nightfall.scriptor.crafter.ArgumentCrafter#redirect(CommandNode)}</li>
     *   <li>Getting all usage with {@link #getAllUsage(CommandNode, Object, boolean)}</li>
     *   <li>Obtaining smart usage with {@link #getSmartUsage(CommandNode, Object)}</li>
     * </ul>
     * Additionally, you can clone the command tree via {@link #CommandDispatcher(RootCommandNode)}
     *
     * @return The root of the command tree.
     */
    public RootCommandNode<S> getRoot() {
        return root;
    }

    /**
     * Finds a valid path from the root to the given target node in the command tree.
     *
     * <p>Multiple paths may exist to the same node, particularly when forking or redirecting is used.
     * However, this method only finds the first available path and does not consider forks or redirects.
     * It returns the first instance of the target node encountered in the tree.</p>
     *
     * <p>The result of this method, when called on the same command tree and library version, will
     * always be a valid input for {@link #findNode(Collection)}, which should return the same node
     * as the one provided to this method.</p>
     *
     * @param target The target node to find the path for.
     * @return A list representing the path to the target node, or an empty list if the node is not found.
     */
    public Collection<String> getPath(final CommandNode<S> target) {
        final List<String> path = new ArrayList<>();
        if (findPath(root, target, path)) {
            return path;
        }
        return Collections.emptyList();
    }

    /**
     * Recursively finds the path from the root to the specified target node.
     *
     * <p>This helper method traverses the command tree starting from the current node and builds the path to the target node.
     * It adds each node's name to the path as it goes. If the target node is found, the method returns {@code true},
     * and the path is built accordingly. If the target node cannot be found, the method returns {@code false}.</p>
     *
     * @param current The node being checked during the traversal.
     * @param target The target node being searched for.
     * @param path The current path being constructed, with node names added as traversal progresses.
     * @return {@code true} if the target node is found, {@code false} otherwise.
     */
    private boolean findPath(CommandNode<S> current, CommandNode<S> target, List<String> path) {
        if (current == target) {
            return true;
        }

        for (CommandNode<S> child : current.getChildren()) {
            path.add(child.getName());
            if (findPath(child, target, path)) {
                return true;
            }
            path.remove(path.size() - 1);
        }

        return false;
    }


    /**
     * Finds a command node by its specified path.
     *
     * <p>Paths are typically generated using {@link #getPath(CommandNode)}. For the same command tree and version
     * of this library, the same valid node will always be found by this method given a valid path.</p>
     *
     * <p>If the specified path does not lead to a valid node, {@code null} will be returned.</p>
     *
     * @param path A collection of strings representing the path to the node.
     * @return The node at the given path, or {@code null} if the node cannot be found.
     */
    public CommandNode<S> findNode(final Collection<String> path) {
        CommandNode<S> node = root;
        for (final String name : path) {
            node = node.getChild(name);
            if (node == null) {
                return null;
            }
        }
        return node;
    }


    /**
     * Scans the command tree for potential ambiguities between command nodes.
     * <p>
     * This method acts as a shortcut for calling {@link CommandNode#findAmbiguities(AmbiguityConsumer)} on the root
     * command node, {@link #getRoot()}.
     * </p>
     * <p>
     * Ambiguities are detected by comparing the examples from {@link CommandNode#getExamples()} of each command node
     * against every sibling node. Note that this method is not foolproof and heavily relies on the example data provided
     * by the argument type providers.
     * </p>
     *
     * @param consumer A callback to be notified whenever potential ambiguities are found during the scan.
     */
    public void findAmbiguities(final AmbiguityConsumer<S> consumer) {
        root.findAmbiguities(consumer);  // Delegate the ambiguity check to the root node
    }

    /**
     * Recursively adds paths of command nodes to the result list.
     * Each path represents a chain of command nodes starting from the provided node.
     * The method traverses the children of each command node and adds all possible paths to the result.
     *
     * @param node The current command node to process and add paths for.
     * @param result The list that will hold all the paths as lists of command nodes.
     * @param parents The current path of parent nodes to be extended with the current node and its children.
     */
    private void addPaths(final CommandNode<S> node, final List<List<CommandNode<S>>> result, final List<CommandNode<S>> parents) {
        // Create a new list for the current path, which includes the current node
        final List<CommandNode<S>> current = new ArrayList<>(parents);
        current.add(node);
        result.add(current);  // Add the current path to the result

        // Recursively process each child node to extend the path
        for (final CommandNode<S> child : node.getChildren()) {
            addPaths(child, result, current);  // Recursive call for the child node
        }
    }

    /**
     * Gets all possible paths of command nodes starting from the root node.
     *
     * <p>This method returns a list of paths, where each path is a list of command nodes.
     * Each path represents a chain of command nodes starting from the root node and traversing
     * through the children of each node. The paths are constructed by recursively adding child nodes
     * to the current path, starting from the root node.</p>
     *
     * <p>Each path in the result list is a list of command nodes, where the first node is the root node
     * and the last node is a leaf node. The paths are constructed by traversing the children of each node
     * and adding them to the current path.</p>
     *
     * @return A list of paths, where each path is a list of command nodes.
     * @since 1.3
     */
    public List<List<CommandNode<S>>> getAllPaths() {
        final List<List<CommandNode<S>>> result = new ArrayList<>();
        addPaths(root, result, Collections.emptyList());
        return result;
    }

    /**
     * Gets all possible paths of command nodes starting from the specified node.
     *
     * <p>This method returns a list of paths, where each path is a list of command nodes.
     * Each path represents a chain of command nodes starting from the specified node and traversing
     * through the children of each node. The paths are constructed by recursively adding child nodes
     * to the current path, starting from the specified node.</p>
     *
     * <p>Each path in the result list is a list of command nodes, where the first node is the specified node
     * and the last node is a leaf node. The paths are constructed by traversing the children of each node
     * and adding them to the current path.</p>
     *
     * @param node The node to start constructing paths from.
     * @return A list of paths, where each path is a list of command nodes.
     * @since 1.3
     */
    public List<List<CommandNode<S>>> getAllPaths(final CommandNode<S> node) {
        final List<List<CommandNode<S>>> result = new ArrayList<>();
        addPaths(node, result, Collections.emptyList());
        return result;
    }
}
