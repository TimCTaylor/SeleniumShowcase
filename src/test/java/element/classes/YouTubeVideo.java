/**
 * YouTubeVideo.java
 * This is an example of our element abstraction layer, abstracting the details of how to interact with an embedded YouTube video.
 * I found it tricky to work with when I interracted with the video on DemonstrateObjectModel01.java, so it was a candidate for abstraction
 * because:
 * 1. The interaction is complex. So by abstracting away the details of that interaction, I can make the test code simpler 
 *    and separate the intent of the tests (which I keep in the JUnit test classes) from the details of how to interact with the video (which
 *    I keep in the class you see here).
 * 2. The code in this class is tightly coupled with both YouTube's implmentation of their video player, and the Muffin Builder framework that I used to
 *    build the humanlegion.com website. Obviously, I have no control over either. So there is an inherent brittleness here because a 3rd party could
 *    change their implementation and break my code. By abstracting the details of how to interact with the video, I can isolate the impact of such changes.
 *    In other words, if YouTube change their video player, I should be able to make the necesary changes to this class and leave the tests themselves untouched.
 *    In a real organisation, this kind of abstraction could be crucial if we had a team of testers where not all of them were skilled enough to be confident
 *    to maintain the more complex code that we have abstracted out of the JUnit tests.
 * 3. This class encapsulates a YouTube embedded video player, but there are many other HTML5 video players. If the website under test switched to a different video
 *    player, we could implement them as different classes that implement a common MoviePlayer interface. That way we wouldn't have to change our JUnit test
 *    methods other than to swap the class we instantiate.
 * 
 *    The class as it stands is a partially evolved state. All the implemented methods work on my desktop browser, but some methods are stubs -- examples of how
 *    we might flesh out the class if we were doing this for real. Also, the embedded video controls work differently in mobile browsers, so this class would need 
 *    more work there if this were a real project.
 */

package element.classes;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.interactions.Actions;

public class YouTubeVideo {
    private WebElement moviePlayerDiv, muteButton, bigRedPlayButton, bottomLeftPlayPauseButton;
    private WebDriver driver;
    private Actions action;
    private WebDriverWait wait;

    final static int DEFAULT_WAIT_TIMEOUT = 210; // Set this high when we're debugging and want to see out watch window!
    // The constants define the classes of elements we want to interract with. Even
    // though other attributes change as we toggle them, the class remains constant.
    // The extra dots are needed to make the class selector work in Selenium for
    // compound class elements. It allows us to
    // call findElement() using By.cssSelector(). We cannot directly call
    // By.className with compound classes.
    final static String BIG_RED_PLAY_BUTTON_CLASS = ".ytp-large-play-button.ytp-button.ytp-large-play-button-red-bg";
    final static String BTM_LEFT_PLAY_PAUSE_BUTTON_CLASS = ".ytp-play-button.ytp-button";
    final static String MUTE_BUTTON_CLASS = ".ytp-mute-button.ytp-button";

    // movieIFrame parameter is the iFrame the movie player sits inside. We assume
    // the elements inside the movie player are the same for all instances
    // public YouTubeVideo(WebElement movieIFrame, WebDriver driver) {
    public YouTubeVideo(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_WAIT_TIMEOUT));
        action = new Actions(driver);

        moviePlayerDiv = driver.findElement(By.id("movie_player"));
        System.out.println(("found movie player"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", moviePlayerDiv);
        bigRedPlayButton = moviePlayerDiv.findElement(By.cssSelector(BIG_RED_PLAY_BUTTON_CLASS));
        System.out.println((((bigRedPlayButton == null) ? "Didn't find big red" : "found big red")));
        bottomLeftPlayPauseButton = moviePlayerDiv
                .findElement(By.cssSelector("button[class='ytp-play-button ytp-button']"));

        System.out.println(((bottomLeftPlayPauseButton != null ? "found btm left" : "Didn't find btm left")));
        muteButton = moviePlayerDiv.findElement(By.cssSelector(MUTE_BUTTON_CLASS));
        System.out.println((muteButton != null ? "found mute" : "didn't find mute"));
    }

    public boolean waitToLoad() {
        System.out.println(("Reached wait to load"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", moviePlayerDiv);
        System.out.println(("Did some scrolling"));
        return wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(BIG_RED_PLAY_BUTTON_CLASS))) != null;
    }

    // returns true if the big red button is available and we can click it. Note:
    // once a video is playing, this button disappears and is not clickable
    public boolean playBigRed() {
        Boolean retVal = false;
        if (bigRedPlayButton.isDisplayed()) {
            bigRedPlayButton.click();
            retVal = true;
        }
        wait.until(ExpectedConditions.invisibilityOf(bigRedPlayButton)); // Explicit wait for the video to start playing
                                                                         // (and the big red button to disappear).
        return retVal;
    }

    // In desktop browsers, we need to hover the mouse over the bottom of the
    // embedded player to make the controls appear.
    // Before we do that, the play/pause button is neither visible nor selectable.
    // On my Chrome browser on my Android phone, this play/pause button is not
    // visible until you tap it, but it is selectable.
    // I'm not going to make this class work for mobile browsers, but if if this
    // were a real project, this class would need more
    // work.
    public void togglePlayButton() {
        action.moveToElement(bottomLeftPlayPauseButton).perform();
        wait.until(ExpectedConditions.elementToBeClickable(bottomLeftPlayPauseButton));
        bottomLeftPlayPauseButton.click();
    }

    // Below are stubs for how we might flesh out the class if we were doing this
    // for real

    public boolean toggleMute() { // In our desktop browsers, we need to hover over the control bar to make the
                                  // mute button clickable
        action.moveToElement(muteButton).perform();
        wait.until(ExpectedConditions.elementToBeClickable(muteButton));
        muteButton.click();
        return true;
    }

    public boolean play() {
        /// logic should be... click if not already playing. This isn't as simple as it
        /// might look
        return true; // change this to return true only if the video is playing
    }

    public boolean pause() {
        return true;
    }

    public boolean isPlaying() {
        // logic to check if the video is playing
        return true;
    }

    public boolean resize() {
        // logic to resize the video player
        return true;
    }

    public boolean maximise() {
        return true;
    }

    public boolean playOnYouTube() {
        return true;
    }

}
