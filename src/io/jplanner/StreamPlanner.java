package io.jplanner;

import io.jplanner.exceptions.InvalidDomainException;
import io.jplanner.exceptions.InvalidProblemException;
import io.jplanner.exceptions.PlannerException;

import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;


/**
 * Planner leveraging the stream api to compute multiple plans
 * @param <S>
 */
public class StreamPlanner<S extends IState>
{

    public static class Builder<S extends IState> extends io.jplanner.util.Builder<StreamPlanner<S>>
    {
        public Builder<S> setPriorityStrategy(IPriorityStrategy<S> priorityStrategy)
        {
            return setValue(this, p -> p.priorityStrategy = priorityStrategy);
        }

        public Builder<S> setHaltStrategy(IHaltStrategy<S> haltStrategy)
        {
            return setValue(this, p -> p.haltStrategy = haltStrategy);
        }

        public Builder<S> setDomain(Domain<S> domain)
        {
            return setValue(this, p -> p.domain = domain);
        }

        public Builder<S> setProblem(Problem<S> problem)
        {
            return setValue(this, p -> p.problem = problem);
        }

        @Override
        protected StreamPlanner<S> newInstance()
        {
            return new StreamPlanner<>();
        }
    }


    private Domain<S> domain = new Domain<>();


    private Problem<S> problem = new Problem<>();


    private IPriorityStrategy<S> priorityStrategy = PriorityStrategyFactory.createPlanLengthStrategy();


    private IHaltStrategy<S> haltStrategy = HaltStrategyFactory.createNoHaltStrategy();


    private final PriorityQueue<PlanNode<S>> nodeQueue = new PriorityQueue<>((o1, o2) -> (int) Math.signum(o1.priority - o2.priority));

    
    private long startTime = 0L;


    public void initialize() throws PlannerException
    {
        nodeQueue.clear();

        if(!domain.isValid())
            throw new InvalidDomainException();
        if(!problem.isValid())
            throw new InvalidProblemException();

        PlanNode<S> init = new PlanNode<>();
        init.totalCost = 0;
        init.priority = 0;
        init.depth = 0;
        init.parent = null;
        init.operator = null;
        init.elapsedTimeMillis = 0L;
        init.state = (S) problem.initialState.clone();

        nodeQueue.add(init);

        startTime = System.currentTimeMillis();
    }


    private Iterator<Plan<S>> createPlanIterator()
    {
        return new Iterator<Plan<S>>()
        {
            @Override
            public boolean hasNext()
            {
                return !nodeQueue.isEmpty();
            }


            @Override
            public Plan<S> next()
            {
                while(!nodeQueue.isEmpty())
                {
                    PlanNode<S> parentNode = nodeQueue.poll();


                    if(problem.goalCondition.test(parentNode.state))
                        return getPlan(parentNode);

                    if(haltStrategy.test(parentNode))
                        continue;

                    long elapsedTime = System.currentTimeMillis() - startTime;

                    domain.operators
                            .stream()
                            .filter(op -> op.canApply(parentNode.state))
                            .map(op -> {
                                S clonedState = (S) parentNode.state.clone();
                                op.accept(clonedState);
                                PlanNode<S> newNode = new PlanNode<>();
                                newNode.state = clonedState;
                                newNode.operator = op;
                                newNode.parent = parentNode;
                                newNode.depth = parentNode.depth + 1;
                                newNode.elapsedTimeMillis = elapsedTime;
                                newNode.totalCost = parentNode.totalCost + op.getCost();
                                newNode.priority = priorityStrategy.apply(newNode);
                                return newNode;
                            })
                            .forEach(nodeQueue::add);

                }
                return null;
            }
        };
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

    /**
     * Streams all found plans. Use limit() to stop after a given number of plans is found
     * @return
     * @throws PlannerException
     */
    public Stream<Plan<S>> stream() throws PlannerException
    {
        initialize();
        Iterator<Plan<S>> planIterator = createPlanIterator();
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(planIterator, Spliterator.ORDERED), false)
                .filter(Objects::nonNull);
    }
}
