package com.dhbw.dempster;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by degele on 16.04.2015.
 */
public class BaseMeasure<T extends Enum>
{
    private Map<Set<T>, Double> focalAmount = new HashMap<>();

    public BaseMeasure()
    {

    }

    public Double get(Set<T> key)
    {
        return focalAmount.get(key);
    }

    public Set<Set<T>> keySet()
    {
        return focalAmount.keySet();
    }

    public void clear()
    {
        focalAmount.clear();
    }

    public boolean containsKey(Set<T> key)
    {
        return focalAmount.containsKey(key);
    }

    public Double remove(Set<T> key)
    {
        return focalAmount.remove(key);
    }

    /**
     * Put a set and a measure to the BaseMeasure. The sum of measures in the BaseMeasure should not exceed 1
     *
     * @param set set
     * @param measure measure
     */
    public void put(Set<T> set, double measure)
    {
        focalAmount.put(set, measure);
    }

    /**
     * Combine two BaseMeasures to an accumulated one
     * @param a m_a
     * @param b m_b
     * @param <X> Type of enum
     * @return combined BaseMeasure m_a + m_b
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
            double k = result.get(emptySet);
            if (k >= 1)
                throw new ArithmeticException("k >= 1");

            result.remove(emptySet);
            double factor = 1 / (1 - k);

            for (Set<X> set : result.keySet())
            {
                double oldValue = result.get(set);
                double newValue = oldValue * factor;
                result.put(set, newValue);
            }
        }

        return result;
    }

    /**
     * Normalize the sum of all elements to 1
     */
    public void normalize()
    {
        double currentSum = measureSum();
        for (Set<T> set : this.keySet())
            this.put(set, this.get(set) / currentSum);
    }

    /**
     * Calculate the current sum of the BaseMeasure
     * @return sum of all elements
     */
    public double measureSum()
    {
        double total = 0;
        for (Double d : focalAmount.values())
            total += d;
        return total;
    }

    /**
     * Calculate belief of set x
     * @param x set x
     * @return belief of x
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
     * Calculate plausibility of set x
     * @param x set x
     * @return plausibility of x
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