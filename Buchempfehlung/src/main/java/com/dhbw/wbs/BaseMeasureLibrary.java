package com.dhbw.wbs;

import com.dhbw.dempster.BaseMeasure;

import java.util.*;

import static com.dhbw.enums.Enums.*;

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
        //double totalCount = sqLiteJDBC.getRowCount();

        calculateAllMeasures(sqLiteJDBC);
        getOmegaForFeatures();

    }


    private void calculateAllMeasures(SqLiteJDBC sqLiteJDBC)
    {
        for (Book book : Book.values())
        {
            
            double count;
            
            if(book == Book.A)
            {
                count = sqLiteJDBC.getRowCountForFeature("Book", Book.A.getText());
            }
            else if (book == Book.B)
            {
                count = sqLiteJDBC.getRowCountForFeature("Book", Book.B.getText());
            }
            else
            {
                count = sqLiteJDBC.getRowCountForFeature("Book", Book.C.getText());
            }

            
            for (Age age : Age.values())
            {
                double measure = sqLiteJDBC.getRowCount(age.getEnumType(), age.getText(), book) / count;
                putMeasureInLibrary(age, book, measure);
            }

            for (Sex sex : Sex.values())
            {
                double measure = sqLiteJDBC.getRowCount(sex.getEnumType(), sex.getText(), book) / count;
                putMeasureInLibrary(sex, book, measure);
            }

            for (Married married : Married.values())
            {
                double measure = sqLiteJDBC.getRowCount(married.getEnumType(), married.getText(), book) / count;
                putMeasureInLibrary(married, book, measure);
            }

            for (Children children : Children.values())
            {
                double measure = sqLiteJDBC.getRowCount(children.getEnumType(), children.getText(), book) / count;
                putMeasureInLibrary(children, book, measure);
            }

            for (Degree degree : Degree.values())
            {
                double measure = sqLiteJDBC.getRowCount(degree.getEnumType(), degree.getText(), book) / count;
                putMeasureInLibrary(degree, book, measure);
            }

            for (Occupation occupation : Occupation.values())
            {
                double measure = sqLiteJDBC.getRowCount(occupation.getEnumType(), occupation.getText(), book) / count;
                putMeasureInLibrary(occupation, book, measure);
            }

            for (Salary salary : Salary.values())
            {
                double measure = sqLiteJDBC.getRowCount(salary.getEnumType(), salary.getText(), book) / count;
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
                totalMeasures += baseMeasure.get(new HashSet<>(Collections.singletonList(book)));
            }

            double omega = 0.1 * totalMeasures;

            baseMeasure.put(allBooksSet, omega);

            baseMeasure.normalize();

            library.put(baseMeasureEntry.getKey(), baseMeasure);
        }
    }

    public void printLibrary()
    {
        if (null == library || library.isEmpty())
        {
            System.out.println("Library not initialized");
        } else
        {
            for (Map.Entry<Enum, BaseMeasure<Book>> entry : library.entrySet())
            {
                StringBuilder builder = new StringBuilder();

                builder.append("Feature ").append(entry.getKey().name()).append(": ");
                BaseMeasure<Book> measure = entry.getValue();

                for (Book book : Book.values())
                {
                    builder.append(book.getText()).append(": ").append(measure.get(new HashSet<>(Collections.singletonList(book)))).append(", ");
                }

                builder.append("Omega: ").append(measure.get(new HashSet<>(Arrays.asList(Book.values()))));
                System.out.println(builder.toString());
            }
        }
    }
}
