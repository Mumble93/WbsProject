package com.dhbw.wbs;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileUtil
{
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
