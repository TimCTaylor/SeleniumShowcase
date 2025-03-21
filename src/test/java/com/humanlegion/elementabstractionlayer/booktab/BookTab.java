/* *
*  BookTab.java
*  Implements the multi-element tab controls for books in the book series pages.
*  Each book will be represented by one BookTab object with and arraylist of
*  TabHeader objects linked to TabSection objects.
* 
**/

package com.humanlegion.elementabstractionlayer.booktab;

import java.time.Duration;
import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BookTab {
    WebElement bookTabContainer; // The parent for all WebElements in the book tab. Makes finding sub-elements more efficient.
    WebDriver driver; // passed via constructor

    public ArrayList<TabHeader> tabHeaders; // The list of tab headers for the book tab. 
    private int selectedTab; // Index of the currently selected tab.

    WebDriverWait wait;
    Actions actions;

    // Constructor. @containerId is the ID of the container element for the book tab. All the tabs will be inside this div.
    public BookTab(String containerId, WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        actions = new Actions(driver);
        bookTabContainer = wait.until(ExpectedConditions.presenceOfElementLocated(By.id(containerId)));
        actions.moveToElement(bookTabContainer).perform();
        wait.until(ExpectedConditions.visibilityOf(bookTabContainer));
        selectedTab = 0;

        //populate arraylists 
        tabHeaders = new ArrayList<TabHeader>(8);
        WebElement tabList = bookTabContainer.findElement(By.cssSelector("ul[role='tablist']"));
        for (WebElement tabHeaderElement : tabList.findElements(By.tagName("li"))) {
            WebElement linkElement = tabHeaderElement.findElement(By.tagName("a"));
            TabHeader tabHeader = new TabHeader(linkElement, linkElement.getText());
            tabHeaders.add(tabHeader);
        }

        // Loop through all the tab panels (which are div elements) and update the tabHeaders arraylist to include them.
        int i = 0;
        for (WebElement tabPanelElement : bookTabContainer.findElements(By.cssSelector("div[role='tabpanel']"))) {
            tabHeaders.get(i).setTabPanel(tabPanelElement);
            i++;
        }
    }

    public String getSelectedTabText() {
        return tabHeaders.get(selectedTab).getTabText();
    }

    public String getSelectedTabPanelText() {
        return tabHeaders.get(selectedTab).getTabPanelText();
    }

    public WebElement getSelectedTabPanel() {
        return tabHeaders.get(selectedTab).getTabPanel();
    }

    public int getSelectedTabIndex() {
        return selectedTab;
    }

    public int getNumTabs() {
        return tabHeaders.size();
    }

    // Click on the tab header specified by the parameter @tabText. Returns true if the tab is found and clicked, false otherwise.
    // Note that while it returns success or failure, it also sets the selectedTab instance variable to the index of the tab that was clicked.
    // So if we subesequently call methods such as getSelectedTab(), it will return the text of the tab that was 'clicked' in selectTabByText().
    public boolean selectTabByText(String tabText) {
        boolean retVal = false;
        for (TabHeader tab : tabHeaders) {
            if (tab.getTabText().equals(tabText)) {
                actions.moveToElement(tab.getTabElement()).perform();
                wait.until(ExpectedConditions.visibilityOf(tab.getTabElement()));
                // In testing, I found some tabs (with a great deal of text in the panel) were throwing exceptions when I tried to element.click() them.
                // This was because the browser was forcing the tabs to the top of the screen in order to show all the text. The website under test has 
                // an overlay main menu that was intercepting the click, even after a successful moveToElement. I don't believe any Selenium method could
                // fix this. So I used JavascriptExecutor to click the tab instead.
                //
                // This is an example of how the abstraction layers can be useful. A junit test failed, but the fix was made in BookTab class, leaving
                // the junit tests and (in this case) the page object untouched. To belabour the point a little: there was never a fault in the intent of
                // the test, and so the tests themselves did not need to be changed because they are only concerned with the intent of the tests and the 
                // implementation details of how that intent is manifested has been separated out into the page object model and the element abstraction layer. 
                try {
                    tab.selectTab();
                } catch (ElementClickInterceptedException e) {
                    System.out.println("Exception caught while clicking tab: " + e.getMessage());
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", tab.getTabElement());
                }
                selectedTab = tabHeaders.indexOf(tab);
                retVal = true;
                break;
            }
        }
        if (!retVal) {
            System.out.println("Tab not found with text " + tabText + " in book tab with id: "
                    + bookTabContainer.getDomAttribute("id"));
        }
        return retVal;
    }

    // Click on the tab header specified by parameter @index. Returns true if the tab is found and clicked, false otherwise.
    public boolean selectTabByIndex(int index) {
        boolean retVal = false;
        // for (int i = 0; i < tabHeaders.size(); i++) {
        //     if (tabHeaders.get(i).getText().equals(tabText)) {
        //         tabHeaders.get(i).click();
        //         selectedTabHeader = tabHeaders.get(i);
        //         retVal = true;
        //         break;
        //     }
        // }
        return retVal;
    }
}
