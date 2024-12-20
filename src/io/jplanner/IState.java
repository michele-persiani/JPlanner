package io.jplanner;


/**
 * Base interface for state objects.
 */
public interface IState extends Cloneable
{
    /**
     * Clone the state and returns the cloned copy of it
     * @return the cloned copy of the state
     */
    IState clone();

    /**
     * equals() and hashCode() need to be implemented by subclasses
     * @param o
     * @return
     */
    abstract boolean equals(Object o);

    /**
     * equals() and hashCode() need to be implemented by subclasses
     * @return
     */
    abstract int hashCode();
}
