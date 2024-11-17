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
 * Represents a suggestion with a range and text, optionally including a tooltip.
 * This class is used for providing suggestions in contexts like autocompletion.
 *
 * <p>The suggestion includes the range of the input it applies to and can modify or replace that range
 * with the suggestion's text.</p>
 *
 * @author M4ximumpizza
 * @since 1.0
 */
public class Suggestion implements Comparable<Suggestion> {

    /** The range in the input where the suggestion is applicable. */
    private final StringRange range;

    /** The text of the suggestion. */
    private final String text;

    /** An optional tooltip associated with the suggestion. */
    private final Message tooltip;

    /**
     * Constructs a suggestion with the specified range and text, without a tooltip.
     *
     * @param range the range in the input where the suggestion applies
     * @param text the text of the suggestion
     */
    public Suggestion(final StringRange range, final String text) {
        this(range, text, null);
    }

    /**
     * Constructs a suggestion with the specified range, text, and optional tooltip.
     *
     * @param range the range in the input where the suggestion applies
     * @param text the text of the suggestion
     * @param tooltip an optional tooltip to display with the suggestion
     */
    public Suggestion(final StringRange range, final String text, final Message tooltip) {
        this.range = range;
        this.text = text;
        this.tooltip = tooltip;
    }

    /**
     * Returns the range of this suggestion.
     *
     * @return the range in the input where the suggestion applies
     */
    public StringRange getRange() {
        return range;
    }

    /**
     * Returns the text of this suggestion.
     *
     * @return the text of the suggestion
     */
    public String getText() {
        return text;
    }

    /**
     * Returns the tooltip associated with this suggestion, if any.
     *
     * @return the tooltip of the suggestion
     */
    public Message getTooltip() {
        return tooltip;
    }

    /**
     * Applies the suggestion's text to the given input string at the specified range.
     *
     * @param input the input string to apply the suggestion to
     * @return the input string with the suggestion's text applied at the specified range
     */
    public String apply(final String input) {
        if (range.start() == 0 && range.end() == input.length()) {
            return text;
        }
        StringBuilder result = new StringBuilder();
        if (range.start() > 0) {
            result.append(input.substring(0, range.start()));
        }
        result.append(text);
        if (range.end() < input.length()) {
            result.append(input.substring(range.end()));
        }
        return result.toString();
    }

    /**
     * Compares this suggestion to another for equality.
     * Two suggestions are equal if their range, text, and tooltip are identical.
     *
     * @param o the object to compare this suggestion to
     * @return {@code true} if the suggestions are equal, otherwise {@code false}
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Suggestion)) {
            return false;
        }
        Suggestion that = (Suggestion) o;
        return Objects.equals(range, that.range) &&
                Objects.equals(text, that.text) &&
                Objects.equals(tooltip, that.tooltip);
    }

    /**
     * Returns a hash code for this suggestion.
     *
     * @return a hash code for this suggestion
     */
    @Override
    public int hashCode() {
        return Objects.hash(range, text, tooltip);
    }

    /**
     * Returns a string representation of this suggestion, including its range, text, and tooltip.
     *
     * @return a string representation of this suggestion
     */
    @Override
    public String toString() {
        return String.format("Suggestion{range=%s, text='%s', tooltip='%s'}", range, text, tooltip);
    }

    /**
     * Compares this suggestion to another based on the text in lexicographical order.
     *
     * @param o the suggestion to compare this one with
     * @return a negative integer, zero, or a positive integer if this suggestion's text is
     *         lexicographically less than, equal to, or greater than the specified suggestion's text
     */
    @Override
    public int compareTo(final Suggestion o) {
        return text.compareTo(o.text);
    }

    /**
     * Compares this suggestion to another, ignoring case in the text comparison.
     *
     * @param b the suggestion to compare this one with
     * @return the result of the case-insensitive comparison
     */
    public int compareToIgnoreCase(final Suggestion b) {
        return text.compareToIgnoreCase(b.text);
    }

    /**
     * Expands this suggestion to fit a new command and range, modifying the suggestion's text
     * to account for changes in the input.
     *
     * @param command the original command text
     * @param range the new range to expand to
     * @return a new Suggestion with the expanded range and modified text
     */
    public Suggestion expand(final String command, final StringRange range) {
        if (range.equals(this.range)) {
            return this;
        }
        StringBuilder result = new StringBuilder();
        if (range.start() < this.range.start()) {
            result.append(command.substring(range.start(), this.range.start()));
        }
        result.append(text);
        if (range.end() > this.range.end()) {
            result.append(command.substring(this.range.end(), range.end()));
        }
        return new Suggestion(range, result.toString(), tooltip);
    }
}

