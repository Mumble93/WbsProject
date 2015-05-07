package com.dhbw.dempster;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A class to model the base measure m of the Dempster-Shafer theory. Subgroups are built with sets which are composed
 * of enum values. Each set/subgroup has an evidence value assigned. The sum of all subgroups should be 1. This can
 * be achieved with calling normalize() and is done automatically before any calculation.<br>
 * Omega is equal to Enum.values().
 * @param <T> Type of enum to use
 */
public class BaseMeasure<T extends Enum>
{
    protected Map<Set<T>, Double> focalAmount = new HashMap<>();

    /**
     * Returns the evidence value for a subgroup, or 0.0 if the subgroup is not in the focal amounts
     * @param key subgroup
     * @return evidence value of the subgroup
     */
    public Double get(Set<T> key)
    {
        Double result = focalAmount.get(key);
        if (result == null)
            return 0.0;
        else
            return result;
    }

    /**
     * Get all subgroups in the basemeasure.
     * @return All defined subgroups.
     */
    public Set<Set<T>> keySet()
    {
        return focalAmount.keySet();
    }

    /**
     * Check if a subgroup is defined inside the basemeasure aka is a focal group.
     * @param key Subgroup as Set.
     * @return True if there is a subgroup within the basemeasure, otherwise false.
     */
    public boolean containsKey(Set<T> key)
    {
        return focalAmount.containsKey(key);
    }

    /**
     * Remove a subgroup from the basemeasure.
     * @param key Subgroup to remove.
     * @return Evidence value of the removed subgroup.
     */
    public Double remove(Set<T> key)
    {
        return focalAmount.remove(key);
    }

    /**
     * Put a subgroup and its evidence value to the BaseMeasure. The sum of measures in the BaseMeasure should not exceed 1.
     * @param set Subgroup as Set.
     * @param measure Evidence value.
     */
    public void put(Set<T> set, double measure)
    {
        if(measure > 0)
        {
            // Only safe focal subgroups
            focalAmount.put(set, measure);
        }
    }

    /**
     * Combine two BaseMeasures to an accumulated one.
     * @param a First basemeasure m_a.
     * @param b Second basemeasure m_b.
     * @param <X> Type of enum.
     * @return Combined BaseMeasure m_a + m_b.
     */
    public static <X extends Enum> BaseMeasure<X> combine(BaseMeasure<X> a, BaseMeasure<X> b)
    {
        BaseMeasure<X> result = new BaseMeasure<>();

        // Normalize measureSum to 1
        // because double may cause rounding errors
        a.normalize();
        b.normalize();

        // Dempster Shafer
        for (Set<X> outerSet : a.keySet())
        {
            for (Set<X> innerSet : b.keySet())
            {
                Set<X> mergedSet = new HashSet<>(outerSet);
                mergedSet.retainAll(innerSet);

                double newMeasure = a.get(outerSet) * b.get(innerSet);

                if (result.containsKey(mergedSet))
                    newMeasure += result.get(mergedSet);

                result.put(mergedSet, newMeasure);
            }
        }

        // Handle conflict
        Set<X> emptySet = new HashSet<>();
        if (result.containsKey(emptySet))
        {
            double k = result.remove(emptySet);
            if (k >= 1)
                throw new ArithmeticException("k >= 1");

            result.normalize();
        }

        return result;
    }

    /**
     * Normalize the sum of all elements to 1.
     */
    public void normalize()
    {
        double currentSum = measureSum();
        for (Set<T> set : this.keySet())
            this.put(set, this.get(set) / currentSum);
    }

    /**
     * Calculate the current sum of all evidence values in the basemeasure.
     * @return Sum of all elements.
     */
    public double measureSum()
    {
        double total = 0;
        for (Double d : focalAmount.values())
            total += d;
        return total;
    }

    /**
     * Calculate belief of subgroup x.
     * @param x Subgroup x.
     * @return Belief of x.
     */
    public double belief(Set<T> x)
    {
        normalize();

        double sum = 0;
        for (Set<T> y : focalAmount.keySet())
        {
            if(x.containsAll(y))
            {
                sum += this.get(y);
            }
        }
        return sum;
    }

    /**
     * Calculate plausibility of subgroup x.
     * @param x Subgroup x.
     * @return Plausibility of x.
     */
    public double plausibility(Set<T> x)
    {
        normalize();

        double sum = 0;

        for (Set<T> y : focalAmount.keySet())
        {
            Set<T> mergedSet = new HashSet<>(x);
            mergedSet.retainAll(y);

            if (!mergedSet.isEmpty())
            {
                sum += this.get(y);
            }
        }

        return sum;
    }
}