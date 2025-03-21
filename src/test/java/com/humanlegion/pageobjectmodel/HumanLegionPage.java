package com.humanlegion.pageobjectmodel;

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

import com.humanlegion.elementabstractionlayer.booktab.AmazonSalesLinks;
import com.humanlegion.elementabstractionlayer.booktab.BookTab;
import com.humanlegion.utils.VerifyLink;

import java.util.List;

import static com.humanlegion.utils.Constants.COPYRIGHT_STATEMENT;

import java.time.Duration;

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
                            // retVal = VerifyLink.verifyLink(driver, linkAnchor, "facebook.com/HumanLegion");
                            retVal = true; // The Facebook check works, but I don't want to prompt Meta to say there has been unusual activity on my account.
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

    // @bookTabId is the ID of the book tab container element (a <section> element).
    // We find the container, then instantiate a BookTab object to represent the book tab inside the container.
    // We return the BookTab object or null if anything went wrong.
    public BookTab getBookTab(String bookTabId) {
        Actions actions = new Actions(driver);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(8));
        BookTab retVal = null;
        WebElement bookTab = driver.findElement(By.id(bookTabId));
        if (bookTab != null) {
            actions.moveToElement(bookTab).perform();
            wait.until(ExpectedConditions.visibilityOf(bookTab));
            retVal = new BookTab(bookTabId, driver);
        }
        return retVal;
    }

    // Finds all amazon store links on the tab with name of @nameOfLinksTab and verifies they are working.
    // It uses @nameOfLinksTab to select the right tab (they don't all have the same name) and @bookTitle as the
    // name of the book, which it uses as part of the verification that the Amazon link sells the correct book.
    public boolean verifyAmazonLinks(BookTab bookTab, String nameOfLinksTab, String bookTitle) {
        boolean retVal = false;
        if (bookTab.selectTabByText(nameOfLinksTab)) { // There's a lot going on in the next two lines, but it's all abstracted away.
            AmazonSalesLinks bookLinks = new AmazonSalesLinks(driver, bookTab.getSelectedTabPanel(), bookTitle);
            retVal = bookLinks.verifyLinks();
        }
        return retVal;
    }

    public boolean selectTab(BookTab bookTab, String tabName) {
        return bookTab.selectTabByText(tabName);
    }

    public String getPanelText(BookTab bookTab) {
        return bookTab.getSelectedTabPanelText();
    }

    public int getTabCount(BookTab bookTab) {
        return bookTab.getNumTabs();
    }
}
