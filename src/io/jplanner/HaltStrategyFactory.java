package io.jplanner;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;


/**
 * Factory defining common halt strategies
 */
public class HaltStrategyFactory
{
    private HaltStrategyFactory () {}




    public static <S extends IState> IHaltStrategy<S> createNoHaltStrategy()
    {
        return _ -> false;
    }

    public static <S extends IState> IHaltStrategy<S> createStateHaltStrategy(Predicate<S> predicate)
    {
        return n -> predicate.test(n.state);
    }

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
        return combineStrategies(Arrays.asList(strategies));
    }

    public static <S extends IState> IHaltStrategy<S> combineStrategies(List<IHaltStrategy<S>> strategies)
    {
        return n -> strategies.stream().anyMatch(s -> s.test(n));
    }

}
