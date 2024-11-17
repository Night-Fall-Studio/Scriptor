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

/**
 * Represents an integer argument type that can be parsed from input and used in a command context.
 * This class provides methods for parsing an integer value, setting minimum and maximum values,
 * and providing examples.
 *
 * @author M4ximumpizza
 * @version 1.0
 */
public class IntegerArgumentType implements ArgumentType<Integer> {

    private static final Collection<String> EXAMPLES = Arrays.asList("0", "123", "-123");

    private final int minimum;
    private final int maximum;

    /**
     * Private constructor to create a new {@link IntegerArgumentType} with specified minimum and maximum values.
     *
     * @param minimum The minimum allowed value for the integer argument.
     * @param maximum The maximum allowed value for the integer argument.
     */
    private IntegerArgumentType(final int minimum, final int maximum) {
        this.minimum = minimum;
        this.maximum = maximum;
    }

    /**
     * Creates a new {@link IntegerArgumentType} with no constraints (min = Integer.MIN_VALUE, max = Integer.MAX_VALUE).
     *
     * @return A new {@link IntegerArgumentType} instance.
     */
    public static IntegerArgumentType integer() {
        return integer(Integer.MIN_VALUE);
    }

    /**
     * Creates a new {@link IntegerArgumentType} with a specified minimum value and no maximum constraint.
     *
     * @param min The minimum allowed value for the integer argument.
     * @return A new {@link IntegerArgumentType} instance.
     */
    public static IntegerArgumentType integer(final int min) {
        return integer(min, Integer.MAX_VALUE);
    }

    /**
     * Creates a new {@link IntegerArgumentType} with specified minimum and maximum values.
     *
     * @param min The minimum allowed value for the integer argument.
     * @param max The maximum allowed value for the integer argument.
     * @return A new {@link IntegerArgumentType} instance.
     */
    public static IntegerArgumentType integer(final int min, final int max) {
        return new IntegerArgumentType(min, max);
    }

    /**
     * Retrieves the integer argument value from the command context.
     *
     * @param context The command context containing the parsed arguments.
     * @param name The name of the argument to retrieve.
     * @return The integer value associated with the specified argument name.
     */
    public static int getInteger(final CommandContext<?> context, final String name) {
        return context.getArgument(name, int.class);
    }

    /**
     * Gets the minimum allowed value for the integer argument.
     *
     * @return The minimum value.
     */
    public int getMinimum() {
        return minimum;
    }

    /**
     * Gets the maximum allowed value for the integer argument.
     *
     * @return The maximum value.
     */
    public int getMaximum() {
        return maximum;
    }

    /**
     * Parses the integer argument from the provided {@link StringReader}.
     *
     * @param reader The reader that provides the input to parse.
     * @return The parsed integer value.
     * @throws CommandSyntaxException If the input cannot be parsed as an integer or is out of the allowed range.
     */
    @Override
    public Integer parse(final StringReader reader) throws CommandSyntaxException {
        final int start = reader.getCursor();
        final int result = reader.readInt();
        if (result < minimum) {
            reader.setCursor(start);
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.integerTooLow().createWithContext(reader, result, minimum);
        }
        if (result > maximum) {
            reader.setCursor(start);
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.integerTooHigh().createWithContext(reader, result, maximum);
        }
        return result;
    }

    /**
     * Checks if this {@link IntegerArgumentType} is equal to another object.
     *
     * @param o The object to compare this instance with.
     * @return True if the other object is an {@link IntegerArgumentType} with the same minimum and maximum values.
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof IntegerArgumentType)) return false;
        final IntegerArgumentType that = (IntegerArgumentType) o;
        return maximum == that.maximum && minimum == that.minimum;
    }

    /**
     * Computes the hash code for this {@link IntegerArgumentType}.
     *
     * @return The hash code based on the minimum and maximum values.
     */
    @Override
    public int hashCode() {
        return 31 * minimum + maximum;
    }

    /**
     * Returns a string representation of this {@link IntegerArgumentType}.
     *
     * @return A string representing the argument type, including the minimum and maximum values if set.
     */
    @Override
    public String toString() {
        if (minimum == Integer.MIN_VALUE && maximum == Integer.MAX_VALUE) {
            return "integer()";
        } else if (maximum == Integer.MAX_VALUE) {
            return "integer(" + minimum + ")";
        } else {
            return "integer(" + minimum + ", " + maximum + ")";
        }
    }

    /**
     * Provides a collection of example values for the integer argument.
     *
     * @return A collection of example values for this argument type.
     */
    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}

