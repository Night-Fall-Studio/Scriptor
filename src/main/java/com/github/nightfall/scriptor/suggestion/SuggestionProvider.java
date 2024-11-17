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

import com.github.nightfall.scriptor.context.CommandContext;
import com.github.nightfall.scriptor.exceptions.CommandSyntaxException;

import java.util.concurrent.CompletableFuture;

/**
 * A functional interface representing a provider for suggestions, typically used in
 * command-line suggestion systems.
 *
 * <p>The {@code SuggestionProvider} is responsible for providing suggestions for a given
 * {@code CommandContext}. Implementations should retrieve and return suggestions asynchronously.</p>
 *
 * @param <S> the type of the context for which suggestions are provided.
 * @author M4ximumpizza
 * @since 1.0
 */
@FunctionalInterface
public interface SuggestionProvider<S> {

    /**
     * Asynchronously retrieves suggestions based on the given command context.
     *
     * @param context the command context that provides the environment for the suggestions
     * @param builder the builder used to collect the suggestions
     * @return a {@code CompletableFuture} that will complete with the suggestions
     * @throws CommandSyntaxException if an error occurs while retrieving the suggestions
     */
    CompletableFuture<Suggestions> getSuggestions(final CommandContext<S> context, final SuggestionsBuilder builder) throws CommandSyntaxException;
}

