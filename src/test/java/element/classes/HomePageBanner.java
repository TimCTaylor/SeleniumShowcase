/** HomePageBanner.java
 *  This class encapuslates the banners on the humanlegion.com homepage.
 *  It is an example of the element abstraction layer that achieves the following:
 *  - Encapsulates the details of how we interract with the banner elements on the homepage. This
 *    abstracts the generic webelement into a simpler set of methods and members that are specific
 *    to writing tests on the humanlegion.com homepage.
 *  - And, of course, we're doing basic good refactoring by separating out what would otherwise be
 *    repeated code (DRY principle) and making the test code more readable and maintainable.
 * 
 *  Selenium/ Java techniques demonstrated:
 *  Use of window handles 
 *  Java encapsulation
 * 
 */

package element.classes;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import static utils.Constants.LEGION_BASE_URL;

public class HomePageBanner {
    WebElement bannerDiv; // The div that wraps the banner elements
    WebDriver driver;
    String homePageHandle, targetPageHandle; // The handles of the homepage window and the target after clicking the banner

    // Constructor
    // @bannerID - the ID of the div created by BeBuilder to wrap the banner
    // elements.
    public HomePageBanner(WebDriver driver, String bannerID) {
        this.driver = driver;
        this.bannerDiv = driver.findElement(By.id(bannerID));
        targetPageHandle = null;
        homePageHandle = driver.getWindowHandle();

    }

    public boolean onHomePage() {
        return homePageHandle.equals(driver.getWindowHandle());
    }

    public boolean onTargetPage() {
        return (targetPageHandle != null) && targetPageHandle.equals(driver.getWindowHandle());
    }

    public boolean scrollToBanner() {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", bannerDiv);
        return true; // test
    }

    // If we are on the home page, clicks the banner and returns true if target page
    // opens successfully. Otherwise returns false.
    public boolean clickBanner() {
        boolean retVal = false;
        if (homePageHandle.equals(driver.getWindowHandle())) {
            bannerDiv.findElement(By.tagName("a")).click();
            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.not(ExpectedConditions.urlToBe(LEGION_BASE_URL))); // wait for the new
                                                                                                                                                  // page to load
            targetPageHandle = driver.getWindowHandle();
            retVal = true;
        }
        return retVal;
    }

    public String getBannerImageName() {
        String retVal = "Error";
        WebElement imageDiv = bannerDiv.findElement(By.className("image_wrapper"));
        WebElement imageTag = imageDiv.findElement(By.tagName("img"));
        if (imageTag != null) {
            String sourceFile = imageTag.getDomAttribute("src");
            if (sourceFile != null) {
                retVal = sourceFile.substring(sourceFile.lastIndexOf("/") + 1); // extract the filename at the end of
                                                                                // the url
            }
        }
        return retVal;
    }

    // returns true if we were on the target window and if closing it returns us to
    // the home page. In all other cases, returns false.
    public boolean closeTargetWindow() {
        boolean retVal = false;
        if (onTargetPage()) {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            driver.navigate().back();
            wait.until(ExpectedConditions.urlToBe(LEGION_BASE_URL)); // wait for the home page to reload
            retVal = true;
        }
        return retVal;
    }
}
