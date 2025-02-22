import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import utils.TestSession;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static utils.Constants.LEGION_BASE_URL;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

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
        Actions actions = new Actions(driver); // We will reuse the same Actions instance a few times, so let's declare
                                               // it here.

        // Mini design template: find the div, then find the element within the div.
        // This is a common pattern in Selenium tests and it's easy to miss the
        // importance of what we're doing here.
        // Rather than attempt to find a single element in the entire page, we're
        // narrowing our search to a specific
        // area of the page. This is a good practice because it makes the test more
        // robust to changes in the page layout.
        // The fact that the findElement method is implemented by the WebElement class
        // as well as the WebDriver class
        // is such a simple thing, but it's one of the things I like best about the
        // Selenium class model.
        // I haven't designed my page object model yet, but I can see myself making full
        // use of this.

        // Scroll to the div with id "homebanner_TDogz"
        WebElement homeBannerDiv = driver.findElement(By.id("homebanner_TDogz"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", homeBannerDiv);

        // Click the first anchor found within the div. This should open the Time
        // Dogz series page in the same window.
        WebElement firstAnchor = homeBannerDiv.findElement(By.tagName("a"));
        firstAnchor.click();

        testSession.pause(4); // implicit wait... should be tidied with an explicit one.

        // Assert that the opened browser tab title begins "Time Dogz Series page"
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

        testSession.pause(3); // implicit wait... should be tidied with an explicit one.

        // Find the div element with id "tim_PastSucks_video"
        WebElement videoDiv = driver.findElement(By.id("tim_PastSucks_video"));
        actions.moveToElement(videoDiv).perform();

        // Switch to the first iframe inside the videoDiv (there is only one iframe)
        WebElement videoIframe = videoDiv.findElement(By.tagName("iframe"));
        driver.switchTo().frame(videoIframe);

        // NOTE TO SELF: I had to call the findElement method on the driver instance
        // here, because the videoIframe webelement is not avaiable inside the iFrame
        // and doesn't work.
        WebElement youTubeDiv = driver.findElement(By.id("movie_player"));

        testSession.pause(2); // implicit wait... should be tidied with an explicit one.

        // Find the button element within the embedded Youtube element (the tags change
        // as we play and pause, but there is only ever one button)
        WebElement playButton = youTubeDiv.findElement(By.cssSelector("button[title='Play']"));
        playButton.click();
        testSession.pause(3); // implicit wait... should be tidied with an explicit one.

        // YouTube video player is tricky because as we play it, the web elements within
        // the player change. e.g main play button disappears
        // and the bottom-left button changes from play to pause. This took me a while
        // to reliably get things working,
        // which only goes to show how brittle these tests can be when they depend upon
        // 3rd party code.
        WebElement pauseButton = youTubeDiv.findElement(By.cssSelector("button[class='ytp-play-button ytp-button']"));
        pauseButton.click(); // Pause the video

        // Take a screenshot of the current page
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(screenshot, new File("tim_vid_screenshot.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        driver.switchTo().defaultContent(); // Switch out of the iFrame and back to the main page
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
