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

import com.github.nightfall.scriptor.interfaces.ImmutableStringReader;

/**
 * Represents a range within a string, defined by a starting and ending index.
 * It provides methods to manipulate and retrieve substrings within the range.
 * @author M4ximumpizza
 * @since 1.0
 */
public record StringRange(int start, int end) {
    /**
     * Constructs a new {@code StringRange} with the given start and end indices.
     *
     * @param start the start index of the range
     * @param end   the end index of the range
     */
    public StringRange {
    }

    /**
     * Creates a {@code StringRange} where the start and end indices are the same, representing a single position.
     *
     * @param pos the position to represent as both the start and end of the range
     * @return a new {@code StringRange} with the given position as both start and end
     */
    public static StringRange at(final int pos) {
        return new StringRange(pos, pos);
    }

    /**
     * Creates a {@code StringRange} between the given start and end indices.
     *
     * @param start the start index of the range
     * @param end   the end index of the range
     * @return a new {@code StringRange} representing the range between start and end
     */
    public static StringRange between(final int start, final int end) {
        return new StringRange(start, end);
    }

    /**
     * Creates a {@code StringRange} that encompasses the union of two other ranges.
     * The resulting range will start at the minimum of the two start indices and end at the maximum of the two end indices.
     *
     * @param a the first {@code StringRange}
     * @param b the second {@code StringRange}
     * @return a new {@code StringRange} that encompasses both {@code StringRange}s
     */
    public static StringRange encompassing(final StringRange a, final StringRange b) {
        return new StringRange(Math.min(a.start(), b.start()), Math.max(a.end(), b.end()));
    }

    /**
     * Returns the start index of the range.
     *
     * @return the start index
     */
    @Override
    public int start() {
        return start;
    }

    /**
     * Returns the end index of the range.
     *
     * @return the end index
     */
    @Override
    public int end() {
        return end;
    }

    /**
     * Returns the substring of the provided {@code ImmutableStringReader} within this range.
     *
     * @param reader the string reader to extract the substring from
     * @return the substring within this range
     */
    public String get(final ImmutableStringReader reader) {
        return reader.getString().substring(start, end);
    }

    /**
     * Returns the substring of the provided string within this range.
     *
     * @param string the string to extract the substring from
     * @return the substring within this range
     */
    public String get(final String string) {
        return string.substring(start, end);
    }

    /**
     * Checks if the range is empty (i.e., the start and end indices are the same).
     *
     * @return {@code true} if the range is empty, {@code false} otherwise
     */
    public boolean isEmpty() {
        return start == end;
    }

    /**
     * Returns the length of the range (end index minus start index).
     *
     * @return the length of the range
     */
    public int getLength() {
        return end - start;
    }

    /**
     * Compares this {@code StringRange} to the specified object for equality.
     * Returns {@code true} if the object is a {@code StringRange} with the same start and end indices.
     *
     * @param o the object to compare
     * @return {@code true} if this object is equal to the specified object
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StringRange)) {
            return false;
        }
        final StringRange that = (StringRange) o;
        return start == that.start && end == that.end;
    }

    /**
     * Returns a string representation of this {@code StringRange}.
     *
     * @return a string representation of the range
     */
    @Override
    public String toString() {
        return "StringRange{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }
}

