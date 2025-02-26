package element.classes.booktab;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;

import java.time.Duration;
import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AmazonSalesLinks {

    private WebDriver driver; // passed via constructor
    private List<WebElement> salesLinks; // The list of sales links for the book tab.
    private String bookTitle; // The title of the book. We will search for this text on the target page.

    // Constructor
    // @tabPanel - the WebElement that represents the tab panel for which we are going to extract <a> tag links
    // @bookTitle - the title of the book. When we follow the links, we will search for this text on the target page. If the sales link is broken, it won't be present.
    public AmazonSalesLinks(WebDriver driver, WebElement tabPanel, String bookTitle) {
        this.driver = driver;
        this.bookTitle = bookTitle;
        salesLinks = tabPanel.findElements(By.tagName("a"));
    }

    public int getNumLinks() {
        return salesLinks.size();
    }

    public boolean verifyLinks() { // perhaps maintain this as an export - with modes of SQL and json
        // logic
        // loop through the arraylist of WebElements, click each one, and check if the target page contains the book title. navigate back

        //for now... let's just print them out.

        // TODO
        // I need to check the target link
        // if open in a new tab then I driver.close() otherwise I navigate back. Each link could have a different value.

        boolean retVal = true;
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        String startingURL = driver.getCurrentUrl();
        for (WebElement link : salesLinks) {
            link.click();
            wait.until(ExpectedConditions.not(ExpectedConditions.urlToBe(startingURL)));

            if (driver.getCurrentUrl().contains("amazon")) { // only continue if this is an amazon link
                WebElement productTitleElement = wait
                        .until(ExpectedConditions.presenceOfElementLocated(By.id("productTitle")));
                if (!productTitleElement.getText().contains(bookTitle)) {
                    retVal = false;
                    break;
                }
            }
            driver.navigate().back(); // or close... 
            wait.until(ExpectedConditions.urlToBe(startingURL));
            //logic
            // check if the target url contains AMAZON
            // check if the target page contains the book title text

        }
        return retVal;
    }

    public boolean getLinks() { // perhaps maintain this as an export - with modes of SQL and json and print as string
        // logic
        // loop through the arraylist of WebElements, click each one, and check if the target page contains the book title. navigate back

        //for now... let's just print them out.

        System.out.println("Printing sales links for " + bookTitle);
        for (WebElement link : salesLinks) {
            System.out.println(link.getText() + " | " + link.getDomAttribute("href"));
        }
        return true;
    }

}
