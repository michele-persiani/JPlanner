package io.jplanner;

import java.util.ArrayList;
import java.util.List;

public class Domain<S extends IState>
{
    public List<IOperator<S>> operators = new ArrayList<>();

    public boolean isValid()
    {
        return operators.stream().allMatch(op -> op.getCost() > 0);
    }
}
