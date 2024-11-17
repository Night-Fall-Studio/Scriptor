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

package com.github.nightfall.scriptor.args;

import com.github.nightfall.scriptor.StringReader;
import com.github.nightfall.scriptor.context.CommandContext;
import com.github.nightfall.scriptor.exceptions.CommandSyntaxException;
import com.github.nightfall.scriptor.suggestion.Suggestions;
import com.github.nightfall.scriptor.suggestion.SuggestionsBuilder;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

/**
 * Represents a boolean argument type that can be parsed from input and used in a command context.
 * This class provides methods for parsing a boolean value, suggesting completions, and providing examples.
 *
 * @author M4ximumpizza
 * @since 1.0
 */
public class BoolArgumentType implements ArgumentType<Boolean> {

    private static final Collection<String> EXAMPLES = Arrays.asList("true", "false");

    // Private constructor to prevent direct instantiation
    private BoolArgumentType() {
    }

    /**
     * Creates a new instance of {@link BoolArgumentType}.
     *
     * @return A new instance of {@link BoolArgumentType}.
     */
    public static BoolArgumentType bool() {
        return new BoolArgumentType();
    }

    /**
     * Retrieves the boolean argument value from the command context.
     *
     * @param context The command context containing the parsed arguments.
     * @param name The name of the argument to retrieve.
     * @return The boolean value associated with the specified argument name.
     */
    public static boolean getBool(final CommandContext<?> context, final String name) {
        return context.getArgument(name, Boolean.class);
    }

    /**
     * Parses the boolean argument from the provided {@link StringReader}.
     *
     * @param reader The reader that provides the input to parse.
     * @return The parsed boolean value.
     * @throws CommandSyntaxException If the input cannot be parsed as a boolean.
     */
    @Override
    public Boolean parse(final StringReader reader) throws CommandSyntaxException {
        return reader.readBoolean();
    }

    /**
     * Provides suggestions for the boolean argument based on the current input.
     *
     * @param context The command context containing the source and arguments.
     * @param builder The builder used to accumulate suggestions.
     * @param <S> The type of the command context's source object.
     * @return A {@link CompletableFuture} representing the list of suggestions.
     */
    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> context, final SuggestionsBuilder builder) {
        if ("true".startsWith(builder.getRemainingLowerCase())) {
            builder.suggest("true");
        }
        if ("false".startsWith(builder.getRemainingLowerCase())) {
            builder.suggest("false");
        }
        return builder.buildFuture();
    }

    /**
     * Provides a collection of example values for the boolean argument.
     *
     * @return A collection of example values for this argument type.
     */
    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}

