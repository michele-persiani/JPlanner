package io.jplanner;

import io.jplanner.exceptions.InvalidDomainException;
import io.jplanner.exceptions.InvalidProblemException;
import io.jplanner.exceptions.NoPlanFoundException;
import io.jplanner.exceptions.PlannerException;

import java.util.*;


public class Planner<S extends IState>
{
    private final IPriorityStrategy<S> priorityStrategy;

    private final IHaltStrategy<S> haltStrategy;

    public Planner(IPriorityStrategy<S> priorityStrategy, IHaltStrategy<S> haltStrategy)
    {
        this.priorityStrategy = priorityStrategy;
        this.haltStrategy = haltStrategy;

    }

    public Planner(IPriorityStrategy<S> costStrategy)
    {
        this(costStrategy, _ -> false);
    }

    /**
     * Finds optimal plan for given domain and problem
     * @param domain
     * @param problem
     * @return
     * @throws PlannerException if either domain or problem are invalid or a plan couldn't be found
     */
    public Plan<S> findPlan(Domain<S> domain, Problem<S> problem) throws PlannerException
    {
        if(!domain.isValid())
            throw new InvalidDomainException();
        if(!problem.isValid())
            throw new InvalidProblemException();

        PriorityQueue<PlanNode<S>> nodeQueue = new PriorityQueue<>((o1, o2) -> (int) Math.signum(o1.priority - o2.priority));

        HashSet<S> visitedStates = new HashSet<>();


        PlanNode<S> init = new PlanNode<>();
        init.totalCost = 0;
        init.priority = 0;
        init.depth = 0;
        init.parent = null;
        init.operator = null;
        init.elapsedTimeMillis = 0L;
        init.state = (S) problem.initialState.clone();

        nodeQueue.add(init);

        long startTime = System.currentTimeMillis();

        while(!nodeQueue.isEmpty())
        {
            PlanNode<S> node = nodeQueue.poll();

            if(visitedStates.contains(node.state) || haltStrategy.test(node))
                continue;

            visitedStates.add(node.state);

            if(problem.goalCondition.test(node.state))
                return getPlan(node);

            long elapsedTime = System.currentTimeMillis() - startTime;

            Collection<PlanNode<S>> newNodes = domain.operators
                    .stream()
                    .filter(op -> op.canApply(node.state))
                    .map(op -> {
                        S clonedState = (S) node.state.clone();
                        op.accept(clonedState);
                        PlanNode<S> newNode = new PlanNode<>();
                        newNode.state = clonedState;
                        newNode.operator = op;
                        newNode.parent = node;
                        newNode.depth = node.depth + 1;
                        newNode.elapsedTimeMillis = elapsedTime;
                        newNode.totalCost = node.totalCost + op.getCost();
                        newNode.priority = priorityStrategy.apply(newNode);
                        return newNode;
                    })
                    .toList();

            nodeQueue.addAll(newNodes);
        }
        throw new NoPlanFoundException();
    }

    private Plan<S> getPlan(PlanNode<S> forNode)
    {
        List<IOperator<S>> operators = new ArrayList<>();

        while(forNode.parent != null)
        {
            operators.add(forNode.operator);
            forNode = forNode.parent;
        }
        operators = operators.reversed();

        if(operators.isEmpty())
            operators.add(new NoopOperator<>(0));

        return new Plan<>(operators);
    }
}
