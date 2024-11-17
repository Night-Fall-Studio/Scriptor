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

/**
 * Represents a message that can be retrieved as a string.
 *
 * <p>This interface defines the contract for objects that can represent a message. The message is
 * represented as a string, and any class implementing this interface is required to provide
 * the string content of the message.</p>
 *
 * <p>It can be used for various purposes, including logging, displaying user-facing messages,
 * or sending messages between different components of an application.</p>
 *
 * @author M4ximumpizza
 * @since 1.0
 */
public interface Message {

    /**
     * Returns the string content of the message.
     *
     * <p>This method provides the main functionality of the {@link Message} interface by returning
     * the literal string that represents the message's content. The string could be plain text or
     * formatted content, depending on the implementation of the {@link Message} interface.</p>
     *
     * @return the string content of the message
     */
    String string();
}

