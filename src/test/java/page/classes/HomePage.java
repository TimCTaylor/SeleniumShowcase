package page.classes;

/** A page object for encapsulating tests specific to the website homepage.
 
 */

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import element.classes.HomePageBanner;
import element.classes.YouTubeVideo;
import element.classes.booktab.AmazonSalesLinks;
import element.classes.booktab.BookTab;
import utils.VerifyLink;

import java.util.List;

import static utils.Constants.AVOID_OPENING_AMAZON_LINKS;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class HomePage extends HumanLegionPage {
    private Actions actions = new Actions(driver);;
    private WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(8));

    public HomePage(WebDriver driver) {
        super(driver);

    }

    // @bannerId is the ID of the book tab container element (a <section> element).
    // We find the container, then instantiate a HomePageBanner object to represent the banner inside the container.
    // We return the HomePageBanner object or null if anything went wrong.
    public HomePageBanner getBanner(String bannerId) {
        Actions actions = new Actions(driver);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(8));
        HomePageBanner retVal = null;
        WebElement banner = driver.findElement(By.id(bannerId));
        if (banner != null) {
            actions.moveToElement(banner).perform();
            wait.until(ExpectedConditions.visibilityOf(banner));
            retVal = new HomePageBanner(driver, bannerId);
        }
        return retVal;
    }

    public boolean followBannerLink(HomePageBanner banner) {
        boolean retVal = false;
        if (banner.onHomePage()) {
            if (banner.clickBanner()) {
                if (banner.onTargetPage()) {
                    retVal = true;
                }
            }
        }
        return retVal;
    }

    public boolean returnToHomePage(HomePageBanner banner) {
        boolean retVal = false;
        if (banner.closeTargetWindow()) { // closeTargetWindow() returns true if we were on the target page and then successfully return to the home page
            retVal = true;
        }
        return retVal;
    }
}
