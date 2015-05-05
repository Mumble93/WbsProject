package com.dhbw.wbs;

import com.dhbw.dempster.BaseMeasure;
import com.dhbw.enums.Enums;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class BaseMeasureLibrary
{

    private static HashMap<Enum, BaseMeasure<Enums.Book>> library;


    public BaseMeasure<Enums.Book> getBaseMeasure(Enum feature)
    {
        return library.get(feature);
    }


    public BaseMeasureLibrary()
    {

        library = new HashMap<>();

        SqLiteJDBC sqLiteJDBC = new SqLiteJDBC();
        int totalCount = sqLiteJDBC.getRowCount();

        try
        {
            Statement statement = new SqLiteJDBC().getStatement();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }

        calculateAllMeasures(sqLiteJDBC, totalCount);
        getOmegaForFeatures();

    }


    private void calculateAllMeasures(SqLiteJDBC sqLiteJDBC, double totalCount)
    {
        for (Enums.Book book : Enums.Book.values())
        {
            for (Enums.Age age : Enums.Age.values())
            {
                double measure = sqLiteJDBC.getRowCount(age.getEnumType(), age.getText(), book) / totalCount;
                putMeasureInLibrary(age, book, measure);
            }

            for (Enums.Sex sex : Enums.Sex.values())
            {
                double measure = sqLiteJDBC.getRowCount(sex.getEnumType(), sex.getText(), book) / totalCount;
                putMeasureInLibrary(sex, book, measure);
            }

            for (Enums.Married married : Enums.Married.values())
            {
                double measure = sqLiteJDBC.getRowCount(married.getEnumType(), married.getText(), book) / totalCount;
                putMeasureInLibrary(married, book, measure);
            }

            for (Enums.Children children : Enums.Children.values())
            {
                double measure = sqLiteJDBC.getRowCount(children.getEnumType(), children.getText(), book) / totalCount;
                putMeasureInLibrary(children, book, measure);
            }

            for (Enums.Degree degree : Enums.Degree.values())
            {
                double measure = sqLiteJDBC.getRowCount(degree.getEnumType(), degree.getText(), book) / totalCount;
                putMeasureInLibrary(degree, book, measure);
            }

            for (Enums.Occupation occupation : Enums.Occupation.values())
            {
                double measure = sqLiteJDBC.getRowCount(occupation.getEnumType(), occupation.getText(), book) / totalCount;
                putMeasureInLibrary(occupation, book, measure);
            }

            for (Enums.Salary salary : Enums.Salary.values())
            {
                double measure = sqLiteJDBC.getRowCount(salary.getEnumType(), salary.getText(), book) / totalCount;
                putMeasureInLibrary(salary, book, measure);
            }
        }

    }

    private void putMeasureInLibrary(Enum feature, Enums.Book book, double measure)
    {
        BaseMeasure<Enums.Book> existingBaseMeasure = library.get(feature);

        if (null == existingBaseMeasure)
        {
            existingBaseMeasure = new BaseMeasure<>();
        }

        Set<Enums.Book> bookSet = new HashSet<>();
        bookSet.add(book);
        existingBaseMeasure.put(bookSet, measure);

        library.put(feature, existingBaseMeasure);

    }

    private void getOmegaForFeatures()
    {
        Set<Enums.Book> allBooksSet = new HashSet<>(Arrays.asList(Enums.Book.values()));

        for (Map.Entry<Enum, BaseMeasure<Enums.Book>> baseMeasureEntry : library.entrySet())
        {
            BaseMeasure<Enums.Book> baseMeasure = baseMeasureEntry.getValue();
            double totalMeasures = 0.0;
            for (Enums.Book book : Enums.Book.values())
            {
                totalMeasures += baseMeasure.get(new HashSet<>(Collections.singletonList(book)));
            }

            double omega = 1.0 - totalMeasures;

            baseMeasure.put(allBooksSet, omega);

            library.put(baseMeasureEntry.getKey(), baseMeasure);
        }
    }
}
