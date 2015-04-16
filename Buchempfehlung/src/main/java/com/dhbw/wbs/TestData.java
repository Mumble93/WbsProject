package com.dhbw.wbs;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TestData
{
    BufferedReader reader;
    String line;

    /**
     * Object to hold and distribute test data
     * @param file The CSV file that holds the test data
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
        }catch (IOException e)
        {
            e.printStackTrace();
        }

        return data;
    }

}


