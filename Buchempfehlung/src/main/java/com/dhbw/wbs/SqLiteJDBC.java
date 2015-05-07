package com.dhbw.wbs;

import com.dhbw.enums.Enums;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

/**
 * Database Access Object for the SQLite database holding the test data.
 */
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
        //System.out.println("Opened database successfully");
    }

    public boolean testTableExists()
    {
        boolean tableExists = false;

        //getRowCount returns -1.0 if operation fails.
        //at minimum, test table should contain 1 entry.
        if (getRowCount() > 0.0)
        {
            tableExists = true;
        }

        return tableExists;
    }


    /**
     * Gets the total count of rows in the 'test' table.
     * @return Total count of rows as double.
     */
    public double getRowCount()
    {
        return getRowCount(null, null, null);
    }


    /**
     * Returns the number of rows that share a manifestation of a feature.
     *
     * @param column The specific feature (Age, Sex, Occupation...) as String.
     * @param value  The manifestation of the feature (&lt;18, m, Hausfrau...) as String.
     * @return The number of rows containing the manifestation of the feature.
     */
    public double getRowCount(String column, String value)
    {
        return getRowCount(column, value, null);
    }

    /**
     * Returns the number of rows that share a manifestation of a feature AND a book.
     *
     * @param column The specific feature (Age, Sex, Occupation...) as String
     * @param value  The manifestation of the feature (&lt;18, m, Hausfrau...) as String
     * @param book   The Book (Buch_A,_B,_C) as Enums.Book
     * @return The number of rows containing the manifestation of the feature and sharing the same book.
     */
    public double getRowCount(String column, String value, Enums.Book book)
    {
        double count = -1.0;

        try
        {
            StringBuilder sql = new StringBuilder().append("SELECT COUNT(*) FROM test");

            if (null != column && null != value)
            {
                sql.append(String.format(" WHERE %s IS '%s'", column, value));
            }

            if (null != book)
            {
                sql.append(String.format(" AND Book IS '%s'", book.getText()));
            }

            PreparedStatement preparedStatement = connection.prepareStatement(sql.toString());

            ResultSet resultSet = preparedStatement.executeQuery();

            count = resultSet.getDouble(1);

            preparedStatement.close();

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
            StringBuilder sql = new StringBuilder()
                    .append("CREATE TABLE IF NOT EXISTS TEST ")
                    .append("(AGE   TEXT, ")
                    .append("SEX    TEXT, ")
                    .append("MARRIED TEXT, ")
                    .append("CHILDREN TEXT, ")
                    .append("DEGREE TEXT, ")
                    .append("OCCUPATION TEXT,")
                    .append("SALARY TEXT, ")
                    .append("BOOK TEXT)");

            PreparedStatement preparedStatement = connection.prepareStatement(sql.toString());
            preparedStatement.executeUpdate();
            preparedStatement.close();

            System.out.println("DB Table Created");

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Fills the 'test' table of the DB Test with values derived from the sample csv.
     * @param pathToCsv Path to the CSV file with the sample data
     */
    public void createTestTableData(String pathToCsv)
    {
        //wipe table of existing data
        clearTestTable();

        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(pathToCsv));
            String line;
            String[] entry;

            while ((line = reader.readLine()) != null)
            {
                StringBuilder sql = new StringBuilder().append("INSERT INTO TEST (AGE,SEX,MARRIED,CHILDREN,DEGREE,OCCUPATION,SALARY,BOOK) " +
                                                                       "VALUES (");

               if (line.equals("Altersgruppe;Geschlecht;Verheiratet;Kinderzahl;Abschluss;Beruf;Familieneinkommen;Buch"))
                {
                    // Ignore header
                    continue;
                }

                entry = line.split(";");

                for (int i = 0; i < entry.length; i++)
                {
                    sql.append("'").append(entry[i]).append("'");
                    if (i != entry.length - 1)
                    {
                        sql.append(", ");
                    }
                }

                sql.append(");");

                try
                {
                    PreparedStatement preparedStatement = connection.prepareStatement(sql.toString());
                    preparedStatement.executeUpdate();
                    preparedStatement.close();
                } catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        System.out.println("DB Table filled with data");
    }


    /**
     * Clears all entries from table 'test'.
     */
    public void clearTestTable()
    {
        try
        {
            String sql ="DELETE FROM test";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        }catch (SQLException e)
        {
            e.printStackTrace();
        }

        System.out.println("Test Data has been wiped!");
    }
}
