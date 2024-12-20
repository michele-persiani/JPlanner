package io.jplanner.impl.simplelogic;

import io.jplanner.IState;
import java.util.HashSet;


public class TruthState implements IState
{
    public HashSet<String> truePredicates = new HashSet<>();

    @Override
    public IState clone()
    {
        TruthState newState = new TruthState();
        newState.truePredicates.addAll(truePredicates);
        return newState;
    }

    @Override
    public boolean equals(Object obj)
    {
        return obj instanceof TruthState && truePredicates.equals(((TruthState)obj).truePredicates);
    }

    @Override
    public int hashCode()
    {
        return truePredicates.hashCode();
    }
}
