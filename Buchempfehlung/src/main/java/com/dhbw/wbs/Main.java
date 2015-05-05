package com.dhbw.wbs;

import java.io.IOException;

public class Main
{

    private static SqLiteJDBC sqlite;

    public static void main(String[] args)
    {
        sqlite = new SqLiteJDBC();

        if (args.length == 2)
        {
            if (args[0].equalsIgnoreCase("create_db"))
            {
                TestData testData = new TestData(args[1]);
                sqlite.createTestTable();
                sqlite.createTestTableData(testData);
            }
            else if (args[0].equalsIgnoreCase("recommend"))
            {
                try
                {
                    String inFile = args[1];
                    String outFile;

                    outFile = FileUtil.getOutFileName(inFile);

                    RecommendProcessor processor = new RecommendProcessor(inFile, outFile);
                    processor.process();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                printHelp();
            }
        }
        else
        {
            printHelp();
        }

    }

    private static void printHelp()
    {
        System.out.println("Unrecognized arguments:\n operation path_to_csv\nSupported operations: create_db, recommend");
        System.exit(1);
    }
}
