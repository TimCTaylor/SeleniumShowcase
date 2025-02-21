
import  static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
//import static org.junit.Assert.*;

// we don't need this in the same package as the jupiter ones    import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

//import static org.junit.jupiter.api.Assertions.assertTrue;
//import org.junit.Test;




public class CheckSauceLabsHomePageTest {

    @Disabled("Not part of core Human Legion test suite.")
    @Test
    public void withoutPageObjects(){
        ChromeOptions options = new ChromeOptions();
        options.setBinary("D:\\Coding\\Selenium\\chrome-win64\\chrome.exe");
        WebDriver driver = new ChromeDriver(options);

        driver.get("https://eviltester.github.io/supportclasses/#2000");
        final WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(3));

        WebElement singleSelectMenu = wait.
                until(ExpectedConditions.
                        visibilityOfElementLocated(By.id("select-menu")));

        final Select select = new Select(singleSelectMenu);
        select.selectByVisibleText("Option 2");

        final By messageLocator = By.id("message");
        wait.until(ExpectedConditions.visibilityOfElementLocated(messageLocator));

        Assertions.assertEquals("Received message: selected 2",
                driver.findElement(messageLocator).getText());
    }



    @Test
    public void site_header_is_on_home_page() {
//        WebDriver browser = new ChromeDriver();

        ChromeOptions options = new ChromeOptions();
        options.setBinary("D:\\Coding\\Selenium\\chrome-win64\\chrome.exe");
        WebDriver browser = new ChromeDriver(options);
        browser.get("https://www.google.com");

        // below lines will click on Accept all button of the pop-up
        WebElement GoogleAcceptAll = browser.findElement(By.xpath ("//div[text()='Accept all']"));
        GoogleAcceptAll.click();

        browser.manage().timeouts().implicitlyWait(Duration.ofSeconds(4));
        try {
            System.out.println("I got here...");
            WebElement element = browser.findElement(By.xpath("//*[@id='APjFqb']"));
            // Enter something to search for
            assertNotNull(element, "Element should be found on the page.");
//            assertNull(element, "Element should be found on the page."); /// force an exception to see that I've handled it right
            browser.manage().timeouts().implicitlyWait(Duration.ofSeconds(4));
        } catch (Exception e) {
            fail("Element not found: " + e.getMessage());
        } finally {
            browser.manage().timeouts().implicitlyWait(Duration.ofSeconds(4));
            browser.quit();
        }
    }
}