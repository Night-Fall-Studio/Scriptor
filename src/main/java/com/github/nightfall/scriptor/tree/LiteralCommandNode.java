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

package com.github.nightfall.scriptor.tree;

import com.github.nightfall.scriptor.interfaces.Command;
import com.github.nightfall.scriptor.interfaces.RedirectModifier;
import com.github.nightfall.scriptor.StringReader;
import com.github.nightfall.scriptor.crafter.LiteralArgumentCrafter;
import com.github.nightfall.scriptor.context.CommandContext;
import com.github.nightfall.scriptor.context.CommandContextBuilder;
import com.github.nightfall.scriptor.context.StringRange;
import com.github.nightfall.scriptor.exceptions.CommandSyntaxException;
import com.github.nightfall.scriptor.suggestion.Suggestions;
import com.github.nightfall.scriptor.suggestion.SuggestionsBuilder;

import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

/**
 * Represents a command node that is specifically a literal string, which matches an exact input string.
 *
 * @param <S> The source type that the command is executed with (e.g., a player, console, etc.)
 * @author M4ximumpizza
 * @since 1.0
 */
public class LiteralCommandNode<S> extends CommandNode<S> {
    private final String literal;
    private final String literalLowerCase;
    private final Predicate<S> requirement;

    /**
     * Constructs a new literal command node with the provided properties.
     *
     * @param literal The literal string that this node represents
     * @param command The command to be executed when this node is matched
     * @param requirement A predicate that checks whether the command can be used by the source
     * @param redirect The command node to redirect to, or null if no redirection is needed
     * @param modifier The modifier for the redirection, or null if no modifier is needed
     * @param forks A flag indicating whether this node causes a fork (new execution branch)
     */
    public LiteralCommandNode(final String literal, final Command<S> command, final Predicate<S> requirement,
                              final CommandNode<S> redirect, final RedirectModifier<S> modifier, final boolean forks) {
        super(command, requirement, redirect, modifier, forks);
        this.literal = literal;
        this.literalLowerCase = literal.toLowerCase(Locale.ROOT);
        this.requirement = requirement;
    }

    /**
     * Gets the literal string this node represents.
     *
     * @return The literal string
     */
    public String getLiteral() {
        return literal;
    }

    /**
     * Gets the name of this node, which is the literal string.
     *
     * @return The literal string
     */
    @Override
    public String getName() {
        return literal;
    }

    /**
     * Parses the input string and checks if it matches the literal value.
     *
     * @param reader The reader to parse the input string from
     * @param contextBuilder The context builder to store the parsed result
     * @throws CommandSyntaxException If the input does not match the literal value
     */
    @Override
    public void parse(final StringReader reader, final CommandContextBuilder<S> contextBuilder) throws CommandSyntaxException {
        final int start = reader.getCursor();
        final int end = parse(reader);
        if (end > -1) {
            contextBuilder.withNode(this, StringRange.between(start, end));
            return;
        }

        throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.literalIncorrect().createWithContext(reader, literal);
    }

    /**
     * Tries to parse the input string to match the literal value.
     *
     * @param reader The reader to parse the input string from
     * @return The end index of the match, or -1 if no match
     */
    private int parse(final StringReader reader) {
        final int start = reader.getCursor();
        if (reader.canRead(literal.length())) {
            final int end = start + literal.length();
            if (reader.getString().substring(start, end).equals(literal)) {
                reader.setCursor(end);
                if (!reader.canRead() || reader.peek() == ' ') {
                    return end;
                } else {
                    reader.setCursor(start);
                }
            }
        }
        return -1;
    }

    /**
     * Suggests completions for this literal command node.
     *
     * @param context The command context
     * @param builder The suggestions builder to add suggestions to
     * @return A future containing the suggestions
     */
    @Override
    public CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> context, final SuggestionsBuilder builder) {
        if (literalLowerCase.startsWith(builder.getRemainingLowerCase())) {
            return builder.suggest(literal).buildFuture();
        } else {
            return Suggestions.empty();
        }
    }

    /**
     * Determines if the input string is a valid match for this node's literal.
     *
     * @param input The input string to check
     * @return True if the input is valid for this node, false otherwise
     */
    @Override
    public boolean isValidInput(final String input) {
        return parse(new StringReader(input)) > -1;
    }

    /**
     * Checks if two command nodes are equal. The comparison includes the literal value and the command node's children.
     *
     * @param o The object to compare to
     * @return True if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof LiteralCommandNode)) return false;

        final LiteralCommandNode<S> that = (LiteralCommandNode<S>) o;

        if (!literal.equals(that.literal)) return false;
        return super.equals(o);
    }

    /**
     * Gets the usage text for this node, which is just the literal string.
     *
     * @return The usage text
     */
    @Override
    public String getUsageText() {
        return literal;
    }

    /**
     * Generates a hash code for this node. The hash code includes the literal value.
     *
     * @return The hash code
     */
    @Override
    public int hashCode() {
        int result = literal.hashCode();
        result = 31 * result + super.hashCode();
        return result;
    }

    /**
     * Creates an argument builder for this node.
     *
     * @return A new builder for this literal argument
     */
    @Override
    public LiteralArgumentCrafter<S> createBuilder() {
        final LiteralArgumentCrafter<S> builder = LiteralArgumentCrafter.literal(this.literal);
        builder.requires(getRequirement());
        builder.forward(getRedirect(), getRedirectModifier(), isFork());
        if (getCommand() != null) {
            builder.executes(getCommand());
        }
        return builder;
    }

    /**
     * Gets the requirement predicate for this node.
     *
     * @return The requirement predicate
     */
    public Predicate<S> getRequirement() {
        return requirement;
    }

    /**
     * Gets the sorted key used for comparing nodes. This is simply the literal string.
     *
     * @return The sorted key
     */
    @Override
    protected String getSortedKey() {
        return literal;
    }

    /**
     * Gets a collection of example strings for this command node. For a literal, this is just the literal string.
     *
     * @return A collection of example strings
     */
    @Override
    public Collection<String> getExamples() {
        return Collections.singleton(literal);
    }

    /**
     * Returns a string representation of this literal command node.
     *
     * @return A string representation of the literal command node
     */
    @Override
    public String toString() {
        return "<literal " + literal + ">";
    }
}

