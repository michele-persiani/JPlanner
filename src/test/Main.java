package test;

import io.jplanner.Domain;
import io.jplanner.Plan;
import io.jplanner.Planner;
import io.jplanner.Problem;
import io.jplanner.exceptions.PlannerException;
import io.jplanner.impl.math.MathProblemFactory;
import io.jplanner.impl.math.MathState;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args)
    {
        Domain<MathState> dm = MathProblemFactory.newDefaultDomain();
        Planner<MathState> pl = MathProblemFactory.newPlanner();

        Problem<MathState> pr = MathProblemFactory.newDefaultProblem(52);

        Plan<MathState> sol;
        try
        {
            sol = pl.findPlan(dm, pr);
        } catch (PlannerException e)
        {
            throw new RuntimeException(e);
        }
    }
}