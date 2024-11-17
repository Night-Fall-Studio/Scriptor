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

import com.github.nightfall.scriptor.tree.CommandNode;
import com.github.nightfall.scriptor.tree.LiteralCommandNode;

/**
 * A concrete subclass of {@link ArgumentCrafter} for handling literal arguments.
 * This class represents arguments that are matched exactly by their string value,
 * rather than being processed as dynamic or variable arguments.
 *
 * @param <S> the type of the command context.
 * @author M4ximumpizza
 * @since 1.0
 */
public class LiteralArgumentCrafter<S> extends ArgumentCrafter<S, LiteralArgumentCrafter<S>> {

    /** The literal value of the argument, which must be matched exactly. */
    private final String literal;

    /**
     * Constructs a new {@code LiteralArgumentCrafter} with the specified literal value.
     *
     * @param literal the string literal that will be matched for this argument
     */
    protected LiteralArgumentCrafter(final String literal) {
        this.literal = literal;
    }

    /**
     * Creates a new {@code LiteralArgumentCrafter} with the specified literal name.
     *
     * @param <S> the type of the command context
     * @param name the string literal name to match
     * @return a new {@code LiteralArgumentCrafter} instance
     */
    public static <S> LiteralArgumentCrafter<S> literal(final String name) {
        return new LiteralArgumentCrafter<>(name);
    }

    /**
     * Returns the current instance of {@code LiteralArgumentCrafter}, enabling method chaining.
     *
     * @return the current {@code LiteralArgumentCrafter} instance
     */
    @Override
    protected LiteralArgumentCrafter<S> getThis() {
        return this;
    }

    /**
     * Retrieves the literal value of the argument.
     *
     * @return the string literal value for this argument
     */
    public String getLiteral() {
        return literal;
    }

    /**
     * Builds the corresponding {@link LiteralCommandNode} based on the current argument's properties.
     * This method constructs a command node that is used to match and execute the literal argument.
     *
     * @return the constructed {@code LiteralCommandNode} for this argument
     */
    @Override
    public LiteralCommandNode<S> build() {
        final LiteralCommandNode<S> result = new LiteralCommandNode<>(getLiteral(), getCommand(), getRequirement(), getRedirect(), getRedirectModifier(), isFork());

        // Add all child arguments to the literal command node
        for (final CommandNode<S> argument : getArguments()) {
            result.addChild(argument);
        }

        return result;
    }
}

