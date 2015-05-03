package com.dhbw.wbs;

import java.sql.SQLException;
import java.sql.Statement;

public class BaseMeasureLibrary
{
    private SqLiteJDBC sqLiteJDBC;

    public BaseMeasureLibrary()
    {
        sqLiteJDBC = new SqLiteJDBC();
    }

    public void getTestData(String table, String... params)
    {
        String sql = "SELECT * ";

        try
        {
            Statement statement = sqLiteJDBC.getStatement();

            sql = "";

        }catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

}
