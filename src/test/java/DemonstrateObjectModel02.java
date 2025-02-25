
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import element.classes.YouTubeVideo;
import utils.*;

import java.time.Duration;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static utils.Constants.LEGION_BASE_URL;

public class DemonstrateObjectModel02 {

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
        public void happyTour2() {

                // Declare some common variables we'll use for multiple actions and assertions.
                Actions actions = new Actions(driver); // We will reuse the same Actions instance a few times, so let's
                                                       // declare it here.
                WebElement homeBannerDiv; // Element for each banner div
                WebElement firstAnchor; // First anchor found within a parent element
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(8)); // We'll use this for explicit
                                                                                       // waits
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
                                .until(ExpectedConditions
                                                .presenceOfElementLocated(By.cssSelector("section#MarineCadet")));
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
                WebElement listElement2 = marineCadetSection
                                .findElement(By.cssSelector("li[aria-controls='MarineCadet-2']"));
                listElement2.click();
                testSession.implicitWait(2); // I'm not sure how to do an explicit wait on this neatly, because the tab
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
                sectionTab2.getText().contains("the largest independent audiobook publisher in the US"); // This is a
                                                                                                         // simple
                                                                                                         // check that
                                                                                                         // the text
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
                WebElement listElement1 = marineCadetSection
                                .findElement(By.cssSelector("li[aria-controls='MarineCadet-1']"));
                listElement1.click();
                testSession.implicitWait(2); // I'm not sure how to do an explicit wait on this neatly, because the tab
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

                testSession.implicitWait(4); // implicit wait... should be tidied with an explicit one.

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

                testSession.implicitWait(0); // turn it off so we don't mix implicit and explicit waits

                /**
                 * Test the embedded YouTube video player
                 * In this second pass, we have abstracted the player into a class called
                 * YouTubeVideo.
                 */
                testSession.setBrowserToDebugSize(); // Set the browser window to a size that's good for debugging/
                                                     // This allows us to see the browser and the IDE at the same time.

                // Find the div element with id "tim_PastSucks_video"
                WebElement videoDiv = wait
                                .until(ExpectedConditions.presenceOfElementLocated(By.id("tim_PastSucks_video")));
                actions.moveToElement(videoDiv).perform();

                // Switch to the first iframe inside the videoDiv (there is only one iframe) and
                // use an instance of our abstracted YouTubeVideo class to interract with it
                WebElement videoIframe = videoDiv.findElement(By.tagName("iframe"));
                driver.switchTo().frame(videoIframe);
                YouTubeVideo pastSucksVideo = new YouTubeVideo(videoIframe, driver);
                assertTrue(pastSucksVideo.waitToLoad(), "Timed out waiting for the Past Sucks video to load.");
                assertTrue(pastSucksVideo.playBigRed(), "Could not play video."); // Hit the big red play button
                testSession.sleep(4);
                pastSucksVideo.togglePlayButton(); // pause play
                CaptureScreenshot.captureScreenshot(driver, "happyTour2", "tim_vid_screenshot_1");
                testSession.sleep(4);
                ; // If we have successfully paused, the video will not change and so the
                  // next
                  // screenshot should be the same as the last. We should see both have a
                  // 'video
                  // paused' two vertical bars at bottom left
                CaptureScreenshot.captureScreenshot(driver, "happyTour2", "tim_vid_screenshot_2");
                pastSucksVideo.togglePlayButton(); // restart the video
                testSession.sleep(4); // Move further into the video

                assertTrue(pastSucksVideo.toggleMute(), "Failed to toggle mute button.");
                CaptureScreenshot.captureScreenshot(driver, "happyTour2", "tim_vid_screenshot_3"); // should be
                                                                                                   // at a different
                                                                                                   // point from the
                                                                                                   // first two videos
                                                                                                   // and it should show
                                                                                                   // muted and the
                                                                                                   // 'video paused'
                                                                                                   // bars should
                                                                                                   // have changed to a
                                                                                                   // trianle (play)
                                                                                                   // symbol.

                // From this point, most of the video methods are just stubs, but they
                // illustrate some of the things we could implement if they were useful
                if (pastSucksVideo.isPlaying()) {
                        pastSucksVideo.togglePlayButton(); // show how to make it pause
                }
                assertTrue(pastSucksVideo.maximise(), "Failed to maximise embedded video player.");
                assertTrue(pastSucksVideo.playOnYouTube(), "Failed to open video in YouTube");
                testSession.setBrowserToFullScreen();
                driver.switchTo().defaultContent(); // Switch out of the iFrame and back to the main page

                // My original intention was to click on all the book series banners and carry
                // out a mix of generic and page-specific
                // tests on each. However, I think by this point, I've teased out enough
                // understanding of the problem area from what
                // I've written to proceed to design a first set of abstraction layers for
                // elements that were repeated and/ or
                // tricky to work with (book sections, youtube video, etc). And also to design a
                // first set of page-specific classes.
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
