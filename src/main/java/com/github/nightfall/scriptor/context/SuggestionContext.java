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
 * Represents the context in which a suggestion is made, including the parent command node and the start position.
 * This class is typically used in handling suggestions for a specific command during parsing or execution.
 *
 * @param <S>      the type of the context
 * @param parent   The parent command node of this suggestion context.
 * @param startPos The start position of the suggestion context.
 * @author M4ximumpizza
 * @since 1.0
 */
public record SuggestionContext<S>(CommandNode<S> parent, int startPos) {

    /**
     * Constructs a new {@code SuggestionContext} with the specified parent command node and start position.
     *
     * @param parent   the parent command node of this context
     * @param startPos the start position for this suggestion context
     */
    public SuggestionContext {
    }
}

