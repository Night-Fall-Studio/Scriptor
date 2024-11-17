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

import java.util.Objects;

/**
 * Represents a parsed argument with an associated range in a string and the parsed result.
 *
 * @param <S> the type of the argument's source, typically the input string or context
 * @param <T> the type of the parsed result
 * @author M4ximumPizza
 * @since 1.0.0
 */
public class ParsedArgument<S, T> {
    private final StringRange range;
    private final T result;

    /**
     * Constructs a new {@code ParsedArgument} with the given range and parsed result.
     *
     * @param start  the start index of the argument in the source string
     * @param end    the end index of the argument in the source string
     * @param result the parsed result of the argument
     */
    public ParsedArgument(final int start, final int end, final T result) {
        this.range = StringRange.between(start, end);
        this.result = result;
    }

    /**
     * Returns the range of the parsed argument in the source string.
     *
     * @return the range of the parsed argument
     */
    public StringRange getRange() {
        return range;
    }

    /**
     * Returns the parsed result of the argument.
     *
     * @return the parsed result
     */
    public T getResult() {
        return result;
    }

    /**
     * Compares this {@code ParsedArgument} to the specified object for equality.
     * Returns {@code true} if the object is a {@code ParsedArgument} with the same range and result.
     *
     * @param o the object to compare
     * @return {@code true} if this object is equal to the specified object
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ParsedArgument)) {
            return false;
        }
        final ParsedArgument<?, ?> that = (ParsedArgument<?, ?>) o;
        return Objects.equals(range, that.range) && Objects.equals(result, that.result);
    }

    /**
     * Returns the hash code value for this {@code ParsedArgument}.
     *
     * @return the hash code value for this object
     */
    @Override
    public int hashCode() {
        return Objects.hash(range, result);
    }
}

