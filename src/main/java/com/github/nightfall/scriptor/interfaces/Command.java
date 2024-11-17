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
 * Functional interface representing a command that can be executed in a given context.
 * Implementations of this interface define the logic for a specific command.
 *
 * @param <S> The source type that the command is executed with (e.g., a player, console, etc.)
 * @author M4ximumpizza
 * @since 1.0
 */
@FunctionalInterface
public interface Command<S> {

    /**
     * A constant representing the success status code for a successful command execution.
     * The value 1 indicates that the command was successfully executed.
     */
    int SINGLE_SUCCESS = 1;

    /**
     * Executes the command with the provided context.
     *
     * @param context The context in which the command is being executed, providing necessary information such as the source and arguments
     * @return An integer representing the result of the command execution (typically a success code)
     * @throws CommandSyntaxException If the command has syntax errors or fails to execute properly
     */
    int run(CommandContext<S> context) throws CommandSyntaxException;
}

