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

import com.github.nightfall.scriptor.interfaces.Message;

/**
 * Represents a syntax-related exception that occurs during command execution.
 * This exception is thrown when there is an error in the syntax of the command input.
 * It provides context and details about the error, including the position of the error
 * within the input string, if available.
 *
 * <p>This class allows capturing both a generic error message and a more specific
 * contextual message related to the command input.</p>
 *
 * @author M4ximumpizza
 * @since 1.0
 */
public class CommandSyntaxException extends Exception {

    /** Number of characters to include in context before the error position. */
    public static final int CONTEXT_AMOUNT = 10;

    /** Flag to enable or disable stack traces for command syntax exceptions. */
    public static final boolean ENABLE_COMMAND_STACK_TRACES = true;

    /** Provider for built-in exceptions. */
    public static final BuiltInExceptionProvider BUILT_IN_EXCEPTIONS = new BuiltInExceptions();

    private final CommandExceptionType type;
    private final Message message;
    private final String input;
    private final int cursor;

    /**
     * Constructs a new CommandSyntaxException without input context.
     *
     * @param type    the type of the exception
     * @param message the detailed error message
     */
    public CommandSyntaxException(final CommandExceptionType type, final Message message) {
        super(message.string(), null, ENABLE_COMMAND_STACK_TRACES, ENABLE_COMMAND_STACK_TRACES);
        this.type = type;
        this.message = message;
        this.input = null;
        this.cursor = -1;
    }

    /**
     * Constructs a new CommandSyntaxException with input context.
     *
     * @param type    the type of the exception
     * @param message the detailed error message
     * @param input   the command input causing the error
     * @param cursor  the position in the input where the error occurred
     */
    public CommandSyntaxException(final CommandExceptionType type, final Message message, final String input, final int cursor) {
        super(message.string(), null, ENABLE_COMMAND_STACK_TRACES, ENABLE_COMMAND_STACK_TRACES);
        this.type = type;
        this.message = message;
        this.input = input;
        this.cursor = cursor;
    }

    /**
     * Returns the detailed error message, including context if available.
     *
     * <p>If the input and cursor are provided, the error message will include context
     * of the command input, showing the position of the error with surrounding characters.</p>
     *
     * @return the formatted error message
     */
    @Override
    public String getMessage() {
        String baseMessage = message.string();
        if (input == null || cursor < 0) {
            return baseMessage;
        }

        // Include context only if needed
        String context = getContext();
        return baseMessage + " at position " + cursor + ": " + context;
    }

    /**
     * Returns the raw error message.
     *
     * @return the raw message
     */
    public Message getRawMessage() {
        return message;
    }

    /**
     * Retrieves the context of the error, showing surrounding input and position.
     * If context is unavailable (input or cursor is not provided), returns null.
     *
     * @return the error context, or null if unavailable
     */
    public String getContext() {
        if (input == null || cursor < 0) {
            return null;
        }

        int adjustedCursor = Math.min(input.length(), cursor);
        StringBuilder contextBuilder = new StringBuilder();

        // Add the context before the error position
        int start = Math.max(0, adjustedCursor - CONTEXT_AMOUNT);
        if (adjustedCursor > CONTEXT_AMOUNT) {
            contextBuilder.append("...");
        }

        // Append the surrounding characters from the input
        contextBuilder.append(input, start, adjustedCursor);
        contextBuilder.append("<--[HERE] Nice try! ");

        return contextBuilder.toString();
    }

    /**
     * Returns the type of this exception.
     *
     * @return the exception type
     */
    public CommandExceptionType getType() {
        return type;
    }

    /**
     * Returns the input causing the exception.
     *
     * @return the input string
     */
    public String getInput() {
        return input;
    }

    /**
     * Returns the cursor position where the error occurred.
     *
     * @return the cursor position
     */
    public int getCursor() {
        return cursor;
    }
}

