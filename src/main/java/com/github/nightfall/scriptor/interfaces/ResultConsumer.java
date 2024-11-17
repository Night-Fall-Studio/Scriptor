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

/**
 * A functional interface for consuming the result of a command execution.
 *
 * <p>This interface is used to define a callback that is triggered after a command has been processed,
 * allowing for handling the outcome of the command execution.</p>
 *
 * <p>Implementations of this interface are expected to specify how the command's result should be
 * handled, including whether it was successful and the associated result code.</p>
 *
 * @param <S> the type of the source object, typically representing the originator or user of the command.
 * @author M4ximumpizza
 * @since 1.0
 */
@FunctionalInterface
public interface ResultConsumer<S> {

    /**
     * Called when a command has completed execution, with information about its success and result.
     *
     * <p>This method is typically used for logging, triggering follow-up actions, or notifying other
     * parts of the application about the outcome of a command.</p>
     *
     * @param context the {@link CommandContext} providing information about the command execution
     * @param success {@code true} if the command was successful, {@code false} otherwise
     * @param result the numeric result of the command execution (often indicating the status or effect of the command)
     */
    void onCommandComplete(CommandContext<S> context, boolean success, int result);
}

