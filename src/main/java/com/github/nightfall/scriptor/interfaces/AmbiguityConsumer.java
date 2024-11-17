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

import com.github.nightfall.scriptor.tree.CommandNode;

import java.util.Collection;

/**
 * Functional interface for handling ambiguity during command node resolution.
 * Implementations of this interface define how ambiguity is resolved when a command
 * node has multiple possible children that could match the input.
 *
 * @param <S> The source type that the command is executed with (e.g., a player, console, etc.)
 * @author M4ximumpizza
 * @since 1.0
 */
@FunctionalInterface
public interface AmbiguityConsumer<S> {

    /**
     * Handles the ambiguity when multiple command nodes match the input.
     *
     * @param parent The parent command node that holds the ambiguous child nodes
     * @param child The child command node that was matched
     * @param sibling The sibling command node that is also a candidate match
     * @param inputs The collection of input strings that caused the ambiguity
     */
    void ambiguous(CommandNode<S> parent, CommandNode<S> child, CommandNode<S> sibling, Collection<String> inputs);
}

