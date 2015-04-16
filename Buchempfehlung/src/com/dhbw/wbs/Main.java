package com.dhbw.wbs;

public class Main
{

    private static SqLiteJDBC sqlite;

    public static void main(String[] args)
    {

        sqlite = new SqLiteJDBC();

        if (args.length < 1)
        {
            System.out.println("Must enter path to testfile");
            System.exit(0);
        }
        else
        {
            TestData testData = new TestData(args[0]);
            //sqlite.createTestTable();
            //sqlite.createTestTableData(testData);
        }
    }
}
