package com.dhbw.wbs;

import com.dhbw.enums.Enums;
import junit.framework.TestCase;

public class JdbcTest extends TestCase
{

    private static SqLiteJDBC sqLiteJDBC = new SqLiteJDBC();


    public static void testTotalRowCount()
    {
        int totalCount = sqLiteJDBC.getRowCount();

        System.out.println("Total count was " + totalCount);

        assertEquals(100, totalCount);
    }

    public static void testBookACount()
    {
        int count = sqLiteJDBC.getRowCount("Age", "<18", Enums.Book.A);

        System.out.println("Count was " + count);

        assertEquals(7, count);
    }

}
