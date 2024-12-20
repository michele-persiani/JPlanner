package io.jplanner.impl.simplelogic;

import io.jplanner.Domain;
import io.jplanner.IOperator;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TruthDomain extends Domain<TruthState>
{
    public static class Builder extends io.jplanner.util.Builder<TruthDomain>
    {

        @Override
        protected TruthDomain newInstance()
        {
            return new TruthDomain();
        }

        public Builder addOperator(String name, float cost, Map<String, Boolean> preconditions, Map<String, Boolean> effects)
        {
            return addOperator(
                    name,
                    cost,
                    preconditions.keySet().stream().filter(preconditions::get).collect(Collectors.toList()),
                    preconditions.keySet().stream().filter(k -> !preconditions.get(k)).collect(Collectors.toList()),
                    effects.keySet().stream().filter(preconditions::get).collect(Collectors.toList()),
                    effects.keySet().stream().filter(k -> !effects.get(k)).collect(Collectors.toList())
            );
        }

        public Builder addOperator(String name, float cost, List<String> positivePrecond, List<String> negativePrecond, List<String> positiveEffects, List<String> negativeEffects)
        {
            IOperator<TruthState> op = new IOperator<>()
            {
                @Override
                public boolean canApply(TruthState toState)
                {
                    return positivePrecond.stream().allMatch(toState.truePredicates::contains) &&
                            negativePrecond.stream().noneMatch(toState.truePredicates::contains);
                }

                @Override
                public float getCost()
                {
                    return cost;
                }

                @Override
                public void accept(TruthState truthState)
                {
                    negativeEffects.forEach(truthState.truePredicates::remove);
                    positiveEffects.forEach(truthState.truePredicates::add);
                }

                @Override
                public String toString()
                {
                    return String.format("(%s %s PRE: %s not %s EFF: %s not %s)", name, cost, positivePrecond, negativePrecond, positiveEffects, negativeEffects);
                }
            };

            return setValue(this, pr -> pr.operators.add(op));
        }
    }
}
