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

package com.github.nightfall.scriptor.crafter;

import com.github.nightfall.scriptor.args.ArgumentType;
import com.github.nightfall.scriptor.suggestion.SuggestionProvider;
import com.github.nightfall.scriptor.tree.ArgumentCommandNode;
import com.github.nightfall.scriptor.tree.CommandNode;

/**
 * A concrete subclass of {@link ArgumentCrafter} for handling required arguments.
 * This class represents arguments that are mandatory and must be of a specific type.
 *
 * @param <S> the type of the command context
 * @param <T> the type of the argument's value.
 * @author M4ximumpizza
 * @since 1.0
 */
public class RequiredArgumentCrafter<S, T> extends ArgumentCrafter<S, RequiredArgumentCrafter<S, T>> {

    /** The name of the required argument. */
    private final String name;

    /** The type of the argument's value. */
    private final ArgumentType<T> type;

    /** The suggestion provider for the argument (optional). */
    private SuggestionProvider<S> suggestionsProvider = null;

    /**
     * Constructs a new {@code RequiredArgumentCrafter} with the specified name and type.
     *
     * @param name the name of the required argument
     * @param type the type of the argument's value
     */
    private RequiredArgumentCrafter(final String name, final ArgumentType<T> type) {
        this.name = name;
        this.type = type;
    }

    /**
     * Creates a new {@code RequiredArgumentCrafter} for a required argument with the specified name and type.
     *
     * @param <S> the type of the command context
     * @param <T> the type of the argument's value
     * @param name the name of the required argument
     * @param type the type of the argument's value
     * @return a new {@code RequiredArgumentCrafter} instance
     */
    public static <S, T> RequiredArgumentCrafter<S, T> argument(final String name, final ArgumentType<T> type) {
        return new RequiredArgumentCrafter<>(name, type);
    }

    /**
     * Sets the suggestion provider for this argument. This will provide suggestions
     * to the user when they are typing the argument.
     *
     * @param provider the suggestion provider for this argument
     * @return the current {@code RequiredArgumentCrafter} instance, enabling method chaining
     */
    public RequiredArgumentCrafter<S, T> suggests(final SuggestionProvider<S> provider) {
        this.suggestionsProvider = provider;
        return getThis();
    }

    /**
     * Retrieves the suggestion provider for this argument, if any.
     *
     * @return the suggestion provider, or {@code null} if no provider is set
     */
    public SuggestionProvider<S> getSuggestionsProvider() {
        return suggestionsProvider;
    }

    /**
     * Returns the current instance of {@code RequiredArgumentCrafter}, enabling method chaining.
     *
     * @return the current {@code RequiredArgumentCrafter} instance
     */
    @Override
    protected RequiredArgumentCrafter<S, T> getThis() {
        return this;
    }

    /**
     * Retrieves the type of the argument's value.
     *
     * @return the argument type
     */
    public ArgumentType<T> getType() {
        return type;
    }

    /**
     * Retrieves the name of the required argument.
     *
     * @return the name of the argument
     */
    public String getName() {
        return name;
    }

    /**
     * Builds the corresponding {@link ArgumentCommandNode} for this required argument,
     * based on the current argument's properties.
     *
     * @return the constructed {@code ArgumentCommandNode} for this argument
     */
    @Override
    public ArgumentCommandNode<S, T> build() {
        final ArgumentCommandNode<S, T> result = new ArgumentCommandNode<>(
                getName(), getType(), getCommand(), getRequirement(),
                getRedirect(), getRedirectModifier(), isFork(), getSuggestionsProvider()
        );

        // Add all child arguments to the argument command node
        for (final CommandNode<S> argument : getArguments()) {
            result.addChild(argument);
        }

        return result;
    }
}

