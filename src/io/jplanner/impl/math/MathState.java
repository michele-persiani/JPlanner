package io.jplanner.impl.math;

import io.jplanner.IState;

public class MathState implements IState
{
    public int x;

    @Override
    public IState clone()
    {
        MathState s = new MathState();
        s.x = x;
        return s;
    }

    @Override
    public boolean equals(Object obj)
    {
        return obj instanceof MathState && x == ((MathState)obj).x;
    }

    @Override
    public int hashCode()
    {
        return Double.hashCode(x);
    }
}
