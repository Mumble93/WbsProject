package com.dhbw.wbs;

import com.dhbw.enums.Enums;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class JdbcTest
{

    private static SqLiteJDBC sqLiteJDBC;

    private static final double DELTA = 1e-10;


    @BeforeClass
    public static void setup()
    {
        sqLiteJDBC = new SqLiteJDBC();
    }

    @Test
    public void testTotalRowCount()
    {
        double totalCount = sqLiteJDBC.getRowCount();

        System.out.println("Total count was " + totalCount);

        assertEquals(100, totalCount, DELTA);
    }

    @Test
    public void testBookACount()
    {
        double count = sqLiteJDBC.getRowCount("Age", "<18", Enums.Book.A);

        System.out.println("Count was " + count);

        assertEquals(7, count, DELTA);
    }

}
