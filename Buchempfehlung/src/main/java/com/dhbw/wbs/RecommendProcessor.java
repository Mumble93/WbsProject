package com.dhbw.wbs;

import com.dhbw.dempster.BaseMeasure;
import com.dhbw.enums.Enums;

import java.io.*;
import java.util.*;

import static com.dhbw.enums.Enums.*;

public class RecommendProcessor
{
    String pathInput, pathOutput;
    BaseMeasureLibrary library;
    int lineNumber = 0;
    
    public RecommendProcessor(String pathInput, String pathOutput) throws IOException
    {
        library = new BaseMeasureLibrary();

        this.pathInput = pathInput;
        this.pathOutput = pathOutput;
    }

    public void process()
    {
        BufferedReader reader = null;
        BufferedWriter writer = null;
        String[] data;
        String line;
        String recommendation;
        try
        {
            reader = new BufferedReader(new FileReader(pathInput));
            writer = new BufferedWriter(new FileWriter(pathOutput));

            while ((line = reader.readLine()) != null)
            {
                lineNumber++;
                //Alter, Geschlecht, Verheiratet, Kinderzahl, Abschluss, Beruf, Einkommen
                data = line.split(";", 7);

                if (data.length != 7)
                {
                    System.out.println("Error in line " + lineNumber + ": Does not have 7 columns");
                    continue;
                }

                recommendation = calculateRecommendation(data);
                writer.write(line + ";" + recommendation + "\n");
            }

            reader.close();
            writer.flush();
            writer.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private String calculateRecommendation(String[] data)
    {
        List<BaseMeasure<Book>> measures = getMeasuresFromLibrary(data);

        if (measures.size() == 0)
            return "No features found";

        BaseMeasure<Book> accumulated = accumulateMeasures(measures);

        Double plausibA = accumulated.plausibility(new HashSet<>(Collections.singletonList(Book.A)));
        Double plausibB = accumulated.plausibility(new HashSet<>(Collections.singletonList(Book.B)));
        Double plausibC = accumulated.plausibility(new HashSet<>(Collections.singletonList(Book.C)));

        Map<Double, Book> plausibilities = new HashMap<>();
        plausibilities.put(plausibA, Book.A);
        plausibilities.put(plausibB, Book.B);
        plausibilities.put(plausibC, Book.C);

        Double maxPlausibility = Collections.max(plausibilities.keySet());

        String chosen = plausibilities.get(maxPlausibility).getText();

        System.out.println(String.format("Line: %5dP(A)=%5.2fP(B)=%5.2fP(C)=%5.2fchosen=%s",
                lineNumber, plausibA, plausibB, plausibC, chosen));

        return chosen;
    }

    private BaseMeasure<Book> accumulateMeasures(List<BaseMeasure<Book>> measures)
    {
        BaseMeasure<Book> result = measures.get(0);

        for (int i = 1; i < measures.size(); i++)
        {
            result = BaseMeasure.combine(result, measures.get(i));
        }

        return result;
    }

    private List<BaseMeasure<Book>> getMeasuresFromLibrary(String[] data)
    {
        List<BaseMeasure<Enums.Book>> measures = new ArrayList<>();
        Enum curEnum;
        int i = -1;

        //Alter
        if (!data[++i].isEmpty())
        {
            curEnum = Age.fromString(data[i]);
            AddMeasureIfValid(measures, curEnum, data[i]);
        }

        //Geschlecht
        if (!data[++i].isEmpty())
        {
            curEnum = Sex.fromString(data[i]);
            AddMeasureIfValid(measures, curEnum, data[i]);
        }

        //Verheiratet
        if (!data[++i].isEmpty())
        {
            curEnum = Married.fromString(data[i]);
            AddMeasureIfValid(measures, curEnum, data[i]);
        }

        //Kinderzahl
        if (!data[++i].isEmpty())
        {
            curEnum = Children.fromString(data[i]);
            AddMeasureIfValid(measures, curEnum, data[i]);
        }

        //Abschluss
        if (!data[++i].isEmpty())
        {
            curEnum = Degree.fromString(data[i]);
            AddMeasureIfValid(measures, curEnum, data[i]);
        }

        //Beruf
        if (!data[++i].isEmpty())
        {
            curEnum = Occupation.fromString(data[i]);
            AddMeasureIfValid(measures, curEnum, data[i]);
        }

        //Einkommen
        if (!data[++i].isEmpty())
        {
            curEnum = Salary.fromString(data[i]);
            AddMeasureIfValid(measures, curEnum, data[i]);
        }

        return measures;
    }

    /**
     * Lookup the measure and add it to the list, if curEnum != null. Otherwise print an error message containing the
     * original string to stdout
     * @param measures list to add the looked up measure
     * @param curEnum search measure for this enum
     * @param originalEnum string which was parsed to curEnum. Used for error message
     */
    private void AddMeasureIfValid(List<BaseMeasure<Book>> measures, Enum curEnum, String originalEnum)
    {
        if (curEnum != null)
        {
            measures.add(library.getBaseMeasure(curEnum));
        }
        else
        {
            System.out.println("Error in line " + lineNumber + ": Unknown Identifier '"+ originalEnum + "'");
        }
    }
}
