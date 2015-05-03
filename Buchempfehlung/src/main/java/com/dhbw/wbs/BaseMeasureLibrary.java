package com.dhbw.wbs;

import com.dhbw.dempster.BaseMeasure;
import com.dhbw.enums.Enums;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class BaseMeasureLibrary
{

    private SqLiteJDBC sqLiteJDBC = new SqLiteJDBC();
    private Statement statement;

    private HashMap<Enum, BaseMeasure<Enums.Book>> library;


    public BaseMeasure<Enums.Book> getBaseMeasure(Enum feature)
    {
        return null;
    }


    public BaseMeasureLibrary()
    {

        int totalCount = sqLiteJDBC.getRowCount();

        try
        {
            statement = new SqLiteJDBC().getStatement();
        }catch (SQLException e)
        {
            e.printStackTrace();
        }

        for (Enums.Book book : Enums.Book.values())
        {
            Set<Enums.Book> bookSet = new HashSet<>();
            bookSet.add(book);

            for (Enums.Age age : Enums.Age.values())
            {
                double measure = sqLiteJDBC.getRowCount(age.toString(), age.getText(), book) / totalCount;
                BaseMeasure<Enums.Book> baseMeasure = new BaseMeasure<>();
            }

            for (Enums.Age age : Enums.Age.values())
            {

            }

            for (Enums.Age age : Enums.Age.values())
            {

            }

            for (Enums.Age age : Enums.Age.values())
            {

            }

            for (Enums.Age age : Enums.Age.values())
            {

            }

            for (Enums.Age age : Enums.Age.values())
            {

            }

            for (Enums.Age age : Enums.Age.values())
            {

            }
        }

    }

}
