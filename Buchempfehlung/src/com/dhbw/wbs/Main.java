package com.dhbw.wbs;

public class Main
{

    public static void main(String[] args)
    {
        if (args.length < 1)
        {
            System.out.println("Must enter path to testifle");
            System.exit(0);
        }
        else
        {
            TestData testData = new TestData(args[0]);
        }
        
    }
}
