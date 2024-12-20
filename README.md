# JPlanner
(See master branch)

Simple Java planner implementing breadth-first, depth-first, Dijkstra, A* search strategies on custom state spaces.

Example utilization:

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
