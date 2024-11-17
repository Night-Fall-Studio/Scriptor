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

import com.github.nightfall.scriptor.exceptions.dynamic.DynamicCommandExceptionType2;
import com.github.nightfall.scriptor.exceptions.dynamic.DynamicCommandExceptionType;

/**
 * Interface that provides methods for creating built-in exceptions used by the command system.
 * This interface defines various methods that generate specific types of {@link CommandExceptionType},
 * such as exceptions for invalid values, missing arguments, or parsing errors.
 *
 * <p>These exceptions are used by the command system to provide detailed error messages when a command
 * fails to parse or execute. The exceptions are thrown by the command system when an error occurs,
 * and the error message is displayed to the user to help them understand what went wrong.
 *
 * The built-in exceptions are designed to be generic and reusable, so they can be used by any command
 * system that implements the {@link BuiltInExceptionProvider} interface. This allows the command system
 * to provide consistent error messages across different commands and arguments, making it easier for
 * users to understand and fix errors in their commands.
 *
 * <p>Each method in this interface returns a specific type of {@link CommandExceptionType} that represents
 * a different type of error. For example, the {@link #doubleTooLow()} method returns a {@link DynamicCommandExceptionType2}
 * that represents an error when a double value is too low. The {@link #literalIncorrect()} method returns a
 * {@link DynamicCommandExceptionType} that represents an error when an incorrect literal value is found.
 * These exception types can be used by the command system to generate error messages with specific details
 * about the error that occurred.
 *
 * @author M4ximumpizza
 * @since 1.0
 */
public interface BuiltInExceptionProvider {

    /**
     * Provides a {@link DynamicCommandExceptionType2} for a double value that is too low.
     *
     * @return a {@link DynamicCommandExceptionType2} representing the exception
     */
    DynamicCommandExceptionType2 doubleTooLow();

    /**
     * Provides a {@link DynamicCommandExceptionType2} for a double value that is too high.
     *
     * @return a {@link DynamicCommandExceptionType2} representing the exception
     */
    DynamicCommandExceptionType2 doubleTooHigh();

    /**
     * Provides a {@link DynamicCommandExceptionType2} for a float value that is too low.
     *
     * @return a {@link DynamicCommandExceptionType2} representing the exception
     */
    DynamicCommandExceptionType2 floatTooLow();

    /**
     * Provides a {@link DynamicCommandExceptionType2} for a float value that is too high.
     *
     * @return a {@link DynamicCommandExceptionType2} representing the exception
     */
    DynamicCommandExceptionType2 floatTooHigh();

    /**
     * Provides a {@link DynamicCommandExceptionType2} for an integer value that is too low.
     *
     * @return a {@link DynamicCommandExceptionType2} representing the exception
     */
    DynamicCommandExceptionType2 integerTooLow();

    /**
     * Provides a {@link DynamicCommandExceptionType2} for an integer value that is too high.
     *
     * @return a {@link DynamicCommandExceptionType2} representing the exception
     */
    DynamicCommandExceptionType2 integerTooHigh();

    /**
     * Provides a {@link DynamicCommandExceptionType2} for a long value that is too low.
     *
     * @return a {@link DynamicCommandExceptionType2} representing the exception
     */
    DynamicCommandExceptionType2 longTooLow();

    /**
     * Provides a {@link DynamicCommandExceptionType2} for a long value that is too high.
     *
     * @return a {@link DynamicCommandExceptionType2} representing the exception
     */
    DynamicCommandExceptionType2 longTooHigh();

    /**
     * Provides a {@link DynamicCommandExceptionType} for an incorrect literal value.
     *
     * @return a {@link DynamicCommandExceptionType} representing the exception
     */
    DynamicCommandExceptionType literalIncorrect();

    /**
     * Provides a {@link SimpleCommandExceptionType} for a reader expecting the start of a quoted string.
     *
     * @return a {@link SimpleCommandExceptionType} representing the exception
     */
    SimpleCommandExceptionType readerExpectedStartOfQuote();

    /**
     * Provides a {@link SimpleCommandExceptionType} for a reader expecting the end of a quoted string.
     *
     * @return a {@link SimpleCommandExceptionType} representing the exception
     */
    SimpleCommandExceptionType readerExpectedEndOfQuote();

    /**
     * Provides a {@link DynamicCommandExceptionType} for an invalid escape sequence in the reader.
     *
     * @return a {@link DynamicCommandExceptionType} representing the exception
     */
    DynamicCommandExceptionType readerInvalidEscape();

    /**
     * Provides a {@link DynamicCommandExceptionType} for an invalid boolean value in the reader.
     *
     * @return a {@link DynamicCommandExceptionType} representing the exception
     */
    DynamicCommandExceptionType readerInvalidBool();

    /**
     * Provides a {@link DynamicCommandExceptionType} for an invalid integer value in the reader.
     *
     * @return a {@link DynamicCommandExceptionType} representing the exception
     */
    DynamicCommandExceptionType readerInvalidInt();

    /**
     * Provides a {@link SimpleCommandExceptionType} for the reader expecting an integer value.
     *
     * @return a {@link SimpleCommandExceptionType} representing the exception
     */
    SimpleCommandExceptionType readerExpectedInt();

    /**
     * Provides a {@link DynamicCommandExceptionType} for an invalid long value in the reader.
     *
     * @return a {@link DynamicCommandExceptionType} representing the exception
     */
    DynamicCommandExceptionType readerInvalidLong();

    /**
     * Provides a {@link SimpleCommandExceptionType} for the reader expecting a long value.
     *
     * @return a {@link SimpleCommandExceptionType} representing the exception
     */
    SimpleCommandExceptionType readerExpectedLong();

    /**
     * Provides a {@link DynamicCommandExceptionType} for an invalid double value in the reader.
     *
     * @return a {@link DynamicCommandExceptionType} representing the exception
     */
    DynamicCommandExceptionType readerInvalidDouble();

    /**
     * Provides a {@link SimpleCommandExceptionType} for the reader expecting a double value.
     *
     * @return a {@link SimpleCommandExceptionType} representing the exception
     */
    SimpleCommandExceptionType readerExpectedDouble();

    /**
     * Provides a {@link DynamicCommandExceptionType} for an invalid float value in the reader.
     *
     * @return a {@link DynamicCommandExceptionType} representing the exception
     */
    DynamicCommandExceptionType readerInvalidFloat();

    /**
     * Provides a {@link SimpleCommandExceptionType} for the reader expecting a float value.
     *
     * @return a {@link SimpleCommandExceptionType} representing the exception
     */
    SimpleCommandExceptionType readerExpectedFloat();

    /**
     * Provides a {@link SimpleCommandExceptionType} for the reader expecting a boolean value.
     *
     * @return a {@link SimpleCommandExceptionType} representing the exception
     */
    SimpleCommandExceptionType readerExpectedBool();

    /**
     * Provides a {@link DynamicCommandExceptionType} for the reader expecting a specific symbol.
     *
     * @return a {@link DynamicCommandExceptionType} representing the exception
     */
    DynamicCommandExceptionType readerExpectedSymbol();

    /**
     * Provides a {@link SimpleCommandExceptionType} for an unknown command in the dispatcher.
     *
     * @return a {@link SimpleCommandExceptionType} representing the exception
     */
    SimpleCommandExceptionType dispatcherUnknownCommand();

    /**
     * Provides a {@link SimpleCommandExceptionType} for an unknown argument in the dispatcher.
     *
     * @return a {@link SimpleCommandExceptionType} representing the exception
     */
    SimpleCommandExceptionType dispatcherUnknownArgument();

    /**
     * Provides a {@link SimpleCommandExceptionType} for an expected argument separator in the dispatcher.
     *
     * @return a {@link SimpleCommandExceptionType} representing the exception
     */
    SimpleCommandExceptionType dispatcherExpectedArgumentSeparator();

    /**
     * Provides a {@link DynamicCommandExceptionType} for a parsing exception in the dispatcher.
     *
     * @return a {@link DynamicCommandExceptionType} representing the exception
     */
    DynamicCommandExceptionType dispatcherParseException();
}

