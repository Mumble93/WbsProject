package com.dhbw.wbs;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Util to calculate the path of the output file.
 */
public class FileUtil
{
    /**
     * Calculate the path of the outfile to be in the same directory as the infile, but in the format
     * [filename].out.[file extension] or [filename].out if the infile does not have a file extension.
     * @param inFile Path of the input file.
     * @return Path of the output file.
     */
    public static String getOutFileName(String inFile)
    {
        String outFile;
        String hasFileExtensionRegex = "(.*)(\\.[^/\\\\]*)$";
        Pattern hasFileExtension = Pattern.compile(hasFileExtensionRegex);
        Matcher matcher = hasFileExtension.matcher(inFile);

        if (matcher.matches())
        {
            outFile = matcher.group(1) + ".out" + matcher.group(2);
        }
        else
        {
            outFile = inFile + ".out";
        }
        return outFile;
    }
}
