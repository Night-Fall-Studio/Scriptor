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

import com.github.nightfall.scriptor.LiteralMessage;
import com.github.nightfall.scriptor.exceptions.dynamic.DynamicCommandExceptionType2;
import com.github.nightfall.scriptor.exceptions.dynamic.DynamicCommandExceptionType;

/**
 * The {@code BuiltInExceptions} class implements the {@code BuiltInExceptionProvider} interface
 * and provides predefined exception types used for error handling in command processing.
 * These exceptions are designed to handle various types of invalid input, including invalid number ranges
 * (for {@code double}, {@code float}, {@code int}, and {@code long} types), incorrect literal values,
 * string parsing errors, and dispatcher command issues.
 *
 * <p>This class contains static final exception types, each associated with a specific error condition,
 * and provides getter methods to retrieve the appropriate exception type for each case.</p>
 *
 * <p>The exception types used in this class are:</p>
 * <ul>
 *   <li>{@code DynamicCommandExceptionType2} - For number range-related exceptions.</li>
 *   <li>{@code DynamicCommandExceptionType} - For various parsing and value-related exceptions.</li>
 *   <li>{@code SimpleCommandExceptionType} - For simpler command-related errors like unknown commands or missing separators.</li>
 * </ul>
 *
 * @author M4ximumpizza
 * @since 1.0
 */
public class BuiltInExceptions implements BuiltInExceptionProvider {

    // Exception for invalid number ranges (Double)
    private static final DynamicCommandExceptionType2 DOUBLE_TOO_SMALL = new DynamicCommandExceptionType2((found, min) -> new LiteralMessage("The provided double value (" + found + ") is below the minimum of " + min + "."));

    private static final DynamicCommandExceptionType2 DOUBLE_TOO_BIG = new DynamicCommandExceptionType2((found, max) -> new LiteralMessage("The provided double value (" + found + ") exceeds the maximum of " + max + "."));

    // Exception for invalid number ranges (Float)
    private static final DynamicCommandExceptionType2 FLOAT_TOO_SMALL = new DynamicCommandExceptionType2((found, min) -> new LiteralMessage("The float value (" + found + ") cannot be smaller than " + min + "."));

    private static final DynamicCommandExceptionType2 FLOAT_TOO_BIG = new DynamicCommandExceptionType2((found, max) -> new LiteralMessage("The float value (" + found + ") is too large, maximum allowed is " + max + "."));

    // Exception for invalid number ranges (Integer)
    private static final DynamicCommandExceptionType2 INTEGER_TOO_SMALL = new DynamicCommandExceptionType2((found, min) -> new LiteralMessage("The integer value (" + found + ") is smaller than the minimum allowed value: " + min + "."));

    private static final DynamicCommandExceptionType2 INTEGER_TOO_BIG = new DynamicCommandExceptionType2((found, max) -> new LiteralMessage("The integer value (" + found + ") exceeds the maximum allowed value of " + max + "."));

    // Exception for invalid number ranges (Long)
    private static final DynamicCommandExceptionType2 LONG_TOO_SMALL = new DynamicCommandExceptionType2((found, min) -> new LiteralMessage("The long value (" + found + ") is below the expected minimum of " + min + "."));

    private static final DynamicCommandExceptionType2 LONG_TOO_BIG = new DynamicCommandExceptionType2((found, max) -> new LiteralMessage("The long value (" + found + ") is above the maximum value of " + max + "."));

    // Exception for incorrect literal expectations
    private static final DynamicCommandExceptionType LITERAL_INCORRECT = new DynamicCommandExceptionType(expected -> new LiteralMessage("Expected a literal value: '" + expected + "', but got something else."));

    // String Reader Exceptions
    private static final SimpleCommandExceptionType READER_EXPECTED_START_OF_QUOTE = new SimpleCommandExceptionType(new LiteralMessage("A string should begin with a quote character, but none was found."));

    private static final SimpleCommandExceptionType READER_EXPECTED_END_OF_QUOTE = new SimpleCommandExceptionType(new LiteralMessage("Unmatched quote at the end of the string."));

    private static final DynamicCommandExceptionType READER_INVALID_ESCAPE = new DynamicCommandExceptionType(character -> new LiteralMessage("Invalid escape sequence encountered: '" + character + "' inside quoted string."));

    private static final DynamicCommandExceptionType READER_INVALID_BOOL = new DynamicCommandExceptionType(value -> new LiteralMessage("Invalid boolean value, expected 'true' or 'false', but found: '" + value + "'."));

    private static final DynamicCommandExceptionType READER_INVALID_INT = new DynamicCommandExceptionType(value -> new LiteralMessage("Invalid integer value: '" + value + "'."));

    private static final SimpleCommandExceptionType READER_EXPECTED_INT = new SimpleCommandExceptionType(new LiteralMessage("Expected an integer value."));

    private static final DynamicCommandExceptionType READER_INVALID_LONG = new DynamicCommandExceptionType(value -> new LiteralMessage("Invalid long value: '" + value + "'."));

    private static final SimpleCommandExceptionType READER_EXPECTED_LONG = new SimpleCommandExceptionType(new LiteralMessage("Expected a long value."));

    private static final DynamicCommandExceptionType READER_INVALID_DOUBLE = new DynamicCommandExceptionType(value -> new LiteralMessage("Invalid double value: '" + value + "'."));

    private static final SimpleCommandExceptionType READER_EXPECTED_DOUBLE = new SimpleCommandExceptionType(new LiteralMessage("Expected a double value."));

    private static final DynamicCommandExceptionType READER_INVALID_FLOAT = new DynamicCommandExceptionType(value -> new LiteralMessage("Invalid float value: '" + value + "'."));

    private static final SimpleCommandExceptionType READER_EXPECTED_FLOAT = new SimpleCommandExceptionType(new LiteralMessage("Expected a float value."));

    private static final SimpleCommandExceptionType READER_EXPECTED_BOOL = new SimpleCommandExceptionType(new LiteralMessage("Expected a boolean value."));

    private static final DynamicCommandExceptionType READER_EXPECTED_SYMBOL = new DynamicCommandExceptionType(symbol -> new LiteralMessage("Expected the symbol: '" + symbol + "', but got something else."));

    // Dispatcher Command Exceptions
    private static final SimpleCommandExceptionType DISPATCHER_UNKNOWN_COMMAND = new SimpleCommandExceptionType(new LiteralMessage("Unrecognized command. Please check your syntax."));

    private static final SimpleCommandExceptionType DISPATCHER_UNKNOWN_ARGUMENT = new SimpleCommandExceptionType(new LiteralMessage("The provided argument is not valid for this command."));

    private static final SimpleCommandExceptionType DISPATCHER_EXPECTED_ARGUMENT_SEPARATOR = new SimpleCommandExceptionType(new LiteralMessage("A separator (whitespace) was expected between arguments, but extra data was found."));

    private static final DynamicCommandExceptionType DISPATCHER_PARSE_EXCEPTION = new DynamicCommandExceptionType(message -> new LiteralMessage("Error while parsing the command: " + message + "."));

    /**
     * Returns the exception type for when a {@code double} value is too small.
     *
     * @return the {@link DynamicCommandExceptionType2} for a {@code double} value being below the minimum.
     */
    @Override
    public DynamicCommandExceptionType2 doubleTooLow() {
        return DOUBLE_TOO_SMALL;
    }

    /**
     * Returns the exception type for when a {@code double} value is too large.
     *
     * @return the {@link DynamicCommandExceptionType2} for a {@code double} value exceeding the maximum.
     */
    @Override
    public DynamicCommandExceptionType2 doubleTooHigh() {
        return DOUBLE_TOO_BIG;
    }

    /**
     * Returns the exception type for when a {@code float} value is too small.
     *
     * @return the {@link DynamicCommandExceptionType2} for a {@code float} value being smaller than the minimum.
     */
    @Override
    public DynamicCommandExceptionType2 floatTooLow() {
        return FLOAT_TOO_SMALL;
    }

    /**
     * Returns the exception type for when a {@code float} value is too large.
     *
     * @return the {@link DynamicCommandExceptionType2} for a {@code float} value exceeding the maximum.
     */
    @Override
    public DynamicCommandExceptionType2 floatTooHigh() {
        return FLOAT_TOO_BIG;
    }

    /**
     * Returns the exception type for when an {@code int} value is too small.
     *
     * @return the {@link DynamicCommandExceptionType2} for an {@code int} value being smaller than the minimum.
     */
    @Override
    public DynamicCommandExceptionType2 integerTooLow() {
        return INTEGER_TOO_SMALL;
    }

    /**
     * Returns the exception type for when an {@code int} value is too large.
     *
     * @return the {@link DynamicCommandExceptionType2} for an {@code int} value exceeding the maximum.
     */
    @Override
    public DynamicCommandExceptionType2 integerTooHigh() {
        return INTEGER_TOO_BIG;
    }

    /**
     * Returns the exception type for when a {@code long} value is too small.
     *
     * @return the {@link DynamicCommandExceptionType2} for a {@code long} value being smaller than the minimum.
     */
    @Override
    public DynamicCommandExceptionType2 longTooLow() {
        return LONG_TOO_SMALL;
    }

    /**
     * Returns the exception type for when a {@code long} value is too large.
     *
     * @return the {@link DynamicCommandExceptionType2} for a {@code long} value exceeding the maximum.
     */
    @Override
    public DynamicCommandExceptionType2 longTooHigh() {
        return LONG_TOO_BIG;
    }

    /**
     * Returns the exception type for incorrect literal values.
     *
     * @return the {@link DynamicCommandExceptionType} for a literal value mismatch.
     */
    @Override
    public DynamicCommandExceptionType literalIncorrect() {
        return LITERAL_INCORRECT;
    }

    /**
     * Returns the exception type for when a string reader encounters an expected start of a quote error.
     *
     * @return the {@link SimpleCommandExceptionType} for missing starting quote.
     */
    @Override
    public SimpleCommandExceptionType readerExpectedStartOfQuote() {
        return READER_EXPECTED_START_OF_QUOTE;
    }

    /**
     * Returns the exception type for when a string reader encounters an unexpected end of quote error.
     *
     * @return the {@link SimpleCommandExceptionType} for unmatched quote.
     */
    @Override
    public SimpleCommandExceptionType readerExpectedEndOfQuote() {
        return READER_EXPECTED_END_OF_QUOTE;
    }

    /**
     * Returns the exception type for invalid escape sequences inside a quoted string.
     *
     * @return the {@link DynamicCommandExceptionType} for invalid escape characters.
     */
    @Override
    public DynamicCommandExceptionType readerInvalidEscape() {
        return READER_INVALID_ESCAPE;
    }

    /**
     * Returns the exception type for invalid boolean values in the string reader.
     *
     * @return the {@link DynamicCommandExceptionType} for invalid boolean values.
     */
    @Override
    public DynamicCommandExceptionType readerInvalidBool() {
        return READER_INVALID_BOOL;
    }

    /**
     * Returns the exception type for invalid integer values in the string reader.
     *
     * @return the {@link DynamicCommandExceptionType} for invalid integer values.
     */
    @Override
    public DynamicCommandExceptionType readerInvalidInt() {
        return READER_INVALID_INT;
    }

    /**
     * Returns the exception type for expected integer values in the string reader.
     *
     * @return the {@link SimpleCommandExceptionType} for missing integer.
     */
    @Override
    public SimpleCommandExceptionType readerExpectedInt() {
        return READER_EXPECTED_INT;
    }

    /**
     * Returns the exception type for invalid long values in the string reader.
     *
     * @return the {@link DynamicCommandExceptionType} for invalid long values.
     */
    @Override
    public DynamicCommandExceptionType readerInvalidLong() {
        return READER_INVALID_LONG;
    }

    /**
     * Returns the exception type for expected long values in the string reader.
     *
     * @return the {@link SimpleCommandExceptionType} for missing long value.
     */
    @Override
    public SimpleCommandExceptionType readerExpectedLong() {
        return READER_EXPECTED_LONG;
    }

    /**
     * Returns the exception type for invalid double values in the string reader.
     *
     * @return the {@link DynamicCommandExceptionType} for invalid double values.
     */
    @Override
    public DynamicCommandExceptionType readerInvalidDouble() {
        return READER_INVALID_DOUBLE;
    }

    /**
     * Returns the exception type for expected double values in the string reader.
     *
     * @return the {@link SimpleCommandExceptionType} for missing double value.
     */
    @Override
    public SimpleCommandExceptionType readerExpectedDouble() {
        return READER_EXPECTED_DOUBLE;
    }

    /**
     * Returns the exception type for invalid float values in the string reader.
     *
     * @return the {@link DynamicCommandExceptionType} for invalid float values.
     */
    @Override
    public DynamicCommandExceptionType readerInvalidFloat() {
        return READER_INVALID_FLOAT;
    }

    /**
     * Returns the exception type for expected float values in the string reader.
     *
     * @return the {@link SimpleCommandExceptionType} for missing float value.
     */
    @Override
    public SimpleCommandExceptionType readerExpectedFloat() {
        return READER_EXPECTED_FLOAT;
    }

    /**
     * Returns the exception type for expected boolean values in the string reader.
     *
     * @return the {@link SimpleCommandExceptionType} for missing boolean value.
     */
    @Override
    public SimpleCommandExceptionType readerExpectedBool() {
        return READER_EXPECTED_BOOL;
    }

    /**
     * Returns the exception type for expected symbols in the string reader.
     *
     * @return the {@link DynamicCommandExceptionType} for expected symbols.
     */
    @Override
    public DynamicCommandExceptionType readerExpectedSymbol() {
        return READER_EXPECTED_SYMBOL;
    }

    /**
     * Returns the exception type for unknown commands in the dispatcher.
     *
     * @return the {@link SimpleCommandExceptionType} for unrecognized command.
     */
    @Override
    public SimpleCommandExceptionType dispatcherUnknownCommand() {
        return DISPATCHER_UNKNOWN_COMMAND;
    }

    /**
     * Returns the exception type for unknown arguments in the dispatcher.
     *
     * @return the {@link SimpleCommandExceptionType} for unrecognized argument.
     */
    @Override
    public SimpleCommandExceptionType dispatcherUnknownArgument() {
        return DISPATCHER_UNKNOWN_ARGUMENT;
    }

    /**
     * Returns the exception type for missing argument separators in the dispatcher.
     *
     * @return the {@link SimpleCommandExceptionType} for missing argument separator.
     */
    @Override
    public SimpleCommandExceptionType dispatcherExpectedArgumentSeparator() {
        return DISPATCHER_EXPECTED_ARGUMENT_SEPARATOR;
    }

    /**
     * Returns the exception type for general dispatcher parse exceptions.
     *
     * @return the {@link DynamicCommandExceptionType} for dispatcher parse errors.
     */
    @Override
    public DynamicCommandExceptionType dispatcherParseException() {
        return DISPATCHER_PARSE_EXCEPTION;
    }
}

