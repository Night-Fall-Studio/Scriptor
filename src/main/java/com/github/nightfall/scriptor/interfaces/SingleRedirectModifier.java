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

/**
 * A functional interface for modifying and redirecting a command's context to a single source.
 *
 * <p>This interface defines a callback that is triggered during the processing of a command,
 * allowing the modification of the command's execution context and returning a new or redirected
 * source object.</p>
 *
 * <p>Implementations of this interface can provide custom logic for altering or redirecting
 * the command context based on its inputs, potentially changing the target or flow of command execution.</p>
 *
 * @param <S> the type of the source object, typically representing the originator or user of the command.
 * @author M4ximumpizza
 * @since 1.0
 */
@FunctionalInterface
public interface SingleRedirectModifier<S> {

    /**
     * Applies the redirect logic to the given command context, returning a new or modified source.
     *
     * <p>This method is typically used for modifying the context of a command, such as redirecting
     * the command execution to a different source based on the provided context.</p>
     *
     * @param context the {@link CommandContext} that provides information about the command execution
     * @return the new or modified source object that will be used for further command processing
     * @throws CommandSyntaxException if there is an error during the redirection process
     */
    S apply(CommandContext<S> context) throws CommandSyntaxException;
}

