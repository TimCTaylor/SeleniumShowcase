/**
 * TabHeader.java
 * Implements the tabs within a BookTab object that we select to choose the TabSection content to display.
 */

package element.classes.booktab;

import org.openqa.selenium.WebElement;

public class TabHeader {

    private WebElement tabElement; // The anchor tag for the tab header. Passed via constructor.
    private WebElement tabPanel; // The div that contains the tab's content. Set by the BookTab constructor.
    private String tabText;

    // Constructor
    // @tabElement - the WebElement that represents the tab header's anchor tag
    // @tabText - the text of the tab header anchor tag
    public TabHeader(WebElement tabElement, String tabText) {
        this.tabElement = tabElement;
        this.tabText = tabText;
    }

    public WebElement getTabElement() {
        return tabElement;
    }

    public String getTabText() {
        return tabText;
    }

    public String getTabPanelText() {
        return tabPanel.getText();
    }

    public void setTabPanel(WebElement tabPanel) {
        this.tabPanel = tabPanel;
    }

    public WebElement getTabPanel() {
        return tabPanel;
    }

    public boolean selectTab() {
        tabElement.click();
        // TODO logic to check if the tab was selected
        return true;
    }
}
