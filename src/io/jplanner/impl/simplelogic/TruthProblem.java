package io.jplanner.impl.simplelogic;

import io.jplanner.Problem;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TruthProblem extends Problem<TruthState>
{
    public static class Builder extends io.jplanner.util.Builder<TruthProblem>
    {

        @Override
        protected TruthProblem newInstance()
        {
            TruthProblem pr = new TruthProblem();
            pr.initialState = new TruthState();
            pr.goalCondition = s -> true;
            return pr;
        }

        public Builder setInitialState(String ... truthValues)
        {
            return setInitialState(List.of(truthValues));
        }

        public Builder setInitialState(List<String> truthValues)
        {
            return setValue(this, pr -> {
               TruthState state = new TruthState();
               state.truePredicates.addAll(truthValues);
               pr.initialState = state;
            });
        }

        public Builder setGoalCondition(Map<String, Boolean> goalCondition)
        {
            return setGoalCondition(
                    goalCondition.keySet().stream().filter(goalCondition::get).collect(Collectors.toList()),
                    goalCondition.keySet().stream().filter(s -> !goalCondition.get(s)).collect(Collectors.toList())
            );
        }

        public Builder setGoalCondition(List<String> positiveValues, List<String> negativeValues)
        {
            Predicate<TruthState> goal = s -> {
                return positiveValues.stream().allMatch(s.truePredicates::contains) &&
                        negativeValues.stream().noneMatch(s.truePredicates::contains);
            };
            return setValue(this, pr -> pr.goalCondition = goal);
        }

    }
}
