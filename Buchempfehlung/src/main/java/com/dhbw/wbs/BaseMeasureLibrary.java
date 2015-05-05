package com.dhbw.wbs;

import com.dhbw.dempster.BaseMeasure;
import static com.dhbw.enums.Enums.*;

import java.util.*;

public class BaseMeasureLibrary
{

    private HashMap<Enum, BaseMeasure<Book>> library;


    public BaseMeasure<Book> getBaseMeasure(Enum feature)
    {
        return library.get(feature);
    }


    public BaseMeasureLibrary()
    {

        library = new HashMap<>();

        SqLiteJDBC sqLiteJDBC = new SqLiteJDBC();
        int totalCount = sqLiteJDBC.getRowCount();

        calculateAllMeasures(sqLiteJDBC, totalCount);
        getOmegaForFeatures();

    }


    private void calculateAllMeasures(SqLiteJDBC sqLiteJDBC, double totalCount)
    {
        for (Book book : Book.values())
        {
            for (Age age : Age.values())
            {
                double measure = sqLiteJDBC.getRowCount(age.getEnumType(), age.getText(), book) / totalCount;
                putMeasureInLibrary(age, book, measure);
            }

            for (Sex sex : Sex.values())
            {
                double measure = sqLiteJDBC.getRowCount(sex.getEnumType(), sex.getText(), book) / totalCount;
                putMeasureInLibrary(sex, book, measure);
            }

            for (Married married : Married.values())
            {
                double measure = sqLiteJDBC.getRowCount(married.getEnumType(), married.getText(), book) / totalCount;
                putMeasureInLibrary(married, book, measure);
            }

            for (Children children : Children.values())
            {
                double measure = sqLiteJDBC.getRowCount(children.getEnumType(), children.getText(), book) / totalCount;
                putMeasureInLibrary(children, book, measure);
            }

            for (Degree degree : Degree.values())
            {
                double measure = sqLiteJDBC.getRowCount(degree.getEnumType(), degree.getText(), book) / totalCount;
                putMeasureInLibrary(degree, book, measure);
            }

            for (Occupation occupation : Occupation.values())
            {
                double measure = sqLiteJDBC.getRowCount(occupation.getEnumType(), occupation.getText(), book) / totalCount;
                putMeasureInLibrary(occupation, book, measure);
            }

            for (Salary salary : Salary.values())
            {
                double measure = sqLiteJDBC.getRowCount(salary.getEnumType(), salary.getText(), book) / totalCount;
                putMeasureInLibrary(salary, book, measure);
            }
        }

    }

    private void putMeasureInLibrary(Enum feature, Book book, double measure)
    {
        BaseMeasure<Book> existingBaseMeasure = library.get(feature);

        if (null == existingBaseMeasure)
        {
            existingBaseMeasure = new BaseMeasure<>();
        }

        Set<Book> bookSet = new HashSet<>();
        bookSet.add(book);
        existingBaseMeasure.put(bookSet, measure);

        library.put(feature, existingBaseMeasure);

    }

    private void getOmegaForFeatures()
    {
        Set<Book> allBooksSet = new HashSet<>(Arrays.asList(Book.values()));

        for (Map.Entry<Enum, BaseMeasure<Book>> baseMeasureEntry : library.entrySet())
        {
            BaseMeasure<Book> baseMeasure = baseMeasureEntry.getValue();
            double totalMeasures = 0.0;
            for (Book book : Book.values())
            {
                totalMeasures += baseMeasure.get(new HashSet<>(Collections.singleton(book)));
            }

            double omega = 1.0 - totalMeasures;

            baseMeasure.put(allBooksSet, omega);

            Set<Book> a = new HashSet<>(Collections.singleton(Book.A));
            Set<Book> b = new HashSet<>(Collections.singleton(Book.B));
            Set<Book> c = new HashSet<>(Collections.singleton(Book.C));

            System.out.println(String.format("%10s\tA=%.2f\tB=%.2f\tC=%.2f",
                    baseMeasureEntry.getKey(), baseMeasure.get(a), baseMeasure.get(b), baseMeasure.get(c)));

            library.put(baseMeasureEntry.getKey(), baseMeasure);
        }
    }
}
