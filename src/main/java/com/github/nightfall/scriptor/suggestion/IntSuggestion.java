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

package com.github.nightfall.scriptor.suggestion;

import com.github.nightfall.scriptor.interfaces.Message;
import com.github.nightfall.scriptor.context.StringRange;

import java.util.Objects;

/**
 * Represents an integer suggestion, extending from the {@link Suggestion} class.
 * This class provides an integer value that can be used in contexts such as
 * command argument suggestions, along with an optional tooltip for additional information.
 *
 * <p>It supports equality comparison, hashing, and string representation for debugging purposes.</p>
 *
 * @author M4ximumpizza
 * @since 1.0
 */
public class IntSuggestion extends Suggestion {

    /** The integer value of the suggestion. */
    private final int value;

    /**
     * Constructs an IntSuggestion with the specified range and value, without a tooltip.
     *
     * @param range the range in the input where the suggestion is applicable
     * @param value the integer value of the suggestion
     */
    public IntSuggestion(final StringRange range, final int value) {
        this(range, value, null);
    }

    /**
     * Constructs an IntSuggestion with the specified range, value, and optional tooltip.
     *
     * @param range the range in the input where the suggestion is applicable
     * @param value the integer value of the suggestion
     * @param tooltip the optional tooltip to display alongside the suggestion
     */
    public IntSuggestion(final StringRange range, final int value, final Message tooltip) {
        super(range, Integer.toString(value), tooltip);
        this.value = value;
    }

    /**
     * Returns the integer value of this suggestion.
     *
     * @return the integer value of the suggestion
     */
    public int getValue() {
        return value;
    }

    /**
     * Compares this IntSuggestion to another object for equality.
     * Two IntSuggestions are equal if their value and the range, text, and tooltip are the same.
     *
     * @param o the object to compare this suggestion to
     * @return {@code true} if the objects are equal, otherwise {@code false}
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IntSuggestion)) {
            return false;
        }
        IntSuggestion that = (IntSuggestion) o;
        return value == that.value && super.equals(o);
    }

    /**
     * Returns a hash code for this IntSuggestion.
     *
     * @return a hash code for this IntSuggestion
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), value);
    }

    /**
     * Returns a string representation of this IntSuggestion, including the value, range, text, and tooltip.
     *
     * @return a string representation of this IntSuggestion
     */
    @Override
    public String toString() {
        return String.format("IntSuggestion{value=%d, range=%s, text='%s', tooltip='%s'}",
                value, getRange(), getText(), getTooltip());
    }

    /**
     * Compares this IntSuggestion to another suggestion based on the integer value.
     * If the other suggestion is not an instance of IntSuggestion, it falls back to the superclass comparison.
     *
     * @param o the suggestion to compare this one with
     * @return a negative integer, zero, or a positive integer if this suggestion is less than, equal to,
     *         or greater than the specified suggestion
     */
    @Override
    public int compareTo(final Suggestion o) {
        if (o instanceof IntSuggestion) {
            return Integer.compare(value, ((IntSuggestion) o).value);
        }
        return super.compareTo(o);
    }

    /**
     * Compares this IntSuggestion to another suggestion, ignoring case.
     * It is equivalent to {@link #compareTo(Suggestion)}.
     *
     * @param b the suggestion to compare this one with
     * @return the result of the comparison
     */
    @Override
    public int compareToIgnoreCase(final Suggestion b) {
        return compareTo(b);
    }
}

