package io.jplanner;

public class PlanNode<S extends IState>
{
    public S state;
    public float totalCost;
    public float priority;
    public int depth;
    public long elapsedTimeMillis;
    public IOperator<S> operator;
    public PlanNode<S> parent;
}
