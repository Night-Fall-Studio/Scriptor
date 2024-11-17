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

import com.github.nightfall.scriptor.context.CommandContextBuilder;
import com.github.nightfall.scriptor.exceptions.CommandSyntaxException;
import com.github.nightfall.scriptor.interfaces.ImmutableStringReader;
import com.github.nightfall.scriptor.tree.CommandNode;

import java.util.Collections;
import java.util.Map;

/**
 * Represents the result of parsing a command, containing relevant context,
 * reader state, and any exceptions encountered during parsing.
 *
 * <p>This class holds the results of parsing a command string. It includes:
 * <ul>
 *   <li>The command context that holds parsed arguments and information about the command execution.</li>
 *   <li>The reader used to parse the command string, capturing the state of the input.</li>
 *   <li>A map of exceptions that occurred during parsing, keyed by the command nodes.</li>
 * </ul>
 *
 * <p>Instances of this class are typically passed between different stages of the command
 * execution process, allowing easy access to the context, reader, and error information.</p>
 *
 * @param <S> a custom "source" type, such as a user or originator of the command.
 * @author M4ximumpizza
 * @since 1.0
 */
public class ParseResults<S> {

    private final CommandContextBuilder<S> context;
    private final Map<CommandNode<S>, CommandSyntaxException> exceptions;
    private final ImmutableStringReader reader;

    /**
     * Constructs a new instance of {@link ParseResults} with the specified context, reader,
     * and exceptions.
     *
     * <p>This constructor allows for initializing a {@link ParseResults} object with specific
     * data that includes the context of the command, the reader state, and any parsing exceptions
     * that may have occurred.</p>
     *
     * @param context the command context builder, which holds parsed data
     * @param reader the reader used for parsing the command
     * @param exceptions any exceptions encountered during parsing, mapped to the command nodes
     */
    public ParseResults(final CommandContextBuilder<S> context, final ImmutableStringReader reader,
                        final Map<CommandNode<S>, CommandSyntaxException> exceptions) {
        this.context = context;
        this.reader = reader;
        this.exceptions = exceptions;
    }

    /**
     * Constructs a new instance of {@link ParseResults} with the specified context and default
     * reader state and empty exceptions map.
     *
     * <p>This constructor is a shorthand for creating a {@link ParseResults} instance when there
     * are no exceptions and an empty reader.</p>
     *
     * @param context the command context builder, which holds parsed data
     */
    public ParseResults(final CommandContextBuilder<S> context) {
        this(context, new StringReader(""), Collections.emptyMap());
    }

    /**
     * Returns the command context builder for this parse result.
     *
     * <p>The context contains all the parsed arguments and data required for executing the
     * command or further processing.</p>
     *
     * @return the {@link CommandContextBuilder} holding the parsed context
     */
    public CommandContextBuilder<S> getContext() {
        return context;
    }

    /**
     * Returns the reader used to parse the command string.
     *
     * <p>The reader contains the remaining input after parsing, and its state provides insights
     * into how much of the input has been processed and how much is left.</p>
     *
     * @return the {@link ImmutableStringReader} used to parse the input
     */
    public ImmutableStringReader getReader() {
        return reader;
    }

    /**
     * Returns any exceptions encountered during the parsing process.
     *
     * <p>This method returns a map of exceptions that occurred while parsing, where each exception
     * is associated with the command node where it occurred. These exceptions can be used for error
     * reporting or debugging.</p>
     *
     * @return a map of {@link CommandNode} to {@link CommandSyntaxException} representing
     *         parsing errors
     */
    public Map<CommandNode<S>, CommandSyntaxException> getExceptions() {
        return exceptions;
    }
}
