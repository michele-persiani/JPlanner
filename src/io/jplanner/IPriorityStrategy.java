package io.jplanner;

import java.util.function.Function;

/**
 * Strategy to compute the priority of a plan node.
 * For example, could be defined in terms of depth of the node, or parent or operator costs
 * @param <S> class of the state
 */
public interface IPriorityStrategy<S extends IState> extends Function<PlanNode<S>, Float>
{
}
