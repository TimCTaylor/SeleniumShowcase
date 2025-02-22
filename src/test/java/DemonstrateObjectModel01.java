
/**
 * In the DemonstrateObjectModelxx sequence of classes, I'll demonstrate a typical flow of a Selenium test automation
 * project as we first write a simple coded solution for a suite of test cases.
 *
 * Then I will refactor that first solution, introducing an element abstraction layer and page object model.
 *
 * The system under test is my own Human Legion website, which I use to communicate with fans of my book series. A
 * crucial part of this are buy links to various online retailers across the world, most importantly Amazon's regional
 * stores. By the time I have my abstraction object layers designed, I intend to run critical automation tests to verify
 * Amazon sales links on books that I haven't even thought of yet, let alone published. I can't think of a better
 * practical demonstration of the benefits of a page object model and other abstraction layer design.
 *
 *
 * As I write this note, I don't yet have anything more than an initial sketch for what those abstraction layers will
 * look like. I'm very comfortable with that. I don't need to. I do have some goals for the abstraction layers though.
 * They will:
 * - separate the intent of the tests from the implementation of the tests. No matter what we do, we cannot decouple our
 *   test automation from the implementation of the system under test, which will always mean a level of brittleness to
 *   out tests. By separating out the implementation of our tests, if our tests break because the system under test has
 *   changed its implementation, we might be able to restrict our fixes to the test implementation (e.g. to our code that
 *   defines our page object model), leaving the test classes themselves unchanged. (I say 'might' because if the SUT
 *   changes fundmentally then we might need to change the intent of our tests as well as their implementation.
 *
 *
 *   - not contain assertions, which are restricted to the test classes. This supports the previous point. We need to
 *     design our JUnit tests so that if they fail because  the page object model has broken then this is made very clear
 *     in terms of error messaging.
 */
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import utils.TestSession;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static utils.Constants.LEGION_BASE_URL;

public class DemonstrateObjectModel01 {

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
        // todo have things here that I mark: 'to be abstracted with abstract page
        // object'
        // todo and other things that are 'to be abstracted with page specific models'
        // so we will have checks for page foot copyright etc as our abstract page
        // object. And clicking the
        // youtube video on the TD page as a page specific (ie a class derrived from the
        // HumanLegionPageObject class

        // Declare some common variables we'll use for multiple actions and assertions.
        Actions actions = new Actions(driver); // We will reuse the same Actions instance a few times, so let's declare
                                               // it here.
        WebElement homeBannerDiv; // Element for each banner div
        WebElement firstAnchor; // First anchor found within a parent element

        // LEGION BOOK SERIES BANNER TESTS
        // -- In later iterations, we will abstract the banner elements and then
        // abstract the pages they link to.
        // -- But for now, we will simply write all the tests out in a big long list.

        // Scroll to the div for the Legion series banner
        homeBannerDiv = driver.findElement(By.id("homebanner_Legion"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", homeBannerDiv);

        // Click the first anchor found within the div. This should open the book series
        // page in the same browser window.
        firstAnchor = homeBannerDiv.findElement(By.tagName("a"));
        firstAnchor.click();

        // TIME DOGZ BOOK SERIES BANNER TESTS
        // Scroll to the div with id "homebanner_TDogz"
        homeBannerDiv = driver.findElement(By.id("homebanner_TDogz"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", homeBannerDiv);

        // Click the first anchor found within the div. This should open the Time
        // Dogz series page in the same window.
        firstAnchor = homeBannerDiv.findElement(By.tagName("a"));
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

        // Switch to the first iframe inside the videoDiv
        WebElement videoIframe = videoDiv.findElement(By.tagName("iframe"));
        driver.switchTo().frame(videoIframe);

        // todo we're not finding the next element!!!!
        WebElement youTubeDiv = videoIframe.findElement(By.cssSelector("[aria-label='YouTube Video Player']"));
        testSession.pause(2); // implicit wait... should be tidied with an explicit one.

        // Find the button element within the embedded Youtube element (the tags change
        // as we play and pause, but there is only ever one button)
        WebElement playButton = youTubeDiv.findElement(By.tagName("button"));
        playButton.click();

        testSession.pause(3); // implicit wait... should be tidied with an explicit one.

        playButton.click(); // Pause the video

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
