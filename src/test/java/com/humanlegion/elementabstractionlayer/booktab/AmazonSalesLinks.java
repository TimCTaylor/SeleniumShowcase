package com.humanlegion.elementabstractionlayer.booktab;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;

import static com.humanlegion.utils.Constants.AVOID_OPENING_AMAZON_LINKS;

import java.time.Duration;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.humanlegion.utils.TestSession;

public class AmazonSalesLinks {

    private WebDriver driver; // passed via constructor
    private List<WebElement> salesLinks; // The list of sales link elements for the book tab.
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

    // Loop through all the amazon sales links and verify that the target page contains the book title. If the sales link is 
    // broken (as has happened in real life), or Amazon has removed the title from its store front, then the link will open
    // one of Amazon's 404 pages and the book title will not be displayed.
    public boolean verifyLinks() {
        boolean retVal = true;
        Object[] windowHandles; // for switching between tabs        
        String target = ""; // save the target attribute of each link so we know how to return to the previous page
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        String startingURL = driver.getCurrentUrl();
        if (AVOID_OPENING_AMAZON_LINKS) {
            retVal = true;
        } else {
            for (WebElement link : salesLinks) {
                target = link.getDomAttribute("target");
                System.out.println("Attempting to follow link: " + link.getText() + " | " + link.getDomAttribute("href")
                        + " | " + target);
                link.click();
                windowHandles = driver.getWindowHandles().toArray(); // for switching between tabs. A bit wasteful as we might not need it, but the code won't compile unless we initialise it.

                // if we opened a new tab, we have to switch to it. Selenium will open the new tab, but will not switch to it unless we tell it to.
                if ("_blank".equals(target)) {
                    driver.switchTo().window((String) windowHandles[1]);
                }

                wait.until(ExpectedConditions.not(ExpectedConditions.urlToBe(startingURL)));

                if (driver.getCurrentUrl().contains("amazon")) { // only continue if this is an amazon link
                    WebElement productTitleElement = wait
                            .until(ExpectedConditions.presenceOfElementLocated(By.id("productTitle")));
                    if (!productTitleElement.getText().contains(bookTitle)) {
                        retVal = false;
                        break;
                    }
                }

                TestSession.sleep(5); // wait for a few seconds to avoid annoying Amazon bot detectors
                if ("_blank".equals(target)) { // i.e. if we opened the link in a new tab
                    driver.close();
                    //Switch back to the old tab or window
                    driver.switchTo().window((String) windowHandles[0]);
                } else {
                    driver.navigate().back();
                }
                wait.until(ExpectedConditions.urlToBe(startingURL));
            }
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
