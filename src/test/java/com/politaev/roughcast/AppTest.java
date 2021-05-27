package com.politaev.roughcast;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

public class AppTest {
    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outContent;
    private PrintStream outPrintStream;

    @Before
    public void setUp() {
        outContent = new ByteArrayOutputStream();
        outPrintStream = new PrintStream(outContent);
        System.setOut(outPrintStream);
    }


    @Test
    public void testMain() {
        var args = new String[]{
                "-i", "2019-03-01T13:00:00Z/2019-05-11T15:30:00Z",
                "-d", "10",
                "48644c7a-975e-11e5-a090-c8e0eb18c1e9",
                "48cadf26-975e-11e5-b9c2-c8e0eb18c1e9"
        };
        App.main(args);
        var output = outContent.toString();
        var outputLines = output.split("\r?\n");
        assertEquals(2, outputLines.length);
        assertEquals("Available time intervals:", outputLines[0]);
        assertEquals("2019-04-26T10:00 - 2019-04-26T10:15", outputLines[1]);
    }


    @After
    public void tearDown() {
        System.setOut(originalOut);
        outPrintStream.close();
    }

}
