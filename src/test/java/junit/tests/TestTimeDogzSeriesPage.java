package junit.tests;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import element.classes.HomePageBanner;
import element.classes.YouTubeVideo;
import element.classes.booktab.AmazonSalesLinks;
import element.classes.booktab.BookTab;
import page.classes.TimeDogzSeriesPage;
import utils.*;

import java.time.Duration;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static utils.Constants.TIMEDOGZ_SERIES_URL;

public class TestTimeDogzSeriesPage {
    private static TestSession testSession;
    private static WebDriver driver;
    private static TimeDogzSeriesPage timeDogzSeriesPage;

    @BeforeAll
    public static void testSetup() {
        testSession = new TestSession();
        driver = testSession.sessionDriver;
    }

    @BeforeEach
    public void openLegionHomepage() {
        driver.get(TIMEDOGZ_SERIES_URL); // All our tests for this class will start at the TimeDogz homepage
        timeDogzSeriesPage = new TimeDogzSeriesPage(driver);
    }

    @AfterEach
    public void closeLastPage() {
        for (String handle : driver.getWindowHandles()) {
            driver.switchTo().window(handle);
            if (driver.getWindowHandles().size() > 1) {
                driver.close();
            }
        }
    }

    @AfterAll
    public static void testTeardown() {
        testSession.closeTestSession();
    }

    @Test
    public void testTimeDogzPageTitle() {
        assertTrue(driver.getTitle().contains("Time Dogz Series page"));
    }

    @Test
    public void testTimeDogzSlogan() {
        assertTrue(timeDogzSeriesPage.verifySlogan("The past is your playground"));
    }

    @Test
    public void testTimeDogzSalesCopy() {
        assertTrue(timeDogzSeriesPage.verifySalesCopy("a little 23rd-century Danish swearing."));
    }

    @Test
    public void testTimeDogzTimeContinuumDate() { // Note: we log a warning if the date is over a year old
        assertTrue(timeDogzSeriesPage.verifyTimeContinuumDate("April 6th, 2024"));
    }

    @Test
    public void testAuthorReadingOnYouTube() {
        // Set the browser window to a size that's good for debugging.
        // This allows us to see the browser and the IDE in debug mode at the same time.
        testSession.setBrowserToDebugSize();

        timeDogzSeriesPage.youTubeOpenForTesting(); // Switches us into the video iFrame

        assertTrue(timeDogzSeriesPage.youTubePlayBigRed(), "Could not play video."); // Hit the big red play button
        testSession.sleep(4); // allow time for the video to start playing past the opening thumbnail
        assertTrue(timeDogzSeriesPage.youTubeTogglePlayPause(), "Failed to toggle play/pause button."); // pause play
        CaptureScreenshot.captureScreenshot(driver, "TimeDogzSeriesPage", "AuthorReading_1");
        testSession.sleep(4);

        // If we have successfully paused, the video will not change and so the next screenshot should be the same as the last.
        // We should see both have a 'video paused' two vertical bars at bottom left
        CaptureScreenshot.captureScreenshot(driver, "TimeDogzSeriesPage", "AuthorReading_2");
        assertTrue(timeDogzSeriesPage.youTubeTogglePlayPause(), "Failed to toggle play/pause button."); // restart the video
        testSession.sleep(4); // Move further into the video

        assertTrue(timeDogzSeriesPage.youTubeToggleMute(), "Failed to toggle mute button.");

        // The screen capture should be at a different point from the first two videos and it should show muted and the 'video paused' bars 
        // should have changed to a triangle (play) symbol.
        CaptureScreenshot.captureScreenshot(driver, "TimeDogzSeriesPage", "AuthorReading_3");

        // From this point, most of the video methods are just stubs, but they
        // illustrate some of the things we could implement if they were useful
        if (timeDogzSeriesPage.youTubeIsPlaying()) {
            timeDogzSeriesPage.youTubeTogglePlayPause(); // show how to make it pause
        }
        assertTrue(timeDogzSeriesPage.youTubeMaximise(), "Failed to maximise embedded video player.");
        assertTrue(timeDogzSeriesPage.youTubePlayOnYouTube(), "Failed to open video in YouTube");
        testSession.setBrowserToFullScreen();

        timeDogzSeriesPage.youTubeCloseForTesting(); // skips back out of the iFrame
    }

    @Test
    public void testTimeDogzGetFreeBook() {
        assertTrue(timeDogzSeriesPage.verifyGetFreeBook());
    }

    private void verifyBookDetails(int bookNumber) {
        assertTrue((timeDogzSeriesPage.moveToBookSection(1) != null),
                "Could not find the book section for book" + bookNumber + ".");
        assertTrue(timeDogzSeriesPage.verifyCoverImage(bookNumber));
        assertTrue(timeDogzSeriesPage.verifyTitle(bookNumber));
        assertTrue(timeDogzSeriesPage.verifySubTitle(bookNumber));
        if (bookNumber == 4) {
            assertTrue(timeDogzSeriesPage.verifyGetFreeBook());
        } else {
            assertTrue(timeDogzSeriesPage.verifyAmazonLink(bookNumber));
        }
    }

    @Test
    public void findThePastSucks() {
        assertTrue((timeDogzSeriesPage.moveToBookSection(1) != null), "Could not find The Past Sucks book section.");

    }

    @Test
    public void testTimeDogzThePastSucks() {
        verifyBookDetails(1);
    }

    @Test
    public void testTimeDogzAndSoDoesTheFuture() {
        verifyBookDetails(2);
    }

    @Test
    public void testTimeDogzTimeTravelInRock() {
        verifyBookDetails(3);
    }

    @Test
    public void testTimeDogzNakedInTheBlack() {
        verifyBookDetails(4);
        assertTrue(timeDogzSeriesPage.verifyNakedInBlackDownload());
    }

    @Test
    public void testTimeDogzFooter() {
        assertTrue(timeDogzSeriesPage.verifyFooter()); // An example of a method inherited from the abstract page class
    }
}
