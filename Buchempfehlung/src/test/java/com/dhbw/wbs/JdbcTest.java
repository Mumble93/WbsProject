package com.dhbw.wbs;

import com.dhbw.enums.Enums;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class JdbcTest
{

    private static SqLiteJDBC sqLiteJDBC;

    @BeforeClass
    public static void setup()
    {
        sqLiteJDBC = new SqLiteJDBC();
    }

    @Test
    public void testTotalRowCount()
    {
        int totalCount = sqLiteJDBC.getRowCount();

        System.out.println("Total count was " + totalCount);

        assertEquals(100, totalCount);
    }

    @Test
    public void testBookACount()
    {
        int count = sqLiteJDBC.getRowCount("Age", "<18", Enums.Book.A);

        System.out.println("Count was " + count);

        assertEquals(7, count);
    }

}
