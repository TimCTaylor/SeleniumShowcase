package utils;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import element.classes.HomePageBanner;
import element.classes.YouTubeVideo;
import element.classes.booktab.AmazonSalesLinks;
import element.classes.booktab.BookTab;
import utils.*;
import java.util.List;

import java.time.Duration;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static utils.Constants.COPYRIGHT_STATEMENT;

public class VerifyLink {
    // Checks that the url of the page opened up by clicking the  link matches @expectedUrlFragment
    /// change this so I actually open up the link
    public static boolean verifyLink(WebElement link, String expectedUrlFragment) {
        String href = link.getDomAttribute("href");
        return (href != null && href.contains(expectedUrlFragment));
    }

    // Object[] windowHandles; // for switching between tabs        
    // String target = ""; // save the target attribute of each link so we know how to return to the previous page
    // WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    // String startingURL = driver.getCurrentUrl();for(
    // WebElement link:salesLinks)
    // {
    //     target = link.getDomAttribute("target");
    //     System.out.println("Attempting to follow link: " + link.getText() + " | " + link.getDomAttribute("href")
    //             + " | " + target);
    //     link.click();

    //     // if we opened a new tab, we have to switch to it. Selenium will open the new tab, but will not switch to it unless we tell it to.
    //     windowHandles = driver.getWindowHandles().toArray();
    //     driver.switchTo().window((String) windowHandles[1]);

    //     wait.until(ExpectedConditions.not(ExpectedConditions.urlToBe(startingURL)));

    //     if (driver.getCurrentUrl().contains("amazon")) { // only continue if this is an amazon link
    //         WebElement productTitleElement = wait
    //                 .until(ExpectedConditions.presenceOfElementLocated(By.id("productTitle")));
    //         if (!productTitleElement.getText().contains(bookTitle)) {
    //             retVal = false;
    //             break;
    //         }
    //     }

    //     TestSession.sleep(5); // wait for a few seconds to avoid annoying Amazon bot detectors
    //     if ("_blank".equals(target)) { // i.e. if we opened the link in a new tab
    //         driver.close();
    //         //Switch back to the old tab or window
    //         driver.switchTo().window((String) windowHandles[0]);
    //     } else {
    //         driver.navigate().back();
    //     }
    //     wait.until(ExpectedConditions.urlToBe(startingURL));

}
