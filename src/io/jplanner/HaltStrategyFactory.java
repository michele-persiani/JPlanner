package io.jplanner;

import java.util.Arrays;

public class HaltStrategyFactory
{
    private HaltStrategyFactory () {}

    public static <S extends IState> IHaltStrategy<S> createMaxDepthStrategy(int maxDept)
    {
        return n -> n.depth > maxDept;
    }

    public static <S extends IState> IHaltStrategy<S> createMaxCostStrategy(float maxCost)
    {
        return n -> n.totalCost > maxCost;
    }

    public static <S extends IState> IHaltStrategy<S> createMaxElapsedTimeStrategy(long elapsedTimeMillis)
    {
        return n -> n.elapsedTimeMillis > elapsedTimeMillis;
    }

    public static <S extends IState> IHaltStrategy<S> combineStrategies(IHaltStrategy<S> ... strategies)
    {
        return n -> Arrays.stream(strategies).anyMatch(s -> s.test(n));
    }

}
