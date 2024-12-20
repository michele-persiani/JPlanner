package io.jplanner;

public class NoopOperator<S extends IState> implements IOperator<S>
{
    private final float cost;

    public NoopOperator(float cost)
    {
        this.cost = cost;
    }


    @Override
    public boolean canApply(S toState)
    {
        return true;
    }

    @Override
    public float getCost()
    {
        return cost;
    }

    @Override
    public void accept(S s)
    {
    }
}
