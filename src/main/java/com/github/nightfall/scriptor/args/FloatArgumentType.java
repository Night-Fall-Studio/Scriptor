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
 * Represents a float argument type that can be parsed from input and used in a command context.
 * This class provides methods for parsing a float value, setting minimum and maximum values,
 * and providing examples.
 *
 * @author M4ximumpizza
 * @version 1.0
 */
public class FloatArgumentType implements ArgumentType<Float> {

    private static final Collection<String> EXAMPLES = Arrays.asList("0", "1.2", ".5", "-1", "-.5", "-1234.56");

    private final float minimum;
    private final float maximum;

    /**
     * Private constructor to create a new {@link FloatArgumentType} with specified minimum and maximum values.
     *
     * @param minimum The minimum allowed value for the float argument.
     * @param maximum The maximum allowed value for the float argument.
     */
    private FloatArgumentType(final float minimum, final float maximum) {
        this.minimum = minimum;
        this.maximum = maximum;
    }

    /**
     * Creates a new {@link FloatArgumentType} with no constraints (min = -Float.MAX_VALUE, max = Float.MAX_VALUE).
     *
     * @return A new {@link FloatArgumentType} instance.
     */
    public static FloatArgumentType floatArg() {
        return floatArg(-Float.MAX_VALUE);
    }

    /**
     * Creates a new {@link FloatArgumentType} with a specified minimum value and no maximum constraint.
     *
     * @param min The minimum allowed value for the float argument.
     * @return A new {@link FloatArgumentType} instance.
     */
    public static FloatArgumentType floatArg(final float min) {
        return floatArg(min, Float.MAX_VALUE);
    }

    /**
     * Creates a new {@link FloatArgumentType} with specified minimum and maximum values.
     *
     * @param min The minimum allowed value for the float argument.
     * @param max The maximum allowed value for the float argument.
     * @return A new {@link FloatArgumentType} instance.
     */
    public static FloatArgumentType floatArg(final float min, final float max) {
        return new FloatArgumentType(min, max);
    }

    /**
     * Retrieves the float argument value from the command context.
     *
     * @param context The command context containing the parsed arguments.
     * @param name The name of the argument to retrieve.
     * @return The float value associated with the specified argument name.
     */
    public static float getFloat(final CommandContext<?> context, final String name) {
        return context.getArgument(name, Float.class);
    }

    /**
     * Gets the minimum allowed value for the float argument.
     *
     * @return The minimum value.
     */
    public float getMinimum() {
        return minimum;
    }

    /**
     * Gets the maximum allowed value for the float argument.
     *
     * @return The maximum value.
     */
    public float getMaximum() {
        return maximum;
    }

    /**
     * Parses the float argument from the provided {@link StringReader}.
     *
     * @param reader The reader that provides the input to parse.
     * @return The parsed float value.
     * @throws CommandSyntaxException If the input cannot be parsed as a float or is out of the allowed range.
     */
    @Override
    public Float parse(final StringReader reader) throws CommandSyntaxException {
        final int start = reader.getCursor();
        final float result = reader.readFloat();
        if (result < minimum) {
            reader.setCursor(start);
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.floatTooLow().createWithContext(reader, result, minimum);
        }
        if (result > maximum) {
            reader.setCursor(start);
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.floatTooHigh().createWithContext(reader, result, maximum);
        }
        return result;
    }

    /**
     * Checks if this {@link FloatArgumentType} is equal to another object.
     *
     * @param o The object to compare this instance with.
     * @return True if the other object is a {@link FloatArgumentType} with the same minimum and maximum values.
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof FloatArgumentType)) return false;
        final FloatArgumentType that = (FloatArgumentType) o;
        return maximum == that.maximum && minimum == that.minimum;
    }

    /**
     * Computes the hash code for this {@link FloatArgumentType}.
     *
     * @return The hash code based on the minimum and maximum values.
     */
    @Override
    public int hashCode() {
        return (int) (31 * minimum + maximum);
    }

    /**
     * Returns a string representation of this {@link FloatArgumentType}.
     *
     * @return A string representing the argument type, including the minimum and maximum values if set.
     */
    @Override
    public String toString() {
        if (minimum == -Float.MAX_VALUE && maximum == Float.MAX_VALUE) {
            return "float()";
        } else if (maximum == Float.MAX_VALUE) {
            return "float(" + minimum + ")";
        } else {
            return "float(" + minimum + ", " + maximum + ")";
        }
    }

    /**
     * Provides a collection of example values for the float argument.
     *
     * @return A collection of example values for this argument type.
     */
    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}

