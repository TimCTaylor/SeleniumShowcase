package com.humanlegion.testoftests;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.humanlegion.utils.TestSession;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Entry point for driving tests
 *
 * In general, these are tests that verify our tests work correctly, rather than
 * tests of the system under test.
 * In other words, these are unit tests of the test code itself.
 * Where this is the case, we tag them @Tag("TestSystemTest") so we can exclude
 * them from a test run if we want.
 *
 */
public class TestSystemUnitTests {

    /**
     * This batch of tests verify that all versions of the Session object
     * constructors work and can read the default
     * options from the command line.
     */
    @Test
    @Tag("UnitTestTheTests")
    public void verifySessionOptions_noParams() {
        TestSession sessionObj = new TestSession();
        sessionObj.implicitWait(5); // we get socket exceptions if we move too fast, so wait a few seconds
        sessionObj.sessionDriver.get("https://humanlegion.com");

        String commandLineDriver = System.getProperty("driver");
        if (commandLineDriver == null || commandLineDriver.isBlank()) { // Only check for driver default if we haven't set a driver on the command line
            assertTrue(sessionObj.sessionDriver.toString().startsWith("ChromeDriver"),
                    "Test Session object should default to driver=CHROME");
        }

        String forceErrors = System.getProperty("force_errors");
        if (forceErrors == null || forceErrors.isBlank()) { // Only check for force errors default if we haven't set a value on the command line
            assertFalse(sessionObj.forceErrors, "Test Session object should default to  force errors = FALSE");
        }
        sessionObj.closeTestSession();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Tag("UnitTestTheTests")
    public void verifySessionOptions_oneParams() {
        TestSession sessionObj = new TestSession("EDGE");
        sessionObj.implicitWait(5); // we get socket exceptions if we move too fast, so wait a few seconds
        sessionObj.sessionDriver.get("https://humanlegion.com");
        System.out.println(sessionObj.sessionDriver);
        System.out.println(sessionObj.forceErrors);
        assertTrue(sessionObj.sessionDriver.toString().startsWith("EdgeDriver"),
                "Test Session object should accept constructor driver parameter");
        assertFalse(sessionObj.forceErrors, "Test Session object should default to  force errors = FALSE");
        sessionObj.closeTestSession();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Tag("UnitTestTheTests")
    public void verifySessionOptions_twoParams() {
        TestSession sessionObj = new TestSession("EDGE", true);
        sessionObj.implicitWait(5); // we get socket exceptions if we move too fast, so wait a few seconds
        assertTrue(sessionObj.sessionDriver.toString().startsWith("EdgeDriver"),
                "Test Session object should accept constructor driver parameter");
        assertTrue(sessionObj.forceErrors, "Test Session object should accept constructor forcedError parameter");
        sessionObj.closeTestSession();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Tag("UnitTestTheTests")
    public void verifySessionOptions_invalidFirstParam() {
        // Assert that the constructor throws an IllegalArgumentException for an invalid
        // WebDriver choice. Good ol' Compuserve... still helping us today.
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new TestSession("COMPUSERVE");
        }, "Expected IllegalArgumentException to be thrown when loading 'COMPUSERVE' WebDriver, but it wasn't");

    }

}
