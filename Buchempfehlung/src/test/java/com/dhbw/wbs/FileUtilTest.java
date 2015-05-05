package com.dhbw.wbs;

import org.junit.Test;

import static org.junit.Assert.*;

public class FileUtilTest
{

    @Test
    public void testGetOutFileName() throws Exception
    {
        assertEquals("/dir1/dir2/file.out.csv", FileUtil.getOutFileName("/dir1/dir2/file.csv"));
        assertEquals("/dir1/dir2/file.out.txt", FileUtil.getOutFileName("/dir1/dir2/file.txt"));
        assertEquals("/dir1/dir2/file.out", FileUtil.getOutFileName("/dir1/dir2/file"));
        assertEquals("\\dir1\\dir2\\file.out.csv", FileUtil.getOutFileName("\\dir1\\dir2\\file.csv"));
        assertEquals("\\dir1\\dir2\\file.out.txt", FileUtil.getOutFileName("\\dir1\\dir2\\file.txt"));
        assertEquals("\\dir1\\dir2\\file.out", FileUtil.getOutFileName("\\dir1\\dir2\\file"));
    }
}