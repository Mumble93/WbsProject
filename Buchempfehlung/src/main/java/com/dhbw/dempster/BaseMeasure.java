package com.dhbw.dempster;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by degele on 16.04.2015.
 */
public class BaseMeasure<T extends Enum> {
    private Map<Set<T>, Double> focalAmount = new HashMap<Set<T>, Double>();

    public BaseMeasure() {

    }

    public Double get(Object key) {
        return focalAmount.get(key);
    }

    public Set<Set<T>> keySet() {
        return focalAmount.keySet();
    }

    public void clear() {
        focalAmount.clear();
    }

    public boolean containsKey(Object key) {
        return focalAmount.containsKey(key);
    }

    public Double remove(Object key) {
        return focalAmount.remove(key);
    }

    /**
     * Put a set and a measure to the BaseMeasure. The sum of measures in the BaseMeasure must not exceed 1
     * @param set
     * @param measure
     */
    public void put(Set<T> set, double measure) {
        double total = measureSum() + measure;

        if (focalAmount.containsKey(set)) {
            double oldValue = focalAmount.get(set);
            if (measure > oldValue && total > 1)
                throw new IllegalArgumentException("Sum of measures must not exceed 1.00");
        }
        else {
            if (total > 1)
                throw new IllegalArgumentException("Sum of measures must not exceed 1.00");
        }
        focalAmount.put(set, measure);
    }

    /**
     * Put a set to the BaseMeasure. The total sum of measures will be 1 afterwards
     * @param set
     */
    public void put(Set<T> set) {
        double total = measureSum();
        double measure = 1 - total;
        if (measure < 0)
            throw new IllegalArgumentException("Sum of measures must not exceed 1.00");

        focalAmount.put(set, measure);
    }

    public BaseMeasure<T> combine(BaseMeasure<T> other) {
        BaseMeasure<T> a = this;
        BaseMeasure<T> b = other;
        BaseMeasure<T> result = new BaseMeasure<T>();


        for (Set<T> outerSet : a.keySet()) {
            for (Set<T> innerSet : b.keySet()) {
                Set<T> mergedSet = new HashSet<T>(outerSet);
                mergedSet.retainAll(innerSet);

                double newMeasure = a.get(outerSet) * b.get(innerSet);

                if (result.containsKey(mergedSet))
                    newMeasure += result.get(mergedSet);

                result.put(mergedSet, newMeasure);
            }
        }

        Set<T> emptySet = new HashSet<T>();
        if (result.containsKey(emptySet)) {
            double k = result.get(emptySet);
            result.remove(emptySet);
            double factor = 1 / (1 - k);

            for (Set<T> set : result.keySet()) {
                double oldValue = result.get(set);
                double newValue = oldValue * factor;
                result.put(set, newValue);
            }
        }

        return  result;
    }

    /**
     * Calculate the current sum of the BaseMeasure
     * @return
     */
    public double measureSum() {
        double total = 0;
        for (Double d : focalAmount.values())
            total += d;
        return total;
    }
}