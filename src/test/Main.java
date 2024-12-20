package test;

import io.jplanner.*;
import io.jplanner.exceptions.PlannerException;
import io.jplanner.impl.math.MathProblemFactory;
import io.jplanner.impl.math.MathState;

import java.util.Arrays;
import java.util.List;


public class Main
{
    public static void main(String[] args)
    {
        float goal = 12;

        Domain<MathState> dm = MathProblemFactory.newDefaultDomain();
        Planner<MathState> pl = MathProblemFactory.newPlanner();

        Problem<MathState> pr = MathProblemFactory.newDefaultProblem(goal);

        Plan<MathState> sol;
        try
        {
            sol = pl.findPlan(dm, pr);
        } catch (PlannerException e)
        {
            throw new RuntimeException(e);
        }

        StreamPlanner<MathState> pl2 = new StreamPlanner.Builder<MathState>()
                .setDomain(dm)
                .setProblem(pr)
                .setHaltStrategy(
                        HaltStrategyFactory.combineStrategies(
                                        HaltStrategyFactory.createMaxDepthStrategy(13),
                                        HaltStrategyFactory.createStateHaltStrategy(s -> s.x > 100)
                        )
                )
                .setPriorityStrategy(PriorityStrategyFactory.createPlanLengthStrategy())
                .build();

        try
        {
            List<Plan<MathState>> plans = pl2
                    .stream()
                    .limit(200)
                    .toList();
            System.out.println(plans);
        } catch (PlannerException e)
        {
            throw new RuntimeException(e);
        }

    }
}