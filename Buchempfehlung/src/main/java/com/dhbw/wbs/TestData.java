package com.dhbw.wbs;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Object to hold and distribute test data.
 */
public class TestData
{
    BufferedReader reader;
    String line;

    /**
     * Creates new TestData object with file handle on specified test data file.
     *
     * @param file The CSV file that holds the test data.
     */
    public TestData(String file)
    {
        try
        {
            reader = new BufferedReader(new FileReader(file));

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Encapsulates readLine functionality of {@code BufferedReader}. Splits the data into an array by the ';' delimiter.<br>
     * If readLine returns {@code null}, the array remains empty (at length 0).
     *
     * @return The next data entry as an Array of Strings
     */
    public String[] getNextTestDataEntry()
    {
        String[] data = new String[0];

        try
        {
            if ((line = reader.readLine()) != null)
            {
                //Alter, Geschlecht, Verheiratet, Kinderzahl, Abschluss, Beruf, Einkommen, Buch
                data = line.split(";");
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        return data;
    }

}


