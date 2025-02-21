package codeExamples.junit;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Example of use of @BeforeAll
 * Note the following:
 * - the static member variable (setupString) is accessible from testSetup() and the test methods
 * - the @BeforeAll method must be a public static method.
 * - I'm just doing this with a simple string. But we could be using this idea to create some kind of connection
 *   object in testSetup() and tearing it down in testTeardown().
 *
 *   In other words, it's the JUnit equivalent of the pytest fixtures I was using with Data Chassis, though without
 *   the nesting, which seems a real shame.
  */


public class BeforeAll {
    private static String setupString;  // static = available to class and does not need to be instantiated

    @org.junit.jupiter.api.BeforeAll
    public static void testSetup(){
        // my own testing code here
        setupString = "Do something to manipulate the static members here so can be used by the tests";
    }

    @Test
    public void test1(){
        // your code
        assertTrue(!setupString.isEmpty());
    }

    @Test
    public void test2(){
        // your code
        assertTrue(!setupString.isEmpty());
    }

    @AfterAll
    public static void testTeardown(){
        setupString = "";
    }
}