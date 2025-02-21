import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import utils.TestSession;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static utils.Constants.LEGION_BASE_URL;

/**
 * The purpose of the tests in this class is to perform chains of interactions
 * with the human legion website that
 * simulates a user browsing to it for the first time and taking a look around.
 * It is a useful test in itself, because it reflects real workflow and verifies
 * behaviour of a broad, though shallow,
 * view of the website. A smoke test, in other words. However, it's primary
 * purpose is to explore the test approach.
 * The test will initially be written 'in line'. That is to say, I won't
 * abstract the implementation of the tests to
 * the element or page object layers. The learning from this approach will
 * inform me in the design of those abstraction
 * layers and the code here will subsequently be refactored so that the intent
 * of the tests (and the assertions) remain
 * here in this class, and the implementation of those tests are abstracted into
 * implementation layers (ie page object
 * model and element object models).
 */
public class TestHappyPath {
    private static TestSession testSession;
    private static WebDriver driver; // Point to the driver directly to make the code simpler to read

    @BeforeAll
    public static void testSetup() {
        testSession = new TestSession();
        driver = testSession.sessionDriver;
    }

    @BeforeEach
    public void openLegionHomepage() {
        driver.get(LEGION_BASE_URL); // All our tests for this class will start at the Human Legion homepage
    }

    @Test
    public void happyTour1() {
        Actions actions = new Actions(driver); // We will reuse the same Actions instance a few times.

        // Scroll to the div with id "homebanner_TDogz"
        WebElement homeBannerDiv = driver.findElement(By.id("homebanner_TDogz"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", homeBannerDiv);

        // Click the first anchor found within the div. This should open the Time
        // Dogz series page in the same window
        WebElement firstAnchor = homeBannerDiv.findElement(By.tagName("a"));
        firstAnchor.click();

        // Assert that the browser tab title begins "Time Dogz Series page"
        String expectedTDTitle = "Time Dogz Series page";
        String actualTDTitle = driver.getTitle();
        assertTrue(actualTDTitle.startsWith(expectedTDTitle),
                "The browser tab title should start with 'Time Dogz Series page'.");

        // Scroll to the div with id "TDogz_slogan_text". Since we used
        // JavascriptExecutor earlier, let's use Actions this time
        WebElement sloganDiv = driver.findElement(By.id("TDogz_slogan_text"));
        actions.moveToElement(sloganDiv).perform();

        // Check that the first <h2> tag in the div has the visible text "The past is
        // your playground"
        WebElement h2SloganTag = sloganDiv.findElement(By.tagName("h2"));
        String expectedSloganText = "The past is your playground.";
        String actualSloganText = h2SloganTag.getText();
        assertEquals(expectedSloganText, actualSloganText,
                "The <h2> tag text should be 'The past is your playground.'");
    }

    /**
     * In a conventional project, I think I would kill this next method and close my
     * windows explicitly from within
     * each test. However, this is a showcase, and here I am gratuitously
     * demonstrating the difference between
     * 
     * @AfterEach and @AfterAll, and also between WebDriver.close() which closes the
     *            currently focused window
     *            but keeps the WebDriver session active and quit() that shuts down
     *            the entire session.
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
