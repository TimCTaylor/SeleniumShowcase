/* *
*  BookTab.java
*  Implements the multi-element tab controls for books in the book series pages.
*  Each book will be represented by one BookTab object with and arraylist of
*  TabHeader objects linked to TabSection objects.
* 
**/

package element.classes.booktab;

import java.time.Duration;
import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BookTab {
    WebElement bookTabContainer; // The parent for all WebElements in the book tab. Makes finding sub-elements more efficient.
    WebDriver driver; // passed via constructor

    public ArrayList<TabHeader> tabHeaders; // The list of tab headers for the book tab. 
    private int selectedTab; // The currently selected tab.

    WebDriverWait wait;
    Actions actions;

    // Constructor. @containerId is the ID of the container element for the book tab. All the tabs will be inside this div.
    public BookTab(String containerId, WebDriver driver) {
        String containerSelector = "section#" + containerId;
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        actions = new Actions(driver);
        bookTabContainer = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(containerSelector)));
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

    public int getSelectedTabIndex() {
        return selectedTab;
    }

    // Click on the tab header specified by the parameter @tabText. Returns true if the tab is found and clicked, false otherwise.
    public boolean selectTabByText(String tabText) {
        boolean retVal = false;
        for (TabHeader tab : tabHeaders) {
            if (tab.getTabText().equals(tabText)) {
                tab.selectTab();
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
