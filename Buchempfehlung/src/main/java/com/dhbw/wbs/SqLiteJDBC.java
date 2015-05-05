package com.dhbw.wbs;

import com.dhbw.enums.Enums;

import java.sql.*;

public class SqLiteJDBC
{

    private Connection connection;

    /**
     * Establishes a connection to the SQLite DB. Creates the DB if it does not exist. Poses as DAO.
     */
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


    /**
     * Returns the number of rows that share a manifestation of an feature.
     *
     * @param column The specific feature (Age, Sex, Occupation...) as String.
     * @param value  The manifestation of the feature (<18, m, Hausfrau...) as String.
     * @return The number of rows containing the manifestation of the feature.
     */
    public double getRowCount(String column, String value)
    {
        return getRowCount(column, value, null);
    }

    /**
     * Returns the number of rows that share a manifestation of an arbitrary feature AND a book.
     *
     * @param column The specific feature (Age, Sex, Occupation...) as String
     * @param value  The manifestation of the feature (<18, m, Hausfrau...) as String
     * @param book   The Book (Buch_A,_B,_C) as Enums.Book
     * @return The number of rows containing the manifestation of the feature and sharing the same book.
     */
    public double getRowCount(String column, String value, Enums.Book book)
    {
        double count = -1.0;

        try
        {
            Statement statement = connection.createStatement();

            String sql = String.format("SELECT COUNT(*) FROM test WHERE %s IS '%s'", column, value);

            if (null != book)
            {
                sql += String.format(" AND Book IS '%s'", book.getText());
            }

            ResultSet resultSet = statement.executeQuery(sql);

            count = resultSet.getDouble(1);

            statement.close();

        } catch (SQLException e)
        {
            e.printStackTrace();
        }

        return count;
    }

    /**
     * Creates an SQLite with all features as columns.
     */
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

    /**
     * Fills the 'test' table of the DB Test  with values derived from the Test Data.
     *
     * @param testData The {@code TestData} object which encapsulates the Test Data from a .csv file.
     */
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
