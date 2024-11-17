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

package com.github.nightfall.scriptor.interfaces;

import com.github.nightfall.scriptor.context.CommandContext;
import com.github.nightfall.scriptor.exceptions.CommandSyntaxException;

import java.util.Collection;

/**
 * A functional interface for modifying or redirecting command execution based on a {@link CommandContext}.
 *
 * <p>This interface is used for altering or redirecting the flow of command execution by applying
 * changes based on the provided command context.</p>
 *
 * <p>Implementations of this interface are expected to define how the command execution should be
 * modified or redirected by processing the given {@link CommandContext} and returning a collection
 * of modified results.</p>
 *
 * @param <S> the type of the source object, which typically represents the originator or user of
 *            the command
 * @author M4ximumpizza
 * @since 1.0
 */
@FunctionalInterface
public interface RedirectModifier<S> {

    /**
     * Applies the modifier to the given {@link CommandContext} and returns a collection of modified results.
     *
     * <p>This method is called during the command processing phase and allows for custom redirection
     * of command execution based on the state of the provided context.</p>
     *
     * @param context the {@link CommandContext} that provides the necessary data to apply the modification
     * @return a collection of modified results after applying the redirection
     * @throws CommandSyntaxException if an error occurs during the redirection process
     */
    Collection<S> apply(CommandContext<S> context) throws CommandSyntaxException;
}

