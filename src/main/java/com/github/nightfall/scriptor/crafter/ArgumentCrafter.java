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

package com.github.nightfall.scriptor.crafter;

import com.github.nightfall.scriptor.interfaces.Command;
import com.github.nightfall.scriptor.interfaces.RedirectModifier;
import com.github.nightfall.scriptor.interfaces.SingleRedirectModifier;
import com.github.nightfall.scriptor.tree.CommandNode;
import com.github.nightfall.scriptor.tree.RootCommandNode;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * An abstract base class for crafting and managing command arguments and their behavior.
 * This class allows chaining command nodes, defining their execution requirements, and setting redirection
 * or fork behavior for argument handling.
 *
 * @param <S> the type of the command context
 * @param <T> the concrete type of the subclass, enabling fluent chaining of methods.
 * @author M4ximumpizza
 * @since 1.0
 */
public abstract class ArgumentCrafter<S, T extends ArgumentCrafter<S, T>> {

    /** The root node that contains the arguments in the command structure. */
    private final RootCommandNode<S> arguments = new RootCommandNode<>();

    /**
     * Creates a new {@code ArgumentCrafter} instance.
     *
     * @since 1.3
     */
    @SuppressWarnings("unchecked")
    public static <S> Predicate<S> requireNothing() {
        return (Predicate<S>) RequireNothing.INSTANCE;
    }
    /** The command to execute for this argument. */
    private Command<S> command;

    /** A predicate to check the requirement for executing the argument. */
    private Predicate<S> requirement = s -> true;

    /** The target node for redirection or forking. */
    private CommandNode<S> target;

    /** The modifier applied when redirecting the argument. */
    private RedirectModifier<S> modifier = null;

    /** Inicates whether the argument is forked. */
    private boolean forks;

    /**
     * Returns the concrete subclass of this {@code ArgumentCrafter} to support fluent method chaining.
     *
     * @return the concrete subclass instance
     */
    protected abstract T getThis();

    /**
     * Adds the given {@link ArgumentCrafter} as a child of the current argument.
     * This allows chaining arguments together to form more complex argument structures.
     *
     * @param argument the argument to add as a child
     * @return the current {@code ArgumentCrafter} instance for method chaining
     * @throws IllegalStateException if a redirection has already been set, preventing further children from being added
     */
    public T then(final ArgumentCrafter<S, ?> argument) {
        if (target != null) {
            throw new IllegalStateException("Cannot add children to a redirected node");
        }
        arguments.addChild(argument.build());
        return getThis();
    }

    /**
     * Adds the given {@link CommandNode} as a child of the current argument.
     *
     * @param argument the command node to add as a child
     * @return the current {@code ArgumentCrafter} instance for method chaining
     * @throws IllegalStateException if a redirection has already been set, preventing further children from being added
     */
    public T then(final CommandNode<S> argument) {
        if (target != null) {
            throw new IllegalStateException("Cannot add children to a redirected node");
        }
        arguments.addChild(argument);
        return getThis();
    }

    /**
     * Retrieves the collection of arguments (command nodes) associated with this argument crafter.
     *
     * @return a collection of child command nodes
     */
    public Collection<CommandNode<S>> getArguments() {
        return arguments.getChildren();
    }

    /**
     * Sets the command to execute when the argument is parsed and matched.
     *
     * @param command the command to execute
     * @return the current {@code ArgumentCrafter} instance for method chaining
     */
    public T executes(final Command<S> command) {
        this.command = command;
        return getThis();
    }

    /**
     * Retrieves the command associated with this argument.
     *
     * @return the command to execute
     */
    public Command<S> getCommand() {
        return command;
    }

    /**
     * Sets a requirement predicate that must be satisfied for the argument to be executed.
     *
     * @param requirement the predicate representing the requirement
     * @return the current {@code ArgumentCrafter} instance for method chaining
     */
    public T requires(final Predicate<S> requirement) {
        this.requirement = Objects.requireNonNull(requirement, "requirement");
        return getThis();
    }

    /**
     * A predicate that requires nothing to be satisfied.
     *
     * @param <S> the type of the command context
     * @return a predicate that always returns {@code true}
     * @since 1.3
     */
    private static final class RequireNothing implements Predicate<Object> {
        private static final RequireNothing INSTANCE = new RequireNothing();

        @Override
        public boolean test(Object o) {
            return true;
        }
    }

    /**
     * Retrieves the requirement predicate for this argument.
     *
     * @return the requirement predicate
     */
    public Predicate<S> getRequirement() {
        return requirement;
    }

    /**
     * Redirects this argument to a different command node, preventing further argument additions.
     *
     * @param target the target node to redirect to
     * @return the current {@code ArgumentCrafter} instance for method chaining
     */
    public T redirect(final CommandNode<S> target) {
        return forward(target, null, false);
    }

    /**
     * Redirects this argument to a different command node with a specific modifier, preventing further argument additions.
     *
     * @param target the target node to redirect to
     * @param modifier the modifier to apply during redirection
     * @return the current {@code ArgumentCrafter} instance for method chaining
     */
    public T redirect(final CommandNode<S> target, final SingleRedirectModifier<S> modifier) {
        return forward(target, modifier == null ? null : o -> Collections.singleton(modifier.apply(o)), false);
    }

    /**
     * Forks this argument to a different command node, with an optional modifier, allowing independent processing of arguments.
     *
     * @param target the target node to fork to
     * @param modifier the modifier to apply during forking
     * @return the current {@code ArgumentCrafter} instance for method chaining
     */
    public T fork(final CommandNode<S> target, final RedirectModifier<S> modifier) {
        return forward(target, modifier, true);
    }

    /**
     * Forwards the current argument to a specified target node, with an optional modifier and fork behavior.
     *
     * @param target the target command node to forward to
     * @param modifier the modifier to apply during redirection or forking
     * @param fork whether to fork the argument processing
     * @return the current {@code ArgumentCrafter} instance for method chaining
     * @throws IllegalStateException if the argument has children when forwarding
     */
    public T forward(final CommandNode<S> target, final RedirectModifier<S> modifier, final boolean fork) {
        if (!arguments.getChildren().isEmpty()) {
            throw new IllegalStateException("Cannot forward a node with children");
        }
        this.target = target;
        this.modifier = modifier;
        this.forks = fork;
        return getThis();
    }

    /**
     * Retrieves the target command node to which the argument is redirected or forked.
     *
     * @return the target command node, or {@code null} if no redirection or forking has been set
     */
    public CommandNode<S> getRedirect() {
        return target;
    }

    /**
     * Retrieves the redirect modifier applied during redirection or forking.
     *
     * @return the redirect modifier, or {@code null} if no modifier is applied
     */
    public RedirectModifier<S> getRedirectModifier() {
        return modifier;
    }

    /**
     * Checks if this argument is forked, meaning it will be processed independently.
     *
     * @return {@code true} if the argument is forked, {@code false} otherwise
     */
    public boolean isFork() {
        return forks;
    }

    /**
     * Builds the command node structure for this argument crafter.
     * This is an abstract method that must be implemented by subclasses to construct the final command node.
     *
     * @return the built command node
     */
    public abstract CommandNode<S> build();
}
