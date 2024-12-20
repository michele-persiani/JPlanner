package io.jplanner;

import java.util.function.Function;


/**
 * Factory for ICostStrategy objects
 */
public class PriorityStrategyFactory
{
    private PriorityStrategyFactory() {}

    /**
     * Breadth first strategy
     * @return
     * @param <S>
     */
    public static <S extends IState> IPriorityStrategy<S> createPlanLengthStrategy()
    {
        return n -> (float) n.depth;
    }

    /**
     * Depth first strategy
     * @return
     * @param <S>
     */
    public static <S extends IState> IPriorityStrategy<S> createInversePlanLengthStrategy()
    {
        return n -> -(float) n.depth;
    }

    /**
     * Dijkstra strategy
     * @return
     * @param <S>
     */
    public static <S extends IState> IPriorityStrategy<S> createPlanCostStrategy()
    {
        return n -> n.totalCost;
    }

    /**
     * A* strategy
     * @param heuristic
     * @return
     * @param <S>
     */
    public static <S extends IState> IPriorityStrategy<S> createAStarStrategy(Function<PlanNode<S>, Float> heuristic)
    {
        return n -> - n.totalCost + heuristic.apply(n);
    }
}
