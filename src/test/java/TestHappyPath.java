import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import utils.TestSession;

import static utils.Constants.LEGION_BASE_URL;

/**
 * The purpose of the tests in this class is to perform chains of interactions with the human legion website that
 * simulates a user browsing to it for the first time and taking a look around.
 * It is a useful test in itself, because it reflects real workflow and verifies behaviour of a broad, though shallow,
 * view of the website. A smoke test, in other words. However, it's primary purpose is to explore the test approach.
 * The test will initially be written 'in line'. That is to say, I won't abstract the implementation of the tests to
 * the element or page object layers. The learning from this approach will inform me in the design of those abstraction
 * layers and the code here will subsequently be refactored so that the intent of the tests (and the assertions) remain
 * here in this class, and the implementation of those tests are abstracted into implementation layers (ie page object
 * model and element object models).
 */
public class TestHappyPath {
    private static TestSession testSession;
    private static WebDriver driver;  // Point to the driver directly to make the code simpler to read

    @BeforeAll
    public static void testSetup() {
        testSession = new TestSession();
        driver = testSession.sessionDriver;
    }

    @BeforeEach
    public void openLegionHomepage() {
        driver.get(LEGION_BASE_URL);  // All our tests for this class will start at the Human Legion homepage
    }






    /**
     * In a conventional project, I think I would kill this next method and close my windows explicitly from within
     * each test. However, this is a showcase, and here I am gratuitously demonstrating the difference between
     * @AfterEach and @AfterAll, and also between WebDriver.close() which closes the currently focused window
     * but keeps the WebDriver session active and quit() that shuts down the entire session.
     */
    @AfterEach
    public void closeLastPage() {
        driver.close();
    }


    @AfterAll
    public static void testTeardown() {
        testSession.closeTestSession();
    }
}
