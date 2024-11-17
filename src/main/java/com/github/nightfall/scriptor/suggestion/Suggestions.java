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

import com.github.nightfall.scriptor.context.StringRange;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * Represents a collection of suggestions, typically used in command-line suggestion systems.
 *
 * <p>This class provides functionality to store suggestions, check if they are empty, merge multiple
 * suggestion sets, and create new suggestion sets based on a command and range of text.</p>
 *
 * @author M4ximumpizza
 * @since 1.0
 */
public class Suggestions {
    private static final Suggestions EMPTY = new Suggestions(StringRange.at(0), Collections.emptyList());

    private final StringRange range;
    private final List<Suggestion> suggestions;

    /**
     * Constructs a {@code Suggestions} object with the specified range and list of suggestions.
     *
     * @param range the range of text the suggestions apply to
     * @param suggestions the list of suggestions
     */
    public Suggestions(final StringRange range, final List<Suggestion> suggestions) {
        this.range = range;
        this.suggestions = suggestions;
    }

    /**
     * Gets the range of text that the suggestions apply to.
     *
     * @return the {@code StringRange} representing the range
     */
    public StringRange getRange() {
        return range;
    }

    /**
     * Gets the list of suggestions.
     *
     * @return the list of {@code Suggestion} objects
     */
    public List<Suggestion> getList() {
        return suggestions;
    }

    /**
     * Checks if the suggestion list is empty.
     *
     * @return {@code true} if there are no suggestions, {@code false} otherwise
     */
    public boolean isEmpty() {
        return suggestions.isEmpty();
    }

    /**
     * Returns a completed {@code CompletableFuture} representing an empty set of suggestions.
     *
     * @return a {@code CompletableFuture} that completes with the empty suggestions
     */
    public static CompletableFuture<Suggestions> empty() {
        return CompletableFuture.completedFuture(EMPTY);
    }

    /**
     * Merges multiple {@code Suggestions} objects into a single one.
     * If the input collection is empty, an empty {@code Suggestions} object is returned.
     *
     * @param command the command text (used for expanding suggestions)
     * @param input the collection of {@code Suggestions} to merge
     * @return a merged {@code Suggestions} object
     */
    public static Suggestions merge(final String command, final Collection<Suggestions> input) {
        if (input.isEmpty()) {
            return EMPTY;
        }

        // Collect all suggestions without duplicates
        Set<Suggestion> mergedSuggestions = new HashSet<>();
        for (Suggestions suggestionSet : input) {
            mergedSuggestions.addAll(suggestionSet.getList());
        }

        // Create and return the merged suggestions
        return create(command, mergedSuggestions);
    }

    /**
     * Creates a {@code Suggestions} object from a collection of suggestions, expanding their ranges
     * based on the provided command text and merging them into a sorted list.
     *
     * @param command the command text (used for expanding suggestions)
     * @param suggestions the collection of {@code Suggestion} objects to include
     * @return a new {@code Suggestions} object
     */
    public static Suggestions create(final String command, final Collection<Suggestion> suggestions) {
        if (suggestions.isEmpty()) {
            return EMPTY;
        }

        // Find the minimum and maximum range
        int start = Integer.MAX_VALUE;
        int end = Integer.MIN_VALUE;

        for (Suggestion suggestion : suggestions) {
            StringRange suggestionRange = suggestion.getRange();
            start = Math.min(start, suggestionRange.start());
            end = Math.max(end, suggestionRange.end());
        }

        StringRange range = new StringRange(start, end);

        // Expand and collect suggestions
        List<Suggestion> expandedSuggestions = new ArrayList<>(suggestions.size());
        for (Suggestion suggestion : suggestions) {
            expandedSuggestions.add(suggestion.expand(command, range));
        }

        // Sort the suggestions ignoring case
        expandedSuggestions.sort(Comparator.comparing(Suggestion::getText, String.CASE_INSENSITIVE_ORDER));

        return new Suggestions(range, expandedSuggestions);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Suggestions)) {
            return false;
        }
        Suggestions that = (Suggestions) o;
        return Objects.equals(range, that.range) && Objects.equals(suggestions, that.suggestions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(range, suggestions);
    }

    @Override
    public String toString() {
        return "Suggestions{" +
                "range=" + range +
                ", suggestions=" + suggestions +
                '}';
    }
}

