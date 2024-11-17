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
import com.github.nightfall.scriptor.suggestion.Suggestions;
import com.github.nightfall.scriptor.suggestion.SuggestionsBuilder;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

/**
 * Represents a type of argument that can be parsed from a {@link StringReader} and used in a command context.
 * This interface defines methods for parsing an argument, suggesting completions, and providing examples.
 *
 * @param <T> The type of the argument value.
 * @author M4ximumpizza
 * @since 1.0
 */
public interface ArgumentType<T> {

    /**
     * Parses the argument from the provided {@link StringReader}.
     *
     * @param reader The reader that provides the input to parse.
     * @return The parsed argument of type T.
     * @throws CommandSyntaxException If the input cannot be parsed correctly.
     */
    T parse(StringReader reader) throws CommandSyntaxException;

    /**
     * Parses the argument from the provided {@link StringReader} with an additional source object.
     * This is a default implementation that delegates to the primary {@link #parse(StringReader)} method.
     *
     * @param reader The reader that provides the input to parse.
     * @param source The source object that may be used in some custom parsing logic.
     * @param <S> The type of the source object.
     * @return The parsed argument of type T.
     * @throws CommandSyntaxException If the input cannot be parsed correctly.
     */
    default <S> T parse(final StringReader reader, final S source) throws CommandSyntaxException {
        return parse(reader);
    }

    /**
     * Lists possible suggestions for the argument in a given command context.
     * This is a default implementation that returns an empty suggestion list.
     *
     * @param context The command context containing the source and arguments.
     * @param builder The builder used to accumulate suggestions.
     * @param <S> The type of the command context's source object.
     * @return A {@link CompletableFuture} representing the list of suggestions.
     */
    default <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> context, final SuggestionsBuilder builder) {
        return Suggestions.empty();
    }

    /**
     * Provides a collection of example values for this argument type.
     * This is a default implementation that returns an empty list of examples.
     *
     * @return A collection of example values for this argument type.
     */
    default Collection<String> getExamples() {
        return Collections.emptyList();
    }
}

