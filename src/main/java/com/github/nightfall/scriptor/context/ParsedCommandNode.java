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

package com.github.nightfall.scriptor.context;

import com.github.nightfall.scriptor.tree.CommandNode;

/**
 * Represents a parsed command node with an associated range in a string.
 * It encapsulates a command node and the range where the command node is located in the input string.
 *
 * @param <S> the type of the source for the command node, typically representing the context or input string.
 * @author M4ximumpizza
 * @since 1.0
 */
public record ParsedCommandNode<S>(CommandNode<S> node, StringRange range) {

    /**
     * Constructs a new {@code ParsedCommandNode} with the given command node and range.
     *
     * @param node  the command node associated with this parsed entry
     * @param range the range in the source string where the command node is located
     */
    public ParsedCommandNode {
    }

    /**
     * Returns the command node associated with this parsed command node.
     *
     * @return the command node
     */
    @Override
    public CommandNode<S> node() {
        return node;
    }

    /**
     * Returns the range in the source string where the command node is located.
     *
     * @return the range of the command node
     */
    @Override
    public StringRange range() {
        return range;
    }

    /**
     * Returns a string representation of this {@code ParsedCommandNode}.
     * The string representation includes the command node and its associated range.
     *
     * @return a string representation of the parsed command node
     */
    @Override
    public String toString() {
        return node + "@" + range;
    }

}

