package com.dhbw.dempster;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.*;

public class BaseMeasureTest
{
    private static final double DELTA = 1e-10;

    enum TestEnum
    {
        A, B, C
    }

    enum Suspect
    {
        P, F, M, K, E, A, H, J
    }

    @Test
    public void testCombine() throws Exception
    {
        // Test from an exam of Reichardt
        BaseMeasure<Suspect> m1, m2, m12;

        // Without Conflict
        m1 = new BaseMeasure<>();
        m1.put(new HashSet<>(Arrays.asList(Suspect.E, Suspect.A, Suspect.P, Suspect.M)), 0.6);
        m1.put(new HashSet<>(Arrays.asList(Suspect.values())), 0.4);

        m2 = new BaseMeasure<>();
        m2.put(new HashSet<>(Arrays.asList(Suspect.E, Suspect.H, Suspect.M)), 0.8);
        m2.put(new HashSet<>(Arrays.asList(Suspect.values())), 0.2);

        m12 = BaseMeasure.combine(m1, m2);
        assertEquals(0.48, m12.get(new HashSet<>(Arrays.asList(Suspect.E, Suspect.M))), DELTA);
        assertEquals(0.32, m12.get(new HashSet<>(Arrays.asList(Suspect.E, Suspect.H, Suspect.M))), DELTA);
        assertEquals(0.12, m12.get(new HashSet<>(Arrays.asList(Suspect.E, Suspect.A, Suspect.P, Suspect.M))), DELTA);
        assertEquals(0.08, m12.get(new HashSet<>(Arrays.asList(Suspect.values()))), DELTA);

        // With Conflict
        m1 = new BaseMeasure<>();
        m1.put(new HashSet<>(Arrays.asList(Suspect.P, Suspect.F, Suspect.M, Suspect.E)), 0.8);
        m1.put(new HashSet<>(Arrays.asList(Suspect.values())), 0.2);

        m2 = new BaseMeasure<>();
        m2.put(new HashSet<>(Arrays.asList(Suspect.P, Suspect.F)), 0.4);
        m2.put(new HashSet<>(Arrays.asList(Suspect.K, Suspect.A, Suspect.J)), 0.4);
        m2.put(new HashSet<>(Arrays.asList(Suspect.values())), 0.2);

        m12 = BaseMeasure.combine(m1, m2);
        assertEquals(0.588235294117647, m12.get(new HashSet<>(Arrays.asList(Suspect.P, Suspect.F))), DELTA);
        assertEquals(0.235294117647059, m12.get(new HashSet<>(Arrays.asList(Suspect.P, Suspect.F, Suspect.M, Suspect.E))), DELTA);
        assertEquals(0.117647058823529, m12.get(new HashSet<>(Arrays.asList(Suspect.K, Suspect.A, Suspect.J))), DELTA);
        assertEquals(0.0588235294117647, m12.get(new HashSet<>(Arrays.asList(Suspect.values()))), DELTA);
    }

    @Test
    public void testMeasureSum() throws Exception
    {
        BaseMeasure<TestEnum> m = new BaseMeasure<>();
        m.put(new HashSet<>(Arrays.asList(TestEnum.A, TestEnum.B)), 0.3);
        assertEquals(0.3, m.measureSum(), DELTA);

        m.put(new HashSet<>(Collections.singleton(TestEnum.C)), 0.3);
        assertEquals(0.6, m.measureSum(), DELTA);

        m.put(new HashSet<>(Arrays.asList(TestEnum.values())), 0.4);
        assertEquals(1.0, m.measureSum(), DELTA);
    }

    @Test
    public void testNormalize() throws Exception
    {
        Set<TestEnum> a = new HashSet<>(Collections.singleton(TestEnum.A));
        Set<TestEnum> b = new HashSet<>(Collections.singleton(TestEnum.B));
        BaseMeasure<TestEnum> m = new BaseMeasure<>();

        m.put(a, 0.2);
        m.put(b, 0.2);

        m.normalize();

        assertEquals(0.5, m.get(a), DELTA);
        assertEquals(0.5, m.get(b), DELTA);
    }

    @Test
    public void testBelief() throws Exception
    {
        BaseMeasure<Suspect> m = new BaseMeasure<>();

        m.put(new HashSet<>(Collections.singleton(Suspect.E)), 0.46);
        m.put(new HashSet<>(Arrays.asList(Suspect.K, Suspect.E, Suspect.J)), 0.04);
        m.put(new HashSet<>(Arrays.asList(Suspect.E, Suspect.M)), 0.24);
        m.put(new HashSet<>(Arrays.asList(Suspect.E, Suspect.A, Suspect.P, Suspect.M)), 0.06);
        m.put(new HashSet<>(Arrays.asList(Suspect.E, Suspect.H, Suspect.M)), 0.16);
        m.put(new HashSet<>(Arrays.asList(Suspect.values())), 0.04);

        assertEquals(0.46, m.belief(new HashSet<>(Collections.singleton(Suspect.E))), DELTA);
        assertEquals(0.70, m.belief(new HashSet<>(new HashSet<>(Arrays.asList(Suspect.E, Suspect.M)))), DELTA);
    }

    @Test
    public void testPlausibility() throws Exception
    {
        BaseMeasure<Suspect> m = new BaseMeasure<>();

        m.put(new HashSet<>(Collections.singleton(Suspect.E)), 0.46);
        m.put(new HashSet<>(Arrays.asList(Suspect.K, Suspect.E, Suspect.J)), 0.04);
        m.put(new HashSet<>(Arrays.asList(Suspect.E, Suspect.M)), 0.24);
        m.put(new HashSet<>(Arrays.asList(Suspect.E, Suspect.A, Suspect.P, Suspect.M)), 0.06);
        m.put(new HashSet<>(Arrays.asList(Suspect.E, Suspect.H, Suspect.M)), 0.16);
        m.put(new HashSet<>(Arrays.asList(Suspect.values())), 0.04);

        assertEquals(0.08, m.plausibility(new HashSet<>(Collections.singleton(Suspect.J))), DELTA);
        assertEquals(1.00, m.plausibility(new HashSet<>(Collections.singleton(Suspect.E))), DELTA);
    }
}