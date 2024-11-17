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

package com.github.nightfall.scriptor.exceptions;

import com.github.nightfall.scriptor.interfaces.ImmutableStringReader;
import com.github.nightfall.scriptor.interfaces.Message;

/**
 * Represents an exception type that carries a simple message for a command exception.
 * This class defines custom exceptions that are thrown during the processing of a command,
 * encapsulating the exception message and providing context when necessary.
 *
 * <p>This implementation allows the creation of exceptions with an associated message
 * and the option to include additional context, such as the command input and cursor position.</p>
 *
 * @author M4ximumpizza
 * @since 1.0
 */
public class SimpleCommandExceptionType implements CommandExceptionType {

    /** The message associated with this exception type. */
    private final Message message;

    /**
     * Constructs a new SimpleCommandExceptionType with the specified message.
     *
     * @param message The message to associate with this exception type.
     */
    public SimpleCommandExceptionType(final Message message) {
        this.message = message;
    }

    /**
     * Creates a new {@link CommandSyntaxException} using the current exception type
     * and the associated message.
     *
     * <p>This method constructs a simple exception without additional context.</p>
     *
     * @return A new instance of {@link CommandSyntaxException} with this exception type and message.
     */
    public CommandSyntaxException create() {
        return new CommandSyntaxException(this, message);
    }

    /**
     * Creates a new {@link CommandSyntaxException} with additional context from the
     * provided string reader, including the current input string and cursor position.
     *
     * <p>This method allows including contextual information when an error occurs,
     * such as the command input and the position where the error occurred.</p>
     *
     * @param reader The reader that provides the context (input string and cursor position).
     * @return A new instance of {@link CommandSyntaxException} with context information.
     */
    public CommandSyntaxException createWithContext(final ImmutableStringReader reader) {
        return new CommandSyntaxException(this, message, reader.getString(), reader.getCursor());
    }

    /**
     * Returns the string representation of this exception type, which is the associated message's string.
     *
     * <p>This representation can be used for logging or debugging purposes.</p>
     *
     * @return The string representation of the message.
     */
    @Override
    public String toString() {
        return message.string();
    }
}


