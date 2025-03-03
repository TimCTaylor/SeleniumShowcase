package page.classes;

/** A page object for encapsulating tests specific to the Four Horsemen book series page.
 *  This class makes use of element abstraction layer classes in the element.classes package.
 *  It extends the HumanLegionPage class, which is the abstract base class of our page object model.
 *  In accordance with our page oject model, this class does not contain junit tests directly. Rather
 *  it it implements test logic used by test methods in the junit.tests package.
 *  We keep the *intent* of the tests and the assertions themselves in junit.tests package classes and
 *  separate out into these page classes (where useful) the test implementation and the unavoidable tight
 *  coupling to the implementation of the system under test.
 * 
 *  Note to self for writing further page classes: plan the test, write the intent of the test in the junit tests
 *  calling stub methods in the page class, and make those stubs real as we go along.
 * 
 *  =How we encasulate the BookTab class.=
 *  The BookTab class is sufficiently complex that I don't want the tests to be calling its methods directly. Separation
 *  of course, also is important in its own right for the reasons I've just given above. However, there is a problem
 *  becuase I need to instantiate a booktab object so the tests can interract with it by clicking the various tabs and
 *  allowing the object to remember what state it is currently in.
 * 
 *  So the approach I have taken is for the tests to call a LegionSeriesPage.getBookTab() method that returns a BookTab object. The tests
 *  can then pass this object to the LegionSeriesPage methods. That way, the tests don't need to know anything about how the booktab
 *  class works, only that we need to pass the object we've been given to the LegionSeriesPage methods that do know how to interract.
 */

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import element.classes.YouTubeVideo;
import element.classes.booktab.AmazonSalesLinks;
import element.classes.booktab.BookTab;
import utils.VerifyLink;

import java.util.List;

import static utils.Constants.AVOID_OPENING_AMAZON_LINKS;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class FourHorsemenSeriesPage extends HumanLegionPage {
    private Actions actions = new Actions(driver);;
    private WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(8));

    public FourHorsemenSeriesPage(WebDriver driver) {
        super(driver);

    }

    // // Finds all amazon store links on the tab with name of @nameOfLinksTab and verifies they are working.
    // // It uses @nameOfLinksTab to select the right tab (they don't all have the same name) and @bookTitle as the
    // // name of the book, which it uses as part of the verification that the Amazon link sells the correct book.
    // public boolean verifyAmazonLinks(BookTab bookTab, String nameOfLinksTab, String bookTitle) {
    //     boolean retVal = false;
    //     if (bookTab.selectTabByText(nameOfLinksTab)) { // There's a lot going on in the next two lines, but it's all abstracted away.
    //         AmazonSalesLinks bookLinks = new AmazonSalesLinks(driver, bookTab.getSelectedTabPanel(), bookTitle);
    //         retVal = bookLinks.verifyLinks();
    //     }
    //     return retVal;
    // }

    // public boolean selectTab(BookTab bookTab, String tabName) {
    //     return bookTab.selectTabByText(tabName);
    // }

    // public String getPanelText(BookTab bookTab) {
    //     return bookTab.getSelectedTabPanelText();
    // }

    // public int getTabCount(BookTab bookTab) {
    //     return bookTab.getNumTabs();
    // }
}
