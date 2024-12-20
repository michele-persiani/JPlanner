package io.jplanner;

import java.util.List;

public class Plan<S extends IState> implements IOperator<S>
{

    public final List<IOperator<S>> operators;


    public Plan(List<IOperator<S>> operators)
    {
        this.operators = operators;
    }


    @Override
    public boolean canApply(S toState)
    {
        S state = (S) toState.clone();
        for (IOperator<S> operator : operators)
        {
            if (!operator.canApply(state))
                return false;
            operator.accept(state);
        }
        return true;
    }

    @Override
    public float getCost()
    {
        return operators.stream().map(IOperator::getCost).reduce(0f, Float::sum);
    }

    @Override
    public void accept(S s)
    {
        for (IOperator<S> operator : operators)
            operator.accept(s);
    }
}
