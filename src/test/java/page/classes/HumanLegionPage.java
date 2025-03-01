package page.classes;

/**
 * todo
 * This class will implement some generics such as page title return, copyright and such like.
 * It is an abstract class that will be inherited/ extended by page specific  classes
 * Such as TimeDogzPage.
 *
 */

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import utils.VerifyLink;
import java.util.List;

import java.time.Duration;
import static utils.Constants.COPYRIGHT_STATEMENT;

abstract public class HumanLegionPage {

    protected WebDriver driver;

    public HumanLegionPage(WebDriver driver) {
        this.driver = driver;
    }

    // All the pages should have the same footer.
    // Verify that we have the correct copyright information and the social media links are active
    // There's a lot more going on in the footer and this could be expanded to include search and
    // newletter signup, for example.
    public boolean verifyFooter() {
        boolean retVal = true;

        // First find the footer and scroll to it. All the element finding will take place within this footer
        Actions actions = new Actions(driver);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(8));
        WebElement footer = driver.findElement(By.cssSelector("footer#Footer")); //ie footer tag with id of 'Footer'
        actions.moveToElement(footer).perform();
        wait.until(ExpectedConditions.visibilityOf(footer));

        // Now check for the copyright message, which we should find in utils.constants.
        WebElement copyrightDiv = footer.findElement(By.className("copyright")); // Could also use By.cssSelector("div.copyright")
        if (!copyrightDiv.getText().contains(COPYRIGHT_STATEMENT)) {
            retVal = false;
        } else {
            // Now check social media links
            WebElement socialMediaList = footer.findElement(By.cssSelector("ul.social"));
            List<WebElement> socialMediaLinks = socialMediaList.findElements(By.tagName("li"));
            String MediaType = "";
            WebElement linkAnchor;
            for (WebElement link : socialMediaLinks) {
                if (retVal && (link.isDisplayed() || link.isEnabled())) {
                    linkAnchor = link.findElement(By.tagName("a"));
                    MediaType = link.getDomAttribute("class");

                    switch (MediaType) {
                        case "facebook":
                            retVal = VerifyLink.verifyLink(driver, linkAnchor, "facebook.com/HumanLegion");
                            break;
                        case "twitter":
                            retVal = VerifyLink.verifyLink(driver, linkAnchor, "x.com/TheHumanLegion");
                            break;
                        case "custom":
                            retVal = (linkAnchor.getDomAttribute("href").contains("goodreads.com")) &&
                                    (VerifyLink.verifyLink(driver, linkAnchor, "Tim_C_Taylor"));
                            break;
                        case "rss":
                            retVal = VerifyLink.verifyLink(driver, linkAnchor, "humanlegion.com/feed/");
                            break;
                        default:
                            System.out.println("Unknown social media link: " + MediaType); // Don't class this as an issue, though
                    }
                }
            }
        }

        return retVal;
    }
}
