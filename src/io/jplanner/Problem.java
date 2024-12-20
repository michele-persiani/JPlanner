package io.jplanner;

import java.util.function.Predicate;


public class Problem<S extends IState>
{
    public S initialState;

    public Predicate<S> goalCondition;


    public boolean isValid()
    {
        return initialState != null && goalCondition != null;
    }


    public Problem<S> cloneForInitialState(S state)
    {
        Problem<S> other = new Problem<>();
        other.initialState = state;
        other.goalCondition = goalCondition;
        return other;
    }
}