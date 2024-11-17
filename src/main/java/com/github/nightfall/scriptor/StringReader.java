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

import com.github.nightfall.scriptor.exceptions.CommandSyntaxException;
import com.github.nightfall.scriptor.interfaces.ImmutableStringReader;

/**
 * A StringReader is a utility class for reading and parsing strings in a cursor-based manner.
 * It supports reading characters, numbers, quoted/unquoted strings, and skipping over whitespace.
 * It provides various methods to read different types of data from the string.
 *
 * @author M4ximumpizza
 * @since 1.0
 */
public class StringReader implements ImmutableStringReader {
    private static final char SYNTAX_ESCAPE = '\\';
    private static final char SYNTAX_DOUBLE_QUOTE = '"';
    private static final char SYNTAX_SINGLE_QUOTE = '\'';

    private final String string;
    private int cursor;

    /**
     * Creates a new StringReader by copying the state from another StringReader.
     *
     * @param other Another StringReader to copy.
     */
    public StringReader(final StringReader other) {
        this.string = other.string;
        this.cursor = other.cursor;
    }

    /**
     * Creates a new StringReader for the provided string.
     *
     * @param string The string to read from.
     */
    public StringReader(final String string) {
        this.string = string;
    }

    @Override
    public String getString() {
        return string;
    }

    /**
     * Sets the cursor to a specific position.
     *
     * @param cursor The new cursor position.
     */
    public void setCursor(final int cursor) {
        this.cursor = cursor;
    }

    @Override
    public int getRemainingLength() {
        return string.length() - cursor;
    }

    @Override
    public int getTotalLength() {
        return string.length();
    }

    @Override
    public int getCursor() {
        return cursor;
    }

    @Override
    public String getRead() {
        return string.substring(0, cursor);
    }

    @Override
    public String getRemaining() {
        return string.substring(cursor);
    }

    @Override
    public boolean canRead(final int length) {
        return cursor + length <= string.length();
    }

    @Override
    public boolean canRead() {
        return canRead(1);
    }

    @Override
    public char peek() {
        return string.charAt(cursor);
    }

    @Override
    public char peek(final int offset) {
        return string.charAt(cursor + offset);
    }

    /**
     * Reads and returns the current character, then advances the cursor.
     *
     * @return The character at the current cursor position.
     */
    public char read() {
        return string.charAt(cursor++);
    }

    /**
     * Skips the current character by advancing the cursor.
     */
    public void skip() {
        cursor++;
    }

    /**
     * Determines if a character is valid for a number (e.g., digit, decimal, negative sign).
     *
     * @param c The character to check.
     * @return true if the character is a valid part of a number, false otherwise.
     */
    public static boolean isAllowedNumber(final char c) {
        return c >= '0' && c <= '9' || c == '.' || c == '-';
    }

    /**
     * Determines if the character marks the start of a quoted string (single or double quote).
     *
     * @param c The character to check.
     * @return true if the character is a quote, false otherwise.
     */
    public static boolean isQuotedStringStart(char c) {
        return c == SYNTAX_DOUBLE_QUOTE || c == SYNTAX_SINGLE_QUOTE;
    }

    /**
     * Skips over any whitespace characters.
     */
    public void skipWhitespace() {
        while (canRead() && Character.isWhitespace(peek())) {
            skip();
        }
    }

    /**
     * Reads an integer value from the string.
     *
     * @return The parsed integer.
     * @throws CommandSyntaxException if the string does not represent a valid integer.
     */
    public int readInt() throws CommandSyntaxException {
        final int start = cursor;
        while (canRead() && isAllowedNumber(peek())) {
            skip();
        }
        final String number = string.substring(start, cursor);
        if (number.isEmpty()) {
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerExpectedInt().createWithContext(this);
        }
        try {
            return Integer.parseInt(number);
        } catch (final NumberFormatException ex) {
            cursor = start;
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerInvalidInt().createWithContext(this, number);
        }
    }

    /**
     * Reads a long value from the string.
     *
     * @return The parsed long.
     * @throws CommandSyntaxException if the string does not represent a valid long.
     */
    public long readLong() throws CommandSyntaxException {
        final int start = cursor;
        while (canRead() && isAllowedNumber(peek())) {
            skip();
        }
        final String number = string.substring(start, cursor);
        if (number.isEmpty()) {
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerExpectedLong().createWithContext(this);
        }
        try {
            return Long.parseLong(number);
        } catch (final NumberFormatException ex) {
            cursor = start;
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerInvalidLong().createWithContext(this, number);
        }
    }

    /**
     * Reads a double value from the string.
     *
     * @return The parsed double.
     * @throws CommandSyntaxException if the string does not represent a valid double.
     */
    public double readDouble() throws CommandSyntaxException {
        final int start = cursor;
        while (canRead() && isAllowedNumber(peek())) {
            skip();
        }
        final String number = string.substring(start, cursor);
        if (number.isEmpty()) {
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerExpectedDouble().createWithContext(this);
        }
        try {
            return Double.parseDouble(number);
        } catch (final NumberFormatException ex) {
            cursor = start;
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerInvalidDouble().createWithContext(this, number);
        }
    }

    /**
     * Reads a float value from the string.
     *
     * @return The parsed float.
     * @throws CommandSyntaxException if the string does not represent a valid float.
     */
    public float readFloat() throws CommandSyntaxException {
        final int start = cursor;
        while (canRead() && isAllowedNumber(peek())) {
            skip();
        }
        final String number = string.substring(start, cursor);
        if (number.isEmpty()) {
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerExpectedFloat().createWithContext(this);
        }
        try {
            return Float.parseFloat(number);
        } catch (final NumberFormatException ex) {
            cursor = start;
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerInvalidFloat().createWithContext(this, number);
        }
    }

    /**
     * Determines if the character is allowed in an unquoted string (e.g., alphanumeric, underscore, hyphen, etc.).
     *
     * @param c The character to check.
     * @return true if the character is allowed, false otherwise.
     */
    public static boolean isAllowedInUnquotedString(final char c) {
        return c >= '0' && c <= '9'
                || c >= 'A' && c <= 'Z'
                || c >= 'a' && c <= 'z'
                || c == '_' || c == '-'
                || c == '.' || c == '+';
    }

    /**
     * Reads an unquoted string (a sequence of valid characters).
     *
     * @return The unquoted string.
     */
    public String readUnquotedString() {
        final int start = cursor;
        while (canRead() && isAllowedInUnquotedString(peek())) {
            skip();
        }
        return string.substring(start, cursor);
    }

    /**
     * Reads a quoted string, handling escape sequences.
     *
     * @return The quoted string.
     * @throws CommandSyntaxException if the string is improperly quoted.
     */
    public String readQuotedString() throws CommandSyntaxException {
        if (!canRead()) {
            return "";
        }
        final char next = peek();
        if (!isQuotedStringStart(next)) {
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerExpectedStartOfQuote().createWithContext(this);
        }
        skip();
        return readStringUntil(next);
    }

    /**
     * Reads a string until the specified terminator character is encountered, handling escape sequences.
     *
     * @param terminator The character that ends the string.
     * @return The string read until the terminator.
     * @throws CommandSyntaxException if the terminator is not found or if an invalid escape sequence is encountered.
     */
    public String readStringUntil(char terminator) throws CommandSyntaxException {
        final StringBuilder result = new StringBuilder();
        boolean escaped = false;
        while (canRead()) {
            final char c = read();
            if (escaped) {
                if (c == terminator || c == SYNTAX_ESCAPE) {
                    result.append(c);
                    escaped = false;
                } else {
                    setCursor(getCursor() - 1);
                    throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerInvalidEscape().createWithContext(this, String.valueOf(c));
                }
            } else if (c == SYNTAX_ESCAPE) {
                escaped = true;
            } else if (c == terminator) {
                return result.toString();
            } else {
                result.append(c);
            }
        }

        throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerExpectedEndOfQuote().createWithContext(this);
    }

    /**
     * Reads a string, handling both quoted and unquoted formats.
     *
     * @return The string read.
     * @throws CommandSyntaxException if the string is improperly formatted.
     */
    public String readString() throws CommandSyntaxException {
        if (!canRead()) {
            return "";
        }
        final char next = peek();
        if (isQuotedStringStart(next)) {
            skip();
            return readStringUntil(next);
        }
        return readUnquotedString();
    }

    /**
     * Reads a boolean value ("true" or "false").
     *
     * @return The parsed boolean value.
     * @throws CommandSyntaxException if the value is not a valid boolean.
     */
    public boolean readBoolean() throws CommandSyntaxException {
        final int start = cursor;
        final String value = readString();
        if (value.isEmpty()) {
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerExpectedBool().createWithContext(this);
        }

        if (value.equals("true")) {
            return true;
        } else if (value.equals("false")) {
            return false;
        } else {
            cursor = start;
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerInvalidBool().createWithContext(this, value);
        }
    }

    /**
     * Ensures that the next character matches the expected symbol.
     *
     * @param c The expected character.
     * @throws CommandSyntaxException if the symbol does not match the expected character.
     */
    public void expect(final char c) throws CommandSyntaxException {
        if (!canRead() || peek() != c) {
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerExpectedSymbol().createWithContext(this, String.valueOf(c));
        }
        skip();
    }
}
