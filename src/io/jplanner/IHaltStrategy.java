package io.jplanner;

import java.util.function.Predicate;

/**
 * Strategy defining when the planner should stop computing a branch of the planning tree.
 * For example, deep branches (depth > 100) may be discarded since unlikely to define optimal plans
 * Halt strategies returns true when the planner should halt at the given node (thus not processing its subnodes), false otherwise
 * @param <S> class of the state
 */
public interface IHaltStrategy<S extends IState> extends Predicate<PlanNode<S>>
{
}
