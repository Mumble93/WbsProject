package com.dhbw.wbs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SqLiteJDBC
{

    private Connection connection;

    public SqLiteJDBC()
    {
        try
        {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:test.db");
        } catch (ClassNotFoundException | SQLException e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened database successfully");
    }

    public Statement getStatement() throws SQLException
    {
        return connection.createStatement();
    }

    public void createTestTable()
    {
        System.out.println("Preparing DB with test entries...");

        try
        {
            Statement statement = connection.createStatement();

            String sql = "CREATE TABLE IF NOT EXISTS TEST " +
                    "(AGE   TEXT, " +
                    "SEX    TEXT, " +
                    "MARRIED TEXT, " +
                    "CHILDREN TEXT, " +
                    "DEGREE TEXT, " +
                    "OCCUPATION TEXT," +
                    "SALARY TEXT, " +
                    "BOOK TEXT)";
            statement.executeUpdate(sql);
            statement.close();

            System.out.print("DB Prepared");

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void createTestTableData(TestData testData)
    {
        String[] entry;
        String sql = "INSERT INTO TEST (AGE,SEX,MARRIED,CHILDREN,DEGREE,OCCUPATION,SALARY,BOOK) " +
                "VALUES (";
        while ((entry = testData.getNextTestDataEntry()).length != 0)
        {
            StringBuilder builder = new StringBuilder();
            builder.append(sql);

            for (int i = 0; i < entry.length; i++)
            {
                builder.append("'").append(entry[i]).append("'");
                if (i != entry.length - 1)
                {
                    builder.append(", ");
                }
            }

            builder.append(");");

            try
            {
                Statement statement = connection.createStatement();
                statement.executeUpdate(builder.toString());
                statement.close();
            } catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
    }
}
