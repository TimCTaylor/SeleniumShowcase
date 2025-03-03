/** TestLegionSeriesPage.java
 * Purpose: JUnit tests for the LegionSeriesPage class, which is our page object to abstract the Legion Series web page.
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
import page.classes.LegionSeriesPage;
import utils.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static utils.Constants.LEGION_SERIES_URL;

public class TestLegionSeriesPage {
    private static TestSession testSession;
    private static WebDriver driver;
    private static LegionSeriesPage legionSeriesPage;

    @BeforeAll
    public static void testSetup() {
        testSession = new TestSession();
        driver = testSession.sessionDriver;
    }

    @BeforeEach
    public void openLegionHomepage() {
        driver.get(LEGION_SERIES_URL); // All our tests for this class will start at the Human Legion book series page
        legionSeriesPage = new LegionSeriesPage(driver);
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
    public void testLegionFooter() {
        assertTrue(legionSeriesPage.verifyFooter()); // An example of a method inherited from the abstract page class
    }

    /* Run a set of tests for each book to verify the Amazon sales links. */

    @Test
    // Follow all the Amazon links for this book and check they are on sale at the Amazon store front
    public void verifyMarineCadetAmazonLinks() {
        BookTab marineCadetTab = legionSeriesPage.getBookTab("MarineCadet");
        if (marineCadetTab != null) {
            assertTrue(legionSeriesPage.verifyAmazonLinks(marineCadetTab, "Links", "Marine Cadet"),
                    "Legion Series Page: Amazon links for Marine Cadet broken or book not on sale at Amazon store.");
        }
    }

    @Test
    public void verifyIndigoSquadAmazonLinks() {
        BookTab indigoSquadTab = legionSeriesPage.getBookTab("IndigoSquad");
        if (indigoSquadTab != null) {
            assertTrue(legionSeriesPage.verifyAmazonLinks(indigoSquadTab, "Links", "Indigo Squad"),
                    "Legion Series Page: Amazon links for Indigo Squad broken or book not on sale at Amazon store.");
        }
    }

    @Test
    public void verifyRenegadeLegionAmazonLinks() {
        BookTab renegadeLegionTab = legionSeriesPage.getBookTab("RenegadeLegion");
        if (renegadeLegionTab != null) {
            assertTrue(legionSeriesPage.verifyAmazonLinks(renegadeLegionTab, "Links", "Renegade Legion"),
                    "Legion Series Page: Amazon links for Renegade Legion broken or book not on sale at Amazon store.");
        }
    }

    @Test
    public void verifyHumanEmpireAmazonLinks() {
        BookTab humanEmpireTab = legionSeriesPage.getBookTab("HumanEmpire");
        if (humanEmpireTab != null) {
            assertTrue(legionSeriesPage.verifyAmazonLinks(humanEmpireTab, "Links", "Human Empire"),
                    "Legion Series Page: Amazon links for Human Empire broken or book not on sale at Amazon store.");
        }
    }

    @Test
    public void verifyWATWKAmazonLinks() {
        BookTab wATWKTab = legionSeriesPage.getBookTab("WarAgainst");
        if (wATWKTab != null) {
            assertTrue(legionSeriesPage.verifyAmazonLinks(wATWKTab, "Links", "War Against the White Knights"),
                    "Legion Series Page: Amazon links for War Against the White Knights broken or book not on sale at Amazon store.");
        }
    }

    @Test
    public void verifyBattleofEarthPt1Links() {
        BookTab battleofEarthPt1Tab = legionSeriesPage.getBookTab("TBOE1");
        if (battleofEarthPt1Tab != null) {
            assertTrue(legionSeriesPage.verifyAmazonLinks(battleofEarthPt1Tab, "How to buy", "Battle of Earth"),
                    "Legion Series Page: Amazon links for Battle of Earth Pt1 broken or book not on sale at Amazon store.");
        }
    }

    @Test
    public void verifyBattleofEarthPt2Links() {
        BookTab battleofEarthPt2Tab = legionSeriesPage.getBookTab("TBOE2");
        if (battleofEarthPt2Tab != null) {
            assertTrue(legionSeriesPage.verifyAmazonLinks(battleofEarthPt2Tab, "How to buy", "Battle of Earth"),
                    "Legion Series Page: Amazon links for Battle of Earth Pt2 broken or book not on sale at Amazon store.");
        }
    }

    @Test
    public void verifyRealityWarLinks() {
        BookTab realityWarTab = legionSeriesPage.getBookTab("RealityWar");
        if (realityWarTab != null) {
            assertTrue(legionSeriesPage.verifyAmazonLinks(realityWarTab, "Links", "Reality War"),
                    "Legion Series Page: Amazon links for the Reality War broken or book not on sale at Amazon store.");
        }
    }

    /* Run a set of tests for each book to verify the tabs work correctly and serve the right content. 
     * This demonstrates how we can encapuslate the complexities of the book tab and place it in our element abstraction layer
     * so that we can leave the tests themselves cleaner and less likely to require maintenance.
    */

    @Test
    public void verifyRenegadeLegionTabs() {
        BookTab marineCadetTab = legionSeriesPage.getBookTab("RenegadeLegion");
        assertNotNull(marineCadetTab, "Renegade Legion book tab not found on Legion Series page.");
    }

    @Test
    public void verifyMarineCadetTabs() {
        String expectedText, messageText;
        BookTab marineCadetTab = legionSeriesPage.getBookTab("MarineCadet");
        assertNotNull(marineCadetTab, "Marine Cadet book tab not found on Legion Series page.");

        assertTrue(legionSeriesPage.selectTab(marineCadetTab, "Try the audio"),
                "Legion Series Page: Could not select 'Try the audio' tab on Marine Cadet book tab.");
        expectedText = "Marine Cadet is available as an audio book produced by Tantor Media, the largest independent audiobook publisher in the US.";
        messageText = "Legion Series Page: Marine Cadet audio book tab does not contain expected text.";
        assertTrue(legionSeriesPage.getPanelText(marineCadetTab).contains(expectedText), messageText);

        assertTrue(legionSeriesPage.selectTab(marineCadetTab, "File Book Under"),
                "Legion Series Page: Could not select 'File Book Under' tab on Marine Cadet book tab.");
        expectedText = "Gender-obsessed aliens";
        messageText = "Legion Series Page: Marine Cadet 'file book under' tab does not contain expected text.";
        assertTrue(legionSeriesPage.getPanelText(marineCadetTab).contains(expectedText), messageText);

        messageText = "Legion Series Page: Marine Cadet book tab does not contain expected number of tabs.";
        assertEquals(6, legionSeriesPage.getTabCount(marineCadetTab), messageText);
    }

    // Let's add a few more tests. we would be more comprehensive in a real test suite, but this is just a demo.
    //
    // Note: the tests below failed at first. The reason was the large amount of text in the panels for this book
    // meant that an element.click() call was being intercepted by the main menu overlay.
    // In other words, the intent of the test was correct, but its implementation inside the booktab package, part of
    // the element abstraction layer, needed to be enhanced to address this.
    // All the necessary changes were made in the element abstraction layer, where they belonged. No changes were necessary
    // to the tests themseleves or to the page object model.
    @Test
    public void verifyBattleofEarth2Tabs() {
        String expectedText, messageText;
        BookTab bOE2Tab = legionSeriesPage.getBookTab("TBOE2");
        assertNotNull(bOE2Tab, "Battle of Earth Pt2 book tab not found on Legion Series page.");

        assertTrue(legionSeriesPage.selectTab(bOE2Tab, "Other tales of Hardit-Occupied Earth"),
                "Legion Series Page: Could not select 'Other tales of Hardit-Occupied Earth' tab on Battle of Earth Pt2 book tab.");
        expectedText = "The Battle of Cairo is a novelette set between War Against the White Knights and The Battle of Earth Part1: Endgame";
        messageText = "Legion Series Page: Battle of Earth Pt2 'Other tales of Hardit-Occupied Earth' tab does not contain expected text.";
        assertTrue(legionSeriesPage.getPanelText(bOE2Tab).contains(expectedText), messageText);

        assertTrue(legionSeriesPage.selectTab(bOE2Tab, "Endings and beginnings..."),
                "Legion Series Page: Could not select 'Endings and beginnings…' tab on Battle of Earth Pt2 book tab.");
        expectedText = "The Legion’s campaign of liberation ends here… one way or another";
        messageText = "Legion Series Page: Battle of Earth Pt2 Endings and beginnings book tab does not contain expected text.";
        assertTrue(legionSeriesPage.getPanelText(bOE2Tab).contains(expectedText), messageText);

        assertTrue(legionSeriesPage.selectTab(bOE2Tab, "File Book Under"),
                "Legion Series Page: Could not select 'File Book Under' tab on Battle of Earth Pt2 book tab.");
        expectedText = "Daring commando raids... beneath the White House.";
        messageText = "Legion Series Page: Battle of Earth Pt2 'File Book Under' tab does not contain expected text.";
        assertTrue(legionSeriesPage.getPanelText(bOE2Tab).contains(expectedText), messageText);

        messageText = "Legion Series Page: Battle of Earth Pt2 book tab does not contain expected number of tabs.";
        assertEquals(4, legionSeriesPage.getTabCount(bOE2Tab), messageText);
    }
}