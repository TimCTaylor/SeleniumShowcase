/** Test4HUSeriesPage.java
 * Purpose: JUnit tests for the FourHorsemenSeriesPage class, which is our page object to abstract the Four Horsemen Series web page.
 * (which is often abbreviated to 4HU, although we can't start a Java identifier with a numeric)
 * 
 * Highlight: The page demonstrates how to combine the page object model with the element abstraction layer.
 * 
 * At the core of the webpage are a series of book tabs, one for each title.
 * 
 * The tabs themselves are a complex set of elements, so I created the element.class.booktab package to abstract them as
 * part of my element abstraction layer. The BookTab class is the main class in that package, and the highest priority test
 * is to verify that all Amazon sales links within each booktab correctly open onto the Amazon store front and that the title
 * is actively on sale.
 * 
 * The design approach is as follows:
 *    - The detailed mechanics of interacting with the book tabs on the web page are handled by the booktab package.
 *    - The details of how to interract with the booktab objects are handled as far as possible by the page object (LegionSeriesPage instance).
 *    - In the test class itself, the intent of the tests should be clear, precise, and simple, having abstracted away the implementation
 *      gubbins. It is close but not perfect, because I stop at the point where that incremental next level of abstraction leads to a 
 *      significantly increased complexity of the abstracted layers. In other words, design patterns can be extremely helpful, but you
 *      shouldn't follow them off a cliff -- for example, because the abstracted layers need to be maintained too, and probably more than
 *      the test layer, and being overly complex and obtuse can be counterproductive.
 * 
 */

package junit.tests;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;

import element.classes.booktab.BookTab;
import page.classes.FourHorsemenSeriesPage;
import utils.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static utils.Constants.HORSEMEN_SERIES_URL;

public class Test4HUSeriesPage {
    private static TestSession testSession;
    private static WebDriver driver;
    private static FourHorsemenSeriesPage fourHorsemenSeriesPage;

    @BeforeAll
    public static void testSetup() {
        testSession = new TestSession();
        driver = testSession.sessionDriver;
    }

    @BeforeEach
    public void open4HUHomepage() {
        driver.get(HORSEMEN_SERIES_URL); // All our tests for this class will start at the 4HU book series page
        fourHorsemenSeriesPage = new FourHorsemenSeriesPage(driver);
    }

    @AfterEach
    public void closeLastPage() {
        for (String handle : driver.getWindowHandles()) {
            driver.switchTo().window(handle);
            if (driver.getWindowHandles().size() > 1) {
                driver.close();
            }
        }
    }

    @AfterAll
    public static void testTeardown() {
        testSession.closeTestSession();
    }

    @Test
    public void test4HUFooter() {
        assertTrue(fourHorsemenSeriesPage.verifyFooter()); // An example of a method inherited from the abstract page class
    }

    /* Run a set of tests for each book to verify the Amazon sales links.
     * The tests themselves seem almost trivial, but the complexity is abstracted away into the combination of the page object model
     * and the element abstraction layer (specifically, the booktab package).
     */

    @Test
    public void verifyMidnightSunAmazonLinks() {
        BookTab midnightSunTab = fourHorsemenSeriesPage.getBookTab("FHU1");
        if (midnightSunTab != null) {
            assertTrue(fourHorsemenSeriesPage.verifyAmazonLinks(midnightSunTab, "Preview & Purchase Links",
                    "Midnight Sun"),
                    "4HU Series Page: Amazon links for Midnight Sun broken or book not on sale at Amazon store.");
        }
    }

    @Test
    public void verifyEndlessNightAmazonLinks() {
        BookTab endlessNightTab = fourHorsemenSeriesPage.getBookTab("FHU2");
        if (endlessNightTab != null) {
            assertTrue(fourHorsemenSeriesPage.verifyAmazonLinks(endlessNightTab, "Preview & Purchase Links",
                    "Endless Night"),
                    "4HU Series Page: Amazon links for Endless Night broken or book not on sale at Amazon store.");
        }
    }

    @Test
    public void verifyTheDarkBeforetheAmazonLinks() {
        BookTab theDarkBeforetheLightTab = fourHorsemenSeriesPage.getBookTab("FHU3");
        if (theDarkBeforetheLightTab != null) {
            assertTrue(fourHorsemenSeriesPage.verifyAmazonLinks(theDarkBeforetheLightTab, "Preview & Purchase Links",
                    "The Dark Before the Light"),
                    "4HU Series Page: Amazon links for The Dark Before the Light broken or book not on sale at Amazon store.");
        }
    }

    @Test
    public void verifyOneMinuteToMidnightAmazonLinks() {
        BookTab oneMinuteToMidnightTab = fourHorsemenSeriesPage.getBookTab("FHU4");
        if (oneMinuteToMidnightTab != null) {
            assertTrue(fourHorsemenSeriesPage.verifyAmazonLinks(oneMinuteToMidnightTab, "Preview & Purchase Links",
                    "One Minute to Midnight"),
                    "4HU Series Page: Amazon links for One Minute to Midnight broken or book not on sale at Amazon store.");
        }
    }

    @Test
    public void verifyForAFewCreditsMoreAmazonLinks() {
        BookTab forAFewCreditsMoreTab = fourHorsemenSeriesPage.getBookTab("FHU6");
        if (forAFewCreditsMoreTab != null) {
            assertTrue(fourHorsemenSeriesPage.verifyAmazonLinks(forAFewCreditsMoreTab, "Preview & Purchase Links",
                    "For a Few Credits More"),
                    "4HU Series Page: Amazon links for For a Few Credits More broken or book not on sale at Amazon store.");
        }
    }

    @Test
    public void verifyTalesFromTheLyonsDenAmazonLinks() {
        BookTab talesFromTheLyonsDenTab = fourHorsemenSeriesPage.getBookTab("FHU7");
        if (talesFromTheLyonsDenTab != null) {
            assertTrue(fourHorsemenSeriesPage.verifyAmazonLinks(talesFromTheLyonsDenTab, "Preview & Purchase Links",
                    "Tales from the Lyon"),
                    "4HU Series Page: Amazon links for Tales from the Lyon's Den broken or book not on sale at Amazon store.");
        }
    }

    @Test
    public void verifyInTheWingsAmazonLinks() {
        BookTab inTheWingsTab = fourHorsemenSeriesPage.getBookTab("FHU8");
        if (inTheWingsTab != null) {
            assertTrue(fourHorsemenSeriesPage.verifyAmazonLinks(inTheWingsTab, "Preview & Purchase Links",
                    "In the Wings"),
                    "4HU Series Page: Amazon links for In the Wings broken or book not on sale at Amazon store.");
        }
    }

    /* Run a set of tests for each book to verify the tabs work correctly and serve the right content. 
     * This demonstrates how we can encapuslate the complexities of the book tab and place it in our element abstraction layer
     * so that we can leave the tests themselves cleaner and less likely to require maintenance.
     * We've done some more complex tasks in the LegionSeriesPage class, so let's keep it sparse here.
    */
    @Test
    public void verifyOneMinuteToMidnightTabs() {
        String expectedText, messageText;
        BookTab oneMinuteToMidnightTab = fourHorsemenSeriesPage.getBookTab("FHU3");
        assertNotNull(oneMinuteToMidnightTab, "One Minute to Midnight book tab not found on 4HU Series page.");

        messageText = "4HU Series Page: One Minute to Midnight book tab does not contain expected number of tabs.";
        assertEquals(4, fourHorsemenSeriesPage.getTabCount(oneMinuteToMidnightTab), messageText);

        assertTrue(fourHorsemenSeriesPage.selectTab(oneMinuteToMidnightTab, "Description"),
                "4HU Series Page: Could not select 'Description' tab on One Minute to Midnight book tab.");
        expectedText = "As Endless Night stretches across the nebula, those who still resist must dig deep and believe that however dire the situation,"
                + " this is the dark before the light.";
        messageText = "4HU Series Page: One Minute to Midnight Description book tab does not contain expected text.";
        assertTrue(fourHorsemenSeriesPage.getPanelText(oneMinuteToMidnightTab).contains(expectedText), messageText);

        assertTrue(fourHorsemenSeriesPage.selectTab(oneMinuteToMidnightTab, "File Under"),
                "4HU Series Page: Could not select 'File Under' tab on One Minute to Midnight book tab.");
        expectedText = "Military science fiction | Space junk as weapons | Ancient alien artifacts | Underwater shenanigans";
        messageText = "4HU Series Page: One Minute to Midnight 'file under' tab does not contain expected text.";
        assertTrue(fourHorsemenSeriesPage.getPanelText(oneMinuteToMidnightTab).contains(expectedText), messageText);

        assertTrue(fourHorsemenSeriesPage.selectTab(oneMinuteToMidnightTab, "Trivia"),
                "4HU Series Page: Could not select 'Trivia' tab on One Minute to Midnight book tab.");
        expectedText = "Talking of name checking real people, in the dedication I mention Obadiah Jex. My brother was researching "
                + "our family tree and came across this ancestor of ours who lived in Suffolk in the 18th century. It was too good a name to ignore.";
        messageText = "4HU Series Page: One Minute to Midnight 'Trivia' tab does not contain expected text.";
        assertTrue(fourHorsemenSeriesPage.getPanelText(oneMinuteToMidnightTab).contains(expectedText), messageText);

    }
}