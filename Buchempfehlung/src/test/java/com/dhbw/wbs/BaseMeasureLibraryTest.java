package com.dhbw.wbs;

import static com.dhbw.enums.Enums.*;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

public class BaseMeasureLibraryTest
{
    private static BaseMeasureLibrary library;

    private static final double DELTA = 1e-10;


    @BeforeClass
    public static void setup()
    {
        library = new BaseMeasureLibrary();
    }

    @Test
    public void testBaseMeasureForMaleAndBookA()
    {

        double measureForMaleAndBookA = library.getBaseMeasure(Sex.M).get(new HashSet<>(Collections.singletonList(Book.A)));

        assertEquals(0.19, measureForMaleAndBookA, DELTA);
    }

    @Test
    public void testBaseMeasureForMaleAndBookB()
    {

        double measureForMaleAndBookB = library.getBaseMeasure(Sex.M).get(new HashSet<>(Collections.singletonList(Book.B)));

        assertEquals(0.07, measureForMaleAndBookB, DELTA);
    }

    @Test
    public void testBaseMeasureForMaleAndBookC()
    {

        double measureForMaleAndBookC = library.getBaseMeasure(Sex.M).get(new HashSet<>(Collections.singletonList(Book.C)));

        assertEquals(0.3, measureForMaleAndBookC, DELTA);
    }

    @Test
    public void testBaseMeasureForMaleAndBookOmega()
    {

        double measureForMaleAndBookOmega = library.getBaseMeasure(Sex.M).get(new HashSet<>(Arrays.asList(Book.values())));

        assertEquals(0.44, measureForMaleAndBookOmega, DELTA);
    }
}
