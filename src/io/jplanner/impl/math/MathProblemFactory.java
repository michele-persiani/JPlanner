package io.jplanner.impl.math;

import io.jplanner.*;

public class MathProblemFactory
{


    public static Domain<MathState> newDefaultDomain()
    {
        Domain<MathState> pr = new Domain<>();
        pr.operators.add(newAdd(1));
        pr.operators.add(newAdd(-1));
        pr.operators.add(newMul(2));
        pr.operators.add(newMul(.5f));
        return pr;
    }

    public static Problem<MathState> newDefaultProblem(double goal)
    {
        Problem<MathState> pr = new Problem<>();
        pr.initialState = new MathState();
        pr.goalCondition = s -> s.x == goal;
        return pr;
    }

    public static Planner<MathState> newPlanner()
    {
        return new Planner<>(PriorityStrategyFactory.createPlanLengthStrategy());
    }

    public static IOperator<MathState> newAdd(double value)
    {
        return new IOperator<>()
        {
            @Override
            public boolean canApply(MathState toState)
            {
                return true;
            }

            @Override
            public float getCost()
            {
                return 1;
            }

            @Override
            public void accept(MathState mathState)
            {
                mathState.x += value;
            }

            public String toString()
            {
                return "add " + value;
            }
        };
    }

    public static IOperator<MathState> newMul(double value)
    {
        return new IOperator<>()
        {
            @Override
            public boolean canApply(MathState toState)
            {
                return true;
            }

            @Override
            public float getCost()
            {
                return 1;
            }

            @Override
            public void accept(MathState mathState)
            {
                mathState.x *= value;
            }

            public String toString()
            {
                return "mul " + value;
            }
        };
    }


}
