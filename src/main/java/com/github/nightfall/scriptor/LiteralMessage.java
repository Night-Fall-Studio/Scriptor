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

package com.github.nightfall.scriptor;

import com.github.nightfall.scriptor.interfaces.Message;

/**
 * Represents a literal message in the form of a plain string.
 *
 * <p>This record implements the {@link Message} interface and provides a simple way to represent a
 * message that consists of a literal string. The string content is immutable, and the class
 * provides basic functionality to retrieve the string or represent it as a string.</p>
 *
 * <p>The record is typically used when you need to encapsulate a message that doesn't require
 * formatting or dynamic content, such as simple static strings.</p>
 *
 * @param string The string content of the message
 * @author M4ximumpizza
 * @since 1.0
 */
public record LiteralMessage(String string) implements Message {

    /**
     * Constructs a new {@link LiteralMessage} with the specified string content.
     *
     * @param string the literal string content of the message
     */
    public LiteralMessage {
    }

    /**
     * Returns the string content of the message.
     *
     * <p>This method allows access to the literal string stored in the message.</p>
     *
     * @return the literal string content of the message
     */
    @Override
    public String string() {
        return string;
    }

    /**
     * Returns a string representation of this message.
     *
     * <p>This method returns the same value as {@link #string ()} and is provided for consistency
     * with the standard Java {@link Object#toString()} method, allowing for easy logging or display of
     * the message content.</p>
     *
     * @return the string content of the message
     */
    @Override
    public String toString() {
        return string;
    }
}
