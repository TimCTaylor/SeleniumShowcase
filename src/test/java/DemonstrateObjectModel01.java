
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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import utils.TestSession;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

import javax.imageio.ImageIO;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static utils.Constants.LEGION_BASE_URL;
import static utils.Constants.SCREENSHOT_FOLDER;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;

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
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(8)); // We'll use this for explicit waits
        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(2)); // For briefer waits

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

        // Wait for the new page to load by checking for the presence of the Marine
        // Cadet section. Then we move to it
        // and then we wait for it to be visible.
        WebElement marineCadetSection = wait
                .until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("section#MarineCadet")));
        actions.moveToElement(marineCadetSection).perform();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("section#MarineCadet")));
        assertTrue(marineCadetSection.isDisplayed(), "The Marine Cadet section should be displayed.");

        // The 'section' is a complex element of tabs, images and text. We need to click
        // on a list element to expand the corresponding tab.
        // I use these sections as my key object to promote each book across each series
        // (and so across multiple poages)
        // same idea across multiple pages. These sections are ripe for abstraction into
        // an element object to be embedded in
        // page objects. But that's for another pass.
        WebElement listElement2 = marineCadetSection.findElement(By.cssSelector("li[aria-controls='MarineCadet-2']"));
        listElement2.click();
        testSession.pause(2); // I'm not sure how to do an explicit wait on this neatly, because the tab
        // itself is always visible. I'll come back to it.

        // We've clicked on the tab, but now we need to select the div that corresponds
        // to the content of tab2.
        WebElement sectionTab2 = marineCadetSection.findElement(By.id("MarineCadet-2"));
        // When we click on a tab, the other tabs are hidden and we can check this by
        // looking at the aria-hidden property.
        System.out.println(sectionTab2.getText());
        // NEXT HACK
        // Let's assume that we can find the sections ok. It's a problem with
        // getDomProperty. conclusion: IT IS!
        assertEquals("false", sectionTab2.getDomAttribute("aria-hidden"),
                "The 'Try the audio' Marine Cadet tab should be selected.");

        // Let's check the visible text in the tab.
        sectionTab2.getText().contains("the largest independent audiobook publisher in the US"); // This is a simple
                                                                                                 // check that the text
                                                                                                 // is present

        // Let's check other tabs are hidden
        WebElement sectionTab1 = marineCadetSection.findElement(By.id("MarineCadet-1"));
        assertEquals("true", sectionTab1.getDomAttribute("aria-hidden"),
                "The 'The Fight for Freedom...' Marine Cadet tab should be hidden.");

        // The other tags should be hidden and they should have the css style of
        // display:none. So let's check the gettext() method returns an empty string
        assertTrue(sectionTab1.getText().isEmpty(),
                "The 'The Fight for Freedom...' Marine Cadet tab text should be empty when not selected.");

        // Now let's click on section 1 and check that its text becomes visible...
        WebElement listElement1 = marineCadetSection.findElement(By.cssSelector("li[aria-controls='MarineCadet-1']"));
        listElement1.click();
        testSession.pause(2); // I'm not sure how to do an explicit wait on this neatly, because the tab
        assertEquals("false", sectionTab1.getDomAttribute("aria-hidden"),
                "The 'The Fight for Freedom...' Marine Cadet tab should be selected.");
        assertTrue(sectionTab1.getText().contains("2565 A.D."),
                "The first tab of the Marine Cadet section should be visible after clicking'");

        // ... and that section 2 is no longer visible
        assertEquals("true", sectionTab2.getDomAttribute("aria-hidden"),
                "The 'Try the audio' Marine Cadet tab should be hidden after clicking the first tab.");
        assertTrue(sectionTab2.getText().isEmpty(),
                "The 'Try the audio' Marine Cadet tab text should be invisible when not selected.");

        driver.navigate().back(); // Go back to the homepage

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

        // Switch to the first iframe inside the videoDiv (there is only one iframe)
        WebElement videoIframe = videoDiv.findElement(By.tagName("iframe"));
        driver.switchTo().frame(videoIframe);

        // NOTE TO SELF: I had to call the findElement method on the driver instance
        // here, because the videoIframe webelement is not avaiable inside the iFrame
        // and doesn't work.
        // EXAMPLE OF GOOD CODE PATTERN: The WebDriverWait.until() method returns a
        // WebElement. So instead of doing the wait and then finding the element as two
        // separate method calls, I combine the two into a single action.
        WebElement youTubeDiv = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("movie_player")));

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
        // To reduce file size, I had to hunt around Stack Overflow until I could find a
        // way to resize the image.
        // It worked (I achieved an 85% reduction in file size from the original .png
        // output), however...
        // The code rapidly got pretty complex, and I'm not familiar with these image
        // manipulation libraries. All of
        // which makes this a good candidate for abstraction into a utility class on the
        // next pass.
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            BufferedImage bufferedImage = ImageIO.read(screenshot);
            BufferedImage resizedImage = new BufferedImage(800, 600, bufferedImage.getType());
            Graphics2D g = resizedImage.createGraphics();
            g.drawImage(bufferedImage, 0, 0, 800, 600, null);
            g.dispose();

            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String filename = SCREENSHOT_FOLDER + "tim_vid_screenshot_" + timestamp + ".jpg";
            File screenshotFolder = new File(SCREENSHOT_FOLDER); // Create the folder (the any parent path to it) if it
                                                                 // doesn't exist
            if (!screenshotFolder.exists()) {
                screenshotFolder.mkdirs();
            }
            ImageIO.write(resizedImage, "jpg", new File(filename));
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
