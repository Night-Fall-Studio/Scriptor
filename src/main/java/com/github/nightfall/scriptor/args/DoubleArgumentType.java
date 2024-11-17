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
 * Represents a double argument type that can be parsed from input and used in a command context.
 * This class provides methods for parsing a double value, setting minimum and maximum values,
 * and providing examples.
 *
 * @since 1.0.0
 * @author M4ximumpizza
 */
public class DoubleArgumentType implements ArgumentType<Double> {

    private static final Collection<String> EXAMPLES = Arrays.asList("0", "1.2", ".5", "-1", "-.5", "-1234.56");

    private final double minimum;
    private final double maximum;

    /**
     * Private constructor to create a new {@link DoubleArgumentType} with specified minimum and maximum values.
     *
     * @param minimum The minimum allowed value for the double argument.
     * @param maximum The maximum allowed value for the double argument.
     */
    private DoubleArgumentType(final double minimum, final double maximum) {
        this.minimum = minimum;
        this.maximum = maximum;
    }

    /**
     * Creates a new {@link DoubleArgumentType} with no constraints (min = -Double.MAX_VALUE, max = Double.MAX_VALUE).
     *
     * @return A new {@link DoubleArgumentType} instance.
     */
    public static DoubleArgumentType doubleArg() {
        return doubleArg(-Double.MAX_VALUE);
    }

    /**
     * Creates a new {@link DoubleArgumentType} with a specified minimum value and no maximum constraint.
     *
     * @param min The minimum allowed value for the double argument.
     * @return A new {@link DoubleArgumentType} instance.
     */
    public static DoubleArgumentType doubleArg(final double min) {
        return doubleArg(min, Double.MAX_VALUE);
    }

    /**
     * Creates a new {@link DoubleArgumentType} with specified minimum and maximum values.
     *
     * @param min The minimum allowed value for the double argument.
     * @param max The maximum allowed value for the double argument.
     * @return A new {@link DoubleArgumentType} instance.
     */
    public static DoubleArgumentType doubleArg(final double min, final double max) {
        return new DoubleArgumentType(min, max);
    }

    /**
     * Retrieves the double argument value from the command context.
     *
     * @param context The command context containing the parsed arguments.
     * @param name The name of the argument to retrieve.
     * @return The double value associated with the specified argument name.
     */
    public static double getDouble(final CommandContext<?> context, final String name) {
        return context.getArgument(name, Double.class);
    }

    /**
     * Gets the minimum allowed value for the double argument.
     *
     * @return The minimum value.
     */
    public double getMinimum() {
        return minimum;
    }

    /**
     * Gets the maximum allowed value for the double argument.
     *
     * @return The maximum value.
     */
    public double getMaximum() {
        return maximum;
    }

    /**
     * Parses the double argument from the provided {@link StringReader}.
     *
     * @param reader The reader that provides the input to parse.
     * @return The parsed double value.
     * @throws CommandSyntaxException If the input cannot be parsed as a double or is out of the allowed range.
     */
    @Override
    public Double parse(final StringReader reader) throws CommandSyntaxException {
        final int start = reader.getCursor();
        final double result = reader.readDouble();
        if (result < minimum) {
            reader.setCursor(start);
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.doubleTooLow().createWithContext(reader, result, minimum);
        }
        if (result > maximum) {
            reader.setCursor(start);
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.doubleTooHigh().createWithContext(reader, result, maximum);
        }
        return result;
    }

    /**
     * Checks if this {@link DoubleArgumentType} is equal to another object.
     *
     * @param o The object to compare this instance with.
     * @return True if the other object is a {@link DoubleArgumentType} with the same minimum and maximum values.
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof DoubleArgumentType)) return false;
        final DoubleArgumentType that = (DoubleArgumentType) o;
        return maximum == that.maximum && minimum == that.minimum;
    }

    /**
     * Computes the hash code for this {@link DoubleArgumentType}.
     *
     * @return The hash code based on the minimum and maximum values.
     */
    @Override
    public int hashCode() {
        return (int) (31 * minimum + maximum);
    }

    /**
     * Returns a string representation of this {@link DoubleArgumentType}.
     *
     * @return A string representing the argument type, including the minimum and maximum values if set.
     */
    @Override
    public String toString() {
        if (minimum == -Double.MAX_VALUE && maximum == Double.MAX_VALUE) {
            return "double()";
        } else if (maximum == Double.MAX_VALUE) {
            return "double(" + minimum + ")";
        } else {
            return "double(" + minimum + ", " + maximum + ")";
        }
    }

    /**
     * Provides a collection of example values for the double argument.
     *
     * @return A collection of example values for this argument type.
     */
    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}

