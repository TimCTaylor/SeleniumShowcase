package page.classes;

/** A page object for encapsulating tests specific to the Time Dogz book series page.
 *  This class makes use of element abstraction layer classes in the element.classes package.
 *  It extends the HumanLegionPage class, which is the abstract base class of our page object model.
 *  In accordance with our page oject model, this class does not contain junit tests directly. Rather
 *  it it implements test logic used by test methods in the junit.tests package.
 *  We keep the *intent* of the tests and the assertions themselves in junit.tests package classes and
 *  separate out into these page classes (where useful) the test implementation and the unavoidable tight
 *  coupling to the implementation of the system under test.
 * 
 *  Note to self for writing further page classes: plan the test, write the intent of the test in the junit tests
 *  calling stub methods in the page class, and make those stubs real as we go along.
 */

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import element.classes.YouTubeVideo;
import utils.VerifyLink;

import java.util.List;

import static utils.Constants.AVOID_OPENING_AMAZON_LINKS;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class TimeDogzSeriesPage extends HumanLegionPage {
    private YouTubeVideo pastSucksVideo; // This is a YouTubeVideo object that we instantiate in the openYouTubeForTesting() method because we need to be in the right iFrame first. 
    private Actions actions = new Actions(driver);;
    private WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(8));

    public TimeDogzSeriesPage(WebDriver driver) {
        super(driver);

    }

    public boolean verifySlogan(String expectedSlogan) {
        boolean retVal = false;
        WebElement sloganDiv = driver.findElement(By.id("TDogz_slogan_text"));
        List<WebElement> elements = sloganDiv.findElements(By.xpath(".//*"));
        for (WebElement element : elements) {
            if (element.getText().contains(expectedSlogan)) {
                retVal = true;
                break;
            }
        }
        return retVal;
    }

    public boolean verifySalesCopy(String expectedCopy) {
        boolean retVal = false;
        WebElement salesCopyDiv = driver.findElement(By.id("TDogz_sales_copy"));
        List<WebElement> elements = salesCopyDiv.findElements(By.tagName("p"));
        for (WebElement element : elements) {
            if (element.getText().contains(expectedCopy)) {
                retVal = true;
                break;
            }
        }
        return retVal;
    }

    public boolean verifyTimeContinuumDate(String expectedDate) {
        final String LINE_PREFIX = "The current location is: ";
        final String LINE_SUFFIX = ". What's happening now?";
        boolean retVal = false;
        WebElement timeContinuumDiv = driver.findElement(By.id("TDogz_time_continuum"));
        List<WebElement> elements = timeContinuumDiv.findElements(By.tagName("p"));
        for (WebElement element : elements) {
            String text = element.getText();
            if (text.contains(LINE_PREFIX)) {
                int startIndex = LINE_PREFIX.length(); // returns the index of the char after the prefix
                int endIndex = text.indexOf(LINE_SUFFIX);
                String dateAsString = text.substring(startIndex, endIndex);
                if (dateAsString.equals(expectedDate)) {
                    // Add logic to verify the date is in the correct format and raise warning if over a year old.
                    LocalDate date = getDateFromTimeContinuum(dateAsString);

                    if (date != null) {
                        retVal = true;
                        LocalDate now = LocalDate.now();
                        // atStartOfDay() is a workaround for LocalDate not having a time component, which would otherwise spark an exception in Duration.between().
                        Duration duration = Duration.between(date.atStartOfDay(), now.atStartOfDay());
                        long days = duration.toDays();
                        if (days > 365) {
                            System.out.println("Warning: TimeDogz 'now' date is over a year old.");
                        }
                    }
                    retVal = true;
                }
                break;
            }
        }
        return retVal;
    }

    private LocalDate getDateFromTimeContinuum(String dateAsString) {
        LocalDate retVal = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d'th', yyyy");
        try {
            retVal = LocalDate.parse(dateAsString, formatter);
        } catch (DateTimeParseException e) {
            // Could be expanded to check other date formats and to log the warning rather than send to stdout.
            System.out.println("Warning: Date format not recognised.");
        }
        return retVal;
    }

    public boolean verifyGetFreeBook() {
        // Add logic to verify the "Get Free Book" section
        return true;
    }

    // Returns books section webelement for passed book number or null if not found
    public WebElement moveToBookSection(int bookNumber) {
        String bookSectionId;
        if (bookNumber == 4) {
            bookSectionId = "TDogz_Naked";
        } else {
            bookSectionId = "TDogz_book" + bookNumber;
        }
        WebElement bookSection = driver.findElement(By.id(bookSectionId));
        if (bookSection != null) {
            actions.moveToElement(bookSection).perform();
            wait.until(ExpectedConditions.visibilityOf(bookSection));
        }
        return bookSection;
    }

    public boolean verifyCoverImage(int bookNumber) {
        boolean retVal = false;
        WebElement bookSection = moveToBookSection(bookNumber);
        if (bookSection != null) {
            WebElement coverDiv = bookSection.findElement(By.className("image_wrapper"));
            WebElement coverImage = coverDiv.findElement(By.tagName("img"));
            if (coverImage != null) {
                String src = coverImage.getDomAttribute("src");
                String fileName = src.substring(src.lastIndexOf('/') + 1);
                String expectedFileName;
                switch (bookNumber) {
                    case 1:
                        expectedFileName = "Time-Dogz-Bk1-ebook-cover";
                        break;
                    case 2:
                        expectedFileName = "TD2_temp_800px";
                        break;
                    case 3:
                        expectedFileName = "TD3_temp_800px";
                        break;
                    case 4:
                        expectedFileName = "Naked-in-the-Black2_800px";
                        break;
                    default:
                        expectedFileName = "xxxInvalidBookNumberxxx";
                }
                retVal = (fileName.contains(expectedFileName));
            }
        }
        return retVal;
    }

    public boolean verifyTitle(int bookNumber) {
        boolean retVal = false;
        WebElement bookSection = moveToBookSection(bookNumber);
        if (bookSection != null) {
            String expectedTitle;
            switch (bookNumber) {
                case 1:
                    expectedTitle = "The Past Sucks";
                    break;
                case 2:
                    expectedTitle = "And So Does the Future";
                    break;
                case 3:
                    expectedTitle = "Time Travel in Rock: 1984";
                    break;
                case 4:
                    expectedTitle = "Naked in the Black";
                    break;
                default:
                    expectedTitle = "xxxInvalidBookNumberxxx";
            }
            WebElement titleElement = bookSection.findElement(By.tagName("h2"));
            String title = titleElement.getText();
            retVal = title.contains(expectedTitle);
        }
        return retVal;
    }

    public boolean verifySubTitle(int bookNumber) {
        boolean retVal = false;
        WebElement bookSection = moveToBookSection(bookNumber);
        if (bookSection != null) {
            String expectedSubTitle;
            switch (bookNumber) {
                case 1:
                    expectedSubTitle = "The past is your playground";
                    break;
                case 2:
                    expectedSubTitle = "In which Stiletto saves the world";
                    break;
                case 3:
                    expectedSubTitle = "In which Stiletto uses the power of rock to fight a Soviet takeover of the world";
                    break;
                case 4:
                    expectedSubTitle = "The last thing he needs is to be sent naked into the black";
                    break;
                default:
                    expectedSubTitle = "xxxInvalidBookNumberxxx";
            }
            WebElement titleElement = bookSection.findElement(By.tagName("h5"));
            String subTitle = titleElement.getText();
            retVal = subTitle.contains(expectedSubTitle);
        }
        return retVal;
    }

    public boolean verifyAmazonLink(int bookNumber) {
        boolean retVal = false;
        WebElement bookSection = moveToBookSection(bookNumber);
        if (bookSection != null) {
            WebElement button = bookSection
                    .findElement(By.cssSelector(".button.button_right.button_theme.button_size_2"));
            if (button != null) {
                String expectedASIN; // ASIN is the Amazon Standard Identification Number (product number) and will be part of the url for storefront page.
                switch (bookNumber) {
                    case 1:
                        expectedASIN = "B0CW3WKBH8";
                        break;
                    case 2:
                        expectedASIN = "B0CX57BBCZ";
                        break;
                    case 3:
                        expectedASIN = "B0CTHPWCZ8";
                        break;
                    default:
                        expectedASIN = "xxxInvalidBookNumberxxx";
                }
                retVal = true;
                if (!AVOID_OPENING_AMAZON_LINKS) {
                    retVal = VerifyLink.verifyLink(driver, button, expectedASIN); // this code works, but I would rather not open up the link repeatedly in case Amazon shuts me down for suspicious behaviour
                }
            }
        }
        return retVal;

    }

    // TODO  Verify we can download the book from BookFunnel
    public boolean verifyNakedInBlackDownload() {
        return true;
    }
    // == Methods related to the embedded YouTube video player ==

    // Performs actions to setup the YouTube video for testing. Must be called before any other YouTube methods.
    // We choose to instantiate the pastSucksVideo object here because we need to be in the right iFrame first.
    public boolean youTubeOpenForTesting() {

        // Find the div element with id "tim_PastSucks_video"
        WebElement videoDiv = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tim_PastSucks_video")));

        actions.moveToElement(videoDiv).perform();

        // Switch to the first iframe inside the videoDiv (there is only one iframe) and
        // use our instance of our abstracted YouTubeVideo class to interract with it (we instantiated this in the constructor).
        WebElement videoIframe = videoDiv.findElement(By.tagName("iframe"));
        driver.switchTo().frame(videoIframe);
        pastSucksVideo = new YouTubeVideo(driver); // We have to wait until we are inside the iFrame before we can instantiate the YouTubeVideo object.
        return (pastSucksVideo.waitToLoad());

    }

    public boolean youTubeCloseForTesting() {
        // Add logic here
        // pause the video if playing
        // jump back to parent frame
        driver.switchTo().defaultContent(); // Switch out of the iFrame and back to the main page
        return true;
    }

    public boolean youTubePlayBigRed() {
        return pastSucksVideo.playBigRed();
    }

    public boolean youTubeTogglePlayPause() {
        pastSucksVideo.togglePlayButton();
        return true;
    }

    public boolean youTubeToggleMute() {
        return pastSucksVideo.toggleMute();
    }

    public boolean youTubeIsPlaying() {
        return pastSucksVideo.isPlaying();
    }

    public boolean youTubeMaximise() {
        return pastSucksVideo.maximise();
    }

    public boolean youTubePlayOnYouTube() {
        return pastSucksVideo.playOnYouTube();
    }

}
