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
    private Map<Set<T>, Double> focalAmount = new HashMap<Set<T>, Double>();

    public BaseMeasure()
    {

    }

    public Double get(Object key)
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

    public boolean containsKey(Object key)
    {
        return focalAmount.containsKey(key);
    }

    public Double remove(Object key)
    {
        return focalAmount.remove(key);
    }

    /**
     * Put a set and a measure to the BaseMeasure. The sum of measures in the BaseMeasure should not exceed 1
     *
     * @param set
     * @param measure
     */
    public void put(Set<T> set, double measure)
    {
        focalAmount.put(set, measure);
    }

    public static <X extends Enum> BaseMeasure<X> combine(BaseMeasure<X> a, BaseMeasure<X> b)
    {
        BaseMeasure<X> result = new BaseMeasure<X>();

        // Normalize measureSum to 1
        // because double may cause rounding errors
        a.normalize();
        b.normalize();

        // Dempster Shafer
        for (Set<X> outerSet : a.keySet())
        {
            for (Set<X> innerSet : b.keySet())
            {
                Set<X> mergedSet = new HashSet<X>(outerSet);
                mergedSet.retainAll(innerSet);

                double newMeasure = a.get(outerSet) * b.get(innerSet);

                if (result.containsKey(mergedSet))
                    newMeasure += result.get(mergedSet);

                result.put(mergedSet, newMeasure);
            }
        }

        // Handle conflict
        Set<X> emptySet = new HashSet<X>();
        if (result.containsKey(emptySet))
        {
            double k = result.get(emptySet);
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
}