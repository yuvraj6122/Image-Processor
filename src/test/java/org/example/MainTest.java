package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class MainTest {
    @Test
    public void testMainMethod() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        Main.main(new String[]{});
        Assertions.assertEquals("Hello and welcome!", outputStream.toString().trim());
        System.setOut(System.out);
    }
}
