package com.dhbw.wbs;

import com.dhbw.dempster.BaseMeasure;

import java.util.*;

import static com.dhbw.enums.Enums.*;

/**
 * Calculates and contains BaseMeasures of every book for every manifestation of a feature based on the data in table 'test'.
 */
public class BaseMeasureLibrary
{

    private HashMap<Enum, BaseMeasure<Book>> library;


    /**
     * Returns the BaseMeasure<Book> object for a certain feature.
     *
     * @param feature The feature whose BaseMeasure object should be returned as Enum
     * @return The BaseMeasure for the feature
     */
    public BaseMeasure<Book> getBaseMeasure(Enum feature)
    {
        return library.get(feature);
    }


    /**
     * Creates and calculates all BaseMeasures from the data in 'test' and returns the object.
     */
    public BaseMeasureLibrary()
    {

        library = new HashMap<>();

        SqLiteJDBC sqLiteJDBC = new SqLiteJDBC();

        calculateAllMeasures(sqLiteJDBC);
        getOmegaForFeatures();

    }


    /**
     * Calculates BaseMeasures for every book and every manifestation of a feature.
     *
     * @param sqLiteJDBC The DAO
     */
    private void calculateAllMeasures(SqLiteJDBC sqLiteJDBC)
    {
        for (Book book : Book.values())
        {

            double count;

            if (book == Book.A)
            {
                count = sqLiteJDBC.getRowCount("Book", Book.A.getText());
            } else if (book == Book.B)
            {
                count = sqLiteJDBC.getRowCount("Book", Book.B.getText());
            } else
            {
                count = sqLiteJDBC.getRowCount("Book", Book.C.getText());
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

    /**
     * Adds the calculated measure for a certain book and feature into the library. If a BaseMeasure for this feature<br>
     * does not already exist, one is created.
     *
     * @param feature The manifestation of a feature as Enum.
     * @param book    The Book this measure was calculated for.
     * @param measure The calculated measure as double.
     */
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

    /**
     * Calculates the Omega value for every manifestation of a feature.
     */
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

            double omega = 0.01 * totalMeasures;

            baseMeasure.put(allBooksSet, omega);

            baseMeasure.normalize();

            library.put(baseMeasureEntry.getKey(), baseMeasure);
        }
    }

    /**
     * Prints out the whole library with evey BaseMeasure for every feature.
     */
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

                builder.append("Feature ")
                        .append(String.format("%10s", entry.getKey().name()))
                        .append(":\t");
                BaseMeasure<Book> measure = entry.getValue();

                for (Book book : Book.values())
                {
                    builder.append(book.getText())
                            .append(": ")
                            .append(String.format("%.2f", measure.get(new HashSet<>(Collections.singletonList(book)))))
                            .append(",\t");
                }

                builder.append("Omega: ").
                        append(String.format("%.2f", measure.get(new HashSet<>(Arrays.asList(Book.values())))));
                System.out.println(builder.toString());
            }
        }
    }
}
