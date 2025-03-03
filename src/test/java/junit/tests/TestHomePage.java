package junit.tests;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static utils.Constants.LEGION_BASE_URL;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

import element.classes.HomePageBanner;
import page.classes.FourHorsemenSeriesPage;
import page.classes.HomePage;
import utils.TestSession;

public class TestHomePage {
    private static TestSession testSession;
    private static WebDriver driver;
    private static HomePage homePage;

    @BeforeAll
    public static void testSetup() {
        testSession = new TestSession();
        driver = testSession.sessionDriver;
    }

    @BeforeEach
    public void openHomepage() {
        driver.get(LEGION_BASE_URL); // All our tests for this class will start at the 4HU book series page
        homePage = new HomePage(driver);
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
    public void testHomePageFooter() {
        assertTrue(homePage.verifyFooter()); // An example of a method inherited from the abstract page class
    }

    @Test
    public void testHumanLegionBanner() {
        HomePageBanner legionBanner = homePage.getBanner("homebanner_Legion");
        assertNotNull(legionBanner);
        assertTrue(homePage.followBannerLink(legionBanner));

        // Insert logic to briefly check the target page has loaded and can be interacted with here

        assertTrue(homePage.returnToHomePage(legionBanner));
    }

    @Test
    public void testTimeDogzBanner() {
        HomePageBanner timeDogzBanner = homePage.getBanner("homebanner_TDogz");
        assertNotNull(timeDogzBanner);
        assertTrue(homePage.followBannerLink(timeDogzBanner));

        // Insert logic to briefly check the target page has loaded and can be interacted with here

        assertTrue(homePage.returnToHomePage(timeDogzBanner));
    }

    @Test
    public void testSleepingLegionBanner() {
        HomePageBanner sleepingLegionBanner = homePage.getBanner("homebanner_SLegion");
        assertNotNull(sleepingLegionBanner);
        assertTrue(homePage.followBannerLink(sleepingLegionBanner));

        // Insert logic to briefly check the target page has loaded and can be interacted with here

        assertTrue(homePage.returnToHomePage(sleepingLegionBanner));
    }

    @Test
    public void testChimeraCompanyBanner() {
        HomePageBanner chimeraCompanyBanner = homePage.getBanner("homebanner_Chimera");
        assertNotNull(chimeraCompanyBanner);
        assertTrue(homePage.followBannerLink(chimeraCompanyBanner));

        // Insert logic to briefly check the target page has loaded and can be interacted with here

        assertTrue(homePage.returnToHomePage(chimeraCompanyBanner));
    }

    @Test
    public void test4HUBanner() {
        HomePageBanner fourHorsemenBanner = homePage.getBanner("homebanner_4HU");
        assertNotNull(fourHorsemenBanner);
        assertTrue(homePage.followBannerLink(fourHorsemenBanner));

        // Insert logic to briefly check the target page has loaded and can be interacted with here

        assertTrue(homePage.returnToHomePage(fourHorsemenBanner));
    }

    @Test
    public void testRevengeSquadBanner() {
        HomePageBanner revengeSquadBanner = homePage.getBanner("homebanner_RSquad");
        assertNotNull(revengeSquadBanner);
        assertTrue(homePage.followBannerLink(revengeSquadBanner));

        // Insert logic to briefly check the target page has loaded and can be interacted with here

        assertTrue(homePage.returnToHomePage(revengeSquadBanner));
    }

    @Test
    public void testAnthologiesBanner() {
        HomePageBanner anthologiesBanner = homePage.getBanner("homebanner_Other");
        assertNotNull(anthologiesBanner);
        assertTrue(homePage.followBannerLink(anthologiesBanner));

        // Insert logic to briefly check the target page has loaded and can be interacted with here

        assertTrue(homePage.returnToHomePage(anthologiesBanner));
    }
}
