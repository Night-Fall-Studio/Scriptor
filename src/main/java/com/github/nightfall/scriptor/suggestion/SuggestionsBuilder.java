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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

/**
 * A builder class for constructing {@link Suggestions} objects.
 *
 * <p>This class allows you to collect a series of suggestions based on input and manage their
 * state (e.g., remaining input, start position, and suggestions list) while constructing the final
 * {@code Suggestions} object.</p>
 *
 * @author M4ximumpizza
 * @since 1.0
 */
public class SuggestionsBuilder {
    private final String input;
    private final String inputLowerCase;
    private final int start;
    private final String remaining;
    private final String remainingLowerCase;
    private final List<Suggestion> result;

    /**
     * Constructs a {@code SuggestionsBuilder} with the provided input and start position.
     *
     * @param input the original input string
     * @param inputLowerCase the lowercase version of the input string
     * @param start the position where suggestions should start
     */
    public SuggestionsBuilder(final String input, final String inputLowerCase, final int start) {
        this.input = input;
        this.inputLowerCase = inputLowerCase;
        this.start = start;
        this.remaining = input.substring(start);
        this.remainingLowerCase = inputLowerCase.substring(start);
        this.result = new ArrayList<>();
    }

    /**
     * Constructs a {@code SuggestionsBuilder} with the provided input string and start position.
     * The input string is converted to lowercase for convenience.
     *
     * @param input the original input string
     * @param start the position where suggestions should start
     */
    public SuggestionsBuilder(final String input, final int start) {
        this(input, input.toLowerCase(Locale.ROOT), start);
    }

    /**
     * Gets the original input string.
     *
     * @return the input string
     */
    public String getInput() {
        return input;
    }

    /**
     * Gets the start position of the suggestion.
     *
     * @return the start position
     */
    public int getStart() {
        return start;
    }

    /**
     * Gets the remaining portion of the input starting from the start position.
     *
     * @return the remaining input string
     */
    public String getRemaining() {
        return remaining;
    }

    /**
     * Gets the remaining portion of the input in lowercase.
     *
     * @return the remaining input string in lowercase
     */
    public String getRemainingLowerCase() {
        return remainingLowerCase;
    }

    /**
     * Builds and returns a {@code Suggestions} object containing the collected suggestions.
     *
     * @return a {@code Suggestions} object
     */
    public Suggestions build() {
        return Suggestions.create(input, result);
    }

    /**
     * Builds and returns a {@code CompletableFuture} that will complete with the {@code Suggestions}
     * object containing the collected suggestions.
     *
     * @return a {@code CompletableFuture} containing the {@code Suggestions}
     */
    public CompletableFuture<Suggestions> buildFuture() {
        return CompletableFuture.completedFuture(build());
    }

    /**
     * Adds a suggestion to the builder with the specified text.
     *
     * @param text the suggestion text
     * @return the current {@code SuggestionsBuilder} instance
     */
    public SuggestionsBuilder suggest(final String text) {
        if (!text.equals(remaining)) {
            result.add(new Suggestion(StringRange.between(start, input.length()), text));
        }
        return this;
    }

    /**
     * Adds a suggestion to the builder with the specified text and a tooltip.
     *
     * @param text the suggestion text
     * @param tooltip the tooltip message for the suggestion
     * @return the current {@code SuggestionsBuilder} instance
     */
    public SuggestionsBuilder suggest(final String text, final Message tooltip) {
        if (!text.equals(remaining)) {
            result.add(new Suggestion(StringRange.between(start, input.length()), text, tooltip));
        }
        return this;
    }

    /**
     * Adds an integer suggestion to the builder.
     *
     * @param value the integer suggestion value
     * @return the current {@code SuggestionsBuilder} instance
     */
    public SuggestionsBuilder suggest(final int value) {
        result.add(new IntSuggestion(StringRange.between(start, input.length()), value));
        return this;
    }

    /**
     * Adds an integer suggestion to the builder with a tooltip.
     *
     * @param value the integer suggestion value
     * @param tooltip the tooltip message for the suggestion
     * @return the current {@code SuggestionsBuilder} instance
     */
    public SuggestionsBuilder suggest(final int value, final Message tooltip) {
        result.add(new IntSuggestion(StringRange.between(start, input.length()), value, tooltip));
        return this;
    }

    /**
     * Merges the suggestions from another {@code SuggestionsBuilder} into this one.
     *
     * @param other the other {@code SuggestionsBuilder} to merge
     * @return the current {@code SuggestionsBuilder} instance
     */
    public SuggestionsBuilder add(final SuggestionsBuilder other) {
        result.addAll(other.result);
        return this;
    }

    /**
     * Creates a new {@code SuggestionsBuilder} starting at a new position in the input string.
     *
     * @param start the new start position
     * @return a new {@code SuggestionsBuilder} instance
     */
    public SuggestionsBuilder createOffset(final int start) {
        return new SuggestionsBuilder(input, inputLowerCase, start);
    }

    /**
     * Restarts the builder by creating a new instance with the same start position.
     *
     * @return a new {@code SuggestionsBuilder} instance with the same start position
     */
    public SuggestionsBuilder restart() {
        return createOffset(start);
    }
}

