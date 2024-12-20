package io.jplanner;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Interface of operators that can be applied to states, thus obtaining resulting states
 * @param <S> class of the state
 */
public interface IOperator<S extends IState> extends Function<S,S>, Consumer<S>
{
    /**
     * Checks whether the operator can be applied to a given state (i.e. preconditions are fulfilled)
     * @param toState state to check
     * @return true if the state fulfills the operator preconditions, false otherwise
     */
    boolean canApply(S toState);

    /**
     * Operator's cost. Must be greater than zero
     * @return operator's cost
     */
    float getCost();

    /**
     * Applies this operator to a state returning a new resulting state.
     * @param state sate to which apply the operator
     * @return a new (cloned) state on which the operator has been applied, or a copy of the input state
     * if the operator couldn't be applied i.e. canApply(s) returned false
     */
    default S apply(S state)
    {
        S newState = (S) state.clone();
        if(!canApply(newState))
            return newState;
        accept(newState);
        return newState;
    }
}
