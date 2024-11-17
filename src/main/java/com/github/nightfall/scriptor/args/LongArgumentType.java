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
 * Represents a long argument type that can be parsed from input and used in a command context.
 * This class provides methods for parsing a long value, setting minimum and maximum values,
 * and providing examples.
 *
 * @author M4ximumpizza
 * @since 1.0
 */
public class LongArgumentType implements ArgumentType<Long> {

    private static final Collection<String> EXAMPLES = Arrays.asList("0", "123", "-123");

    private final long minimum;
    private final long maximum;

    /**
     * Private constructor to create a new {@link LongArgumentType} with specified minimum and maximum values.
     *
     * @param minimum The minimum allowed value for the long argument.
     * @param maximum The maximum allowed value for the long argument.
     */
    private LongArgumentType(final long minimum, final long maximum) {
        this.minimum = minimum;
        this.maximum = maximum;
    }

    /**
     * Creates a new {@link LongArgumentType} with no constraints (min = Long.MIN_VALUE, max = Long.MAX_VALUE).
     *
     * @return A new {@link LongArgumentType} instance.
     */
    public static LongArgumentType longArg() {
        return longArg(Long.MIN_VALUE);
    }

    /**
     * Creates a new {@link LongArgumentType} with a specified minimum value and no maximum constraint.
     *
     * @param min The minimum allowed value for the long argument.
     * @return A new {@link LongArgumentType} instance.
     */
    public static LongArgumentType longArg(final long min) {
        return longArg(min, Long.MAX_VALUE);
    }

    /**
     * Creates a new {@link LongArgumentType} with specified minimum and maximum values.
     *
     * @param min The minimum allowed value for the long argument.
     * @param max The maximum allowed value for the long argument.
     * @return A new {@link LongArgumentType} instance.
     */
    public static LongArgumentType longArg(final long min, final long max) {
        return new LongArgumentType(min, max);
    }

    /**
     * Retrieves the long argument value from the command context.
     *
     * @param context The command context containing the parsed arguments.
     * @param name The name of the argument to retrieve.
     * @return The long value associated with the specified argument name.
     */
    public static long getLong(final CommandContext<?> context, final String name) {
        return context.getArgument(name, long.class);
    }

    /**
     * Gets the minimum allowed value for the long argument.
     *
     * @return The minimum value.
     */
    public long getMinimum() {
        return minimum;
    }

    /**
     * Gets the maximum allowed value for the long argument.
     *
     * @return The maximum value.
     */
    public long getMaximum() {
        return maximum;
    }

    /**
     * Parses the long argument from the provided {@link StringReader}.
     *
     * @param reader The reader that provides the input to parse.
     * @return The parsed long value.
     * @throws CommandSyntaxException If the input cannot be parsed as a long or is out of the allowed range.
     */
    @Override
    public Long parse(final StringReader reader) throws CommandSyntaxException {
        final int start = reader.getCursor();
        final long result = reader.read();
        if (result < minimum) {
            reader.setCursor(start);
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.longTooLow().createWithContext(reader, result, minimum);
        }
        if (result > maximum) {
            reader.setCursor(start);
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.longTooHigh().createWithContext(reader, result, maximum);
        }
        return result;
    }

    /**
     * Checks if this {@link LongArgumentType} is equal to another object.
     *
     * @param o The object to compare this instance with.
     * @return True if the other object is a {@link LongArgumentType} with the same minimum and maximum values.
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof LongArgumentType)) return false;
        final LongArgumentType that = (LongArgumentType) o;
        return maximum == that.maximum && minimum == that.minimum;
    }

    /**
     * Computes the hash code for this {@link LongArgumentType}.
     *
     * @return The hash code based on the minimum and maximum values.
     */
    @Override
    public int hashCode() {
        return 31 * Long.hashCode(minimum) + Long.hashCode(maximum);
    }

    /**
     * Returns a string representation of this {@link LongArgumentType}.
     *
     * @return A string representing the argument type, including the minimum and maximum values if set.
     */
    @Override
    public String toString() {
        if (minimum == Long.MIN_VALUE && maximum == Long.MAX_VALUE) {
            return "longArg()";
        } else if (maximum == Long.MAX_VALUE) {
            return "longArg(" + minimum + ")";
        } else {
            return "longArg(" + minimum + ", " + maximum + ")";
        }
    }

    /**
     * Provides a collection of example values for the long argument.
     *
     * @return A collection of example values for this argument type.
     */
    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}
