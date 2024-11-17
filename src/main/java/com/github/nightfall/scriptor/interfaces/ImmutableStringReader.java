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
 * An interface for a read-only string reader that provides methods for accessing and querying a string's content.
 *
 * <p>This interface defines the essential methods for reading and navigating through a string without modifying it.
 * It provides functionality for accessing the current position within the string, checking the remaining length,
 * and peeking at characters at different offsets.</p>
 *
 * <p>Implementations of this interface should allow for efficient reading operations and support operations
 * to access the string's content in a way that is safe and doesn't alter the underlying data.</p>
 *
 * @author M4ximumpizza
 * @since 1.0
 */
public interface ImmutableStringReader {

    /**
     * Returns the entire string that is being read.
     *
     * @return the string being read
     */
    String getString();

    /**
     * Returns the number of characters remaining to be read in the string.
     *
     * @return the remaining length of the string
     */
    int getRemainingLength();

    /**
     * Returns the total length of the string.
     *
     * @return the total length of the string
     */
    int getTotalLength();

    /**
     * Returns the current cursor position within the string.
     *
     * @return the current position of the cursor
     */
    int getCursor();

    /**
     * Returns the substring of the string that has been read so far, starting from the beginning up to the cursor position.
     *
     * @return the string that has been read
     */
    String getRead();

    /**
     * Returns the substring of the string that has not been read yet, starting from the cursor position to the end.
     *
     * @return the remaining unread string
     */
    String getRemaining();

    /**
     * Checks whether there are enough characters remaining in the string to read the specified number of characters.
     *
     * @param length the number of characters to check for availability
     * @return true if there are enough remaining characters, false otherwise
     */
    boolean canRead(int length);

    /**
     * Checks if there are any characters remaining to be read in the string.
     *
     * @return true if there are remaining characters, false otherwise
     */
    boolean canRead();

    /**
     * Peeks at the next character in the string without advancing the cursor.
     *
     * @return the next character in the string
     */
    char peek();

    /**
     * Peeks at a character at a specified offset from the current cursor position without advancing the cursor.
     *
     * @param offset the offset from the current cursor position
     * @return the character at the specified offset
     */
    char peek(int offset);
}
