package com.dhbw.wbs;

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
                sqlite.createTestTable();
                sqlite.createTestTableData(args[1]);
            }
            else if (args[0].equalsIgnoreCase("recommend"))
            {
                if(!sqlite.testTableExists())
                {
                    System.out.println("ERROR: Failed to retrieve test data. Do DB and table 'test' exist?");
                    System.exit(2);
                }
                String inFile = args[1];
                String outFile;

                outFile = FileUtil.getOutFileName(inFile);

                RecommendProcessor processor = new RecommendProcessor(inFile, outFile);
                processor.process();
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
