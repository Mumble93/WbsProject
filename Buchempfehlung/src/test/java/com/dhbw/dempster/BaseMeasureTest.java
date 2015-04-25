package com.dhbw.dempster;

import junit.framework.TestCase;

import java.util.*;

/**
 * Created by Andreas on 25.04.2015.
 */
public class BaseMeasureTest extends TestCase
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
        assertEquals(m12.get(new HashSet<>(Arrays.asList(Suspect.E, Suspect.M))), 0.48, DELTA);
        assertEquals(m12.get(new HashSet<>(Arrays.asList(Suspect.E, Suspect.H, Suspect.M))), 0.32, DELTA);
        assertEquals(m12.get(new HashSet<>(Arrays.asList(Suspect.E, Suspect.A, Suspect.P, Suspect.M))), 0.12, DELTA);
        assertEquals(m12.get(new HashSet<>(Arrays.asList(Suspect.values()))), 0.08, DELTA);

        // With Conflict
        m1 = new BaseMeasure<>();
        m1.put(new HashSet<>(Arrays.asList(Suspect.P, Suspect.F, Suspect.M, Suspect.E)), 0.8);
        m1.put(new HashSet<>(Arrays.asList(Suspect.values())), 0.2);

        m2 = new BaseMeasure<>();
        m2.put(new HashSet<>(Arrays.asList(Suspect.P, Suspect.F)), 0.4);
        m2.put(new HashSet<>(Arrays.asList(Suspect.K, Suspect.A, Suspect.J)), 0.4);
        m2.put(new HashSet<>(Arrays.asList(Suspect.values())), 0.2);

        m12 = BaseMeasure.combine(m1, m2);
        assertEquals(m12.get(new HashSet<>(Arrays.asList(Suspect.P, Suspect.F))), 0.588235294117647, DELTA);
        assertEquals(m12.get(new HashSet<>(Arrays.asList(Suspect.P, Suspect.F, Suspect.M, Suspect.E))), 0.235294117647059, DELTA);
        assertEquals(m12.get(new HashSet<>(Arrays.asList(Suspect.K, Suspect.A, Suspect.J))), 0.117647058823529, DELTA);
        assertEquals(m12.get(new HashSet<>(Arrays.asList(Suspect.values()))), 0.0588235294117647, DELTA);
    }

    public void testMeasureSum() throws Exception
    {
        BaseMeasure<TestEnum> m = new BaseMeasure<>();
        m.put(new HashSet<>(Arrays.asList(TestEnum.A, TestEnum.B)), 0.3);
        assertEquals(m.measureSum(), 0.3, DELTA);

        m.put(new HashSet<>((List<TestEnum>) Collections.singletonList(TestEnum.C)), 0.3);
        assertEquals(m.measureSum(), 0.6, DELTA);

        m.put(new HashSet<>(Arrays.asList(TestEnum.values())), 0.4);
        assertEquals(m.measureSum(), 1.0, DELTA);
    }

    public void testNormalize() throws Exception
    {
        Set<TestEnum> a = new HashSet<>((List<TestEnum>) Collections.singletonList(TestEnum.A));
        Set<TestEnum> b = new HashSet<>((List<TestEnum>) Collections.singletonList(TestEnum.B));
        BaseMeasure<TestEnum> m = new BaseMeasure<>();

        m.put(a, 0.2);
        m.put(b, 0.2);

        m.normalize();

        assertEquals(m.get(a), 0.5, DELTA);
        assertEquals(m.get(b), 0.5, DELTA);
    }
}