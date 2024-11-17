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

package com.github.nightfall.scriptor.args;

import com.github.nightfall.scriptor.StringReader;
import com.github.nightfall.scriptor.context.CommandContext;
import com.github.nightfall.scriptor.exceptions.CommandSyntaxException;

import java.util.Arrays;
import java.util.Collection;

import java.util.Arrays;
import java.util.Collection;

/**
 * Represents a string argument type that can be parsed from input and used in a command context.
 * This class supports various string types like single words, quoted phrases, and greedy phrases.
 *
 * @see StringType
 * @see ArgumentType
 * @author M4ximumpizza
 * @since 1.0
 */
public class StringArgumentType implements ArgumentType<String> {

    private final StringType type;

    /**
     * Private constructor to create a new {@link StringArgumentType} with a specified string type.
     *
     * @param type The type of the string argument (e.g., SINGLE_WORD, QUOTABLE_PHRASE).
     */
    private StringArgumentType(final StringType type) {
        this.type = type;
    }

    /**
     * Creates a new {@link StringArgumentType} for parsing a single word argument (no spaces).
     *
     * @return A new {@link StringArgumentType} instance for a single word.
     */
    public static StringArgumentType word() {
        return new StringArgumentType(StringType.SINGLE_WORD);
    }

    /**
     * Creates a new {@link StringArgumentType} for parsing a quoted string argument (supports spaces).
     *
     * @return A new {@link StringArgumentType} instance for a quoted phrase.
     */
    public static StringArgumentType string() {
        return new StringArgumentType(StringType.QUOTABLE_PHRASE);
    }

    /**
     * Creates a new {@link StringArgumentType} for parsing a greedy string argument (captures everything until the end).
     *
     * @return A new {@link StringArgumentType} instance for a greedy phrase.
     */
    public static StringArgumentType greedyString() {
        return new StringArgumentType(StringType.GREEDY_PHRASE);
    }

    /**
     * Retrieves the string argument value from the command context.
     *
     * @param context The command context containing the parsed arguments.
     * @param name The name of the argument to retrieve.
     * @return The string value associated with the specified argument name.
     */
    public static String getString(final CommandContext<?> context, final String name) {
        return context.getArgument(name, String.class);
    }

    /**
     * Gets the {@link StringType} associated with this string argument.
     *
     * @return The {@link StringType} of this argument (e.g., SINGLE_WORD, QUOTABLE_PHRASE).
     */
    public StringType getType() {
        return type;
    }

    /**
     * Parses the string argument from the provided {@link StringReader}.
     *
     * @param reader The reader that provides the input to parse.
     * @return The parsed string value.
     * @throws CommandSyntaxException If the input cannot be parsed.
     */
    @Override
    public String parse(final StringReader reader) throws CommandSyntaxException {
        if (type == StringType.GREEDY_PHRASE) {
            // Parse everything remaining in the reader for a greedy phrase.
            final String text = reader.getRemaining();
            reader.setCursor(reader.getTotalLength());
            return text;
        } else if (type == StringType.SINGLE_WORD) {
            // Parse a single unquoted word.
            return reader.readUnquotedString();
        } else {
            // Parse a quoted string (allows spaces).
            return reader.readString();
        }
    }

    /**
     * Returns a string representation of this {@link StringArgumentType}.
     *
     * @return A string representing the argument type, typically "string()".
     */
    @Override
    public String toString() {
        return "string()";
    }

    /**
     * Provides a collection of example values for the string argument, based on its type.
     *
     * @return A collection of example string values.
     */
    @Override
    public Collection<String> getExamples() {
        return type.getExamples();
    }

    /**
     * Escapes the input string if it contains characters that require escaping (e.g., quotes or backslashes).
     *
     * @param input The input string to check and possibly escape.
     * @return The escaped string if necessary, or the original input if no escaping is required.
     */
    public static String escapeIfRequired(final String input) {
        for (final char c : input.toCharArray()) {
            if (!StringReader.isAllowedInUnquotedString(c)) {
                return escape(input);
            }
        }
        return input;
    }

    /**
     * Escapes special characters in the input string, such as backslashes and quotes.
     *
     * @param input The string to escape.
     * @return The escaped string, enclosed in double quotes.
     */
    private static String escape(final String input) {
        final StringBuilder result = new StringBuilder("\"");

        for (int i = 0; i < input.length(); i++) {
            final char c = input.charAt(i);
            if (c == '\\' || c == '"') {
                result.append('\\');
            }
            result.append(c);
        }

        result.append("\"");
        return result.toString();
    }

    /**
     * Enum representing the different types of string arguments that can be parsed.
     */
    public enum StringType {

        SINGLE_WORD("word", "words_with_underscores"),
        QUOTABLE_PHRASE("\"quoted phrase\"", "word", "\"\""),
        GREEDY_PHRASE("word", "words with spaces", "\"and symbols\"");

        private final Collection<String> examples;

        /**
         * Constructor for creating a {@link StringType} with a set of example values.
         *
         * @param examples Example string values for this type.
         */
        StringType(final String... examples) {
            this.examples = Arrays.asList(examples);
        }

        /**
         * Gets the collection of example values for this string type.
         *
         * @return A collection of example strings.
         */
        public Collection<String> getExamples() {
            return examples;
        }
    }
}

