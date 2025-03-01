package utils;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class VerifyLink {
    // Checks that the url of the page opened up by clicking the  link matches @expectedUrlFragment
    /// change this so I actually open up the link
    public static boolean verifyLink(WebDriver driver, WebElement link, String expectedUrlFragment) {

        String href = link.getDomAttribute("href");
        boolean retVal = false; // only if we open the link and match the resulting url will we return true
        if (href != null) {
            Object[] windowHandles = null; // for switching between tabs. A bit 
            String target = link.getDomAttribute("target");
            ; // might be "_blank", in which case we are opening a new window
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            String startingURL = driver.getCurrentUrl();
            link.click();

            // if we opened a new tab, we have to switch to it. Selenium will open the new tab, but will not switch to it unless we tell it to.
            if ("_blank".equals(target)) {
                windowHandles = driver.getWindowHandles().toArray();
                driver.switchTo().window((String) windowHandles[1]);
            }

            wait.until(ExpectedConditions.not(ExpectedConditions.urlToBe(startingURL)));

            // Now check the url of the page we opened
            if (driver.getCurrentUrl().contains(expectedUrlFragment)) {
                retVal = true;
            }

            // Now close the tab we opened
            if ("_blank".equals(target)) { // i.e. if we opened the link in a new tab
                driver.close();
                //Switch back to the old tab or window
                driver.switchTo().window((String) windowHandles[0]);
            } else {
                driver.navigate().back();
            }
            wait.until(ExpectedConditions.urlToBe(startingURL));
        }
        return retVal;
    }
}
