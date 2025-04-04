package com.humanlegion.amazonextractor;

import static com.humanlegion.utils.Constants.LEGION_BASE_URL;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.checkerframework.checker.units.qual.s;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import com.humanlegion.elementabstractionlayer.HomePageBanner;
import com.humanlegion.pageobjectmodel.HomePage;
import com.humanlegion.utils.TestSession;

public class TestAmazonPage {
    // To begin with, we're just going to cll a file directly
    private static final String TEST_HTML_FILE = "B0CRQ9ZLDN_Amazon.com_ The Object_ Hard Science Fiction eBook _ Calvert, Joshua T._ Kindle Store_2025-04-03.html";
    private final String TEST_FILE_FOLDER = "D:\\Coding\\AmazonData\\KindleStorePages\\Parsed\\";
    private static TestSession testSession;
    private static WebDriver driver;
    private static HomePage homePage;
    private Actions actions;
    private WebDriverWait wait;

    @BeforeAll
    public static void testSetup() {
        testSession = new TestSession();
        driver = testSession.sessionDriver;

    }

    @BeforeEach
    public void openHomepage() {
        String target = "file:///" + TEST_FILE_FOLDER + TEST_HTML_FILE;
        System.out.println(target);

        driver.get(target);
        homePage = new HomePage(driver);
        actions = new Actions(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(8));
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
    public void testFirstHappyTest() {
        BookRanking bookData = new BookRanking();
        // Locate the div that contains all the elements I want. If we can't find it, then the entire operation needs
        // to abort, because it probably means that Amazon has changed the design of its store front.
        // WebElement detailBulletsWrapper = driver.findElement(By.id("detailBulletsWrapper_feature_div"));

        WebElement detailBulletsWrapper = wait
                .until(ExpectedConditions.presenceOfElementLocated(By.id("detailBulletsWrapper_feature_div")));

        // Assert that the element is not null (i.e., it exists on the page)
        assertNotNull(detailBulletsWrapper,
                "The 'detailBulletsWrapper_feature_div' element should be present on the page.");
        // TODO - if this fails, then abort

        // Locate the <h2> tag with the text "Product details". This is our double check that we've found the right place.
        WebElement productDetailsHeader = detailBulletsWrapper.findElement(By.xpath("//h2[text()='Product details']"));

        // There's no need to repeat the finding of the same element in a real test, but this is me refreshing my Selenium after
        // a few weeks away playing with Pythonic microservices. 

        // Pretty sure we can't get to this using cssSelector. The closest is to do something like the following. It works, but the logic
        // of why it works is not equivalent (it makes different assumptions about the DOM structure).
        // The logic for productDetailsHeaderX is:
        //   Find the first h2 tag in the detailBulletsWrapper div, and then check if its text is "Product details". If the text isn't what we want, then set it to null.
        //   Obviously, I'm just playing around here. 
        WebElement productDetailsHeaderX = driver.findElement(By.cssSelector("#detailBulletsWrapper_feature_div > h2"));
        if (productDetailsHeaderX != null) {
            if (!"Product details".equals(productDetailsHeaderX.getText().trim())) {
                productDetailsHeaderX = null; // I remember the days when I would give my developers a real telling off for writing this line of code!
            }
        }

        // Using XPath with normalize-space() to handle extra spaces
        WebElement productDetailsHeader2 = detailBulletsWrapper
                .findElement(By.xpath("//h2[normalize-space(text())='Product details']"));

        // Using tag name and filtering by text in a loop
        List<WebElement> headers = detailBulletsWrapper.findElements(By.tagName("h2"));
        WebElement productDetailsHeader3 = null;
        for (WebElement header : headers) {
            if ("Product details".equals(header.getText().trim())) {
                productDetailsHeader3 = header;
                break;
            }
        }

        System.out.print("Did I find the same element ... ");
        boolean success = productDetailsHeader.equals(productDetailsHeader2)
                && productDetailsHeader.equals(productDetailsHeader3)
                && productDetailsHeader.equals(productDetailsHeaderX);
        System.out.print((success ? "Yes!" : "No!") + "\n");

        // There's no need to repeat the finding of the same element in a real test, but this is me refreshing my Selenium after
        // a few weeks away playing with Pythonic microservices.

        assertNotNull(productDetailsHeader, "The 'Product details' header should be present on the page.");

        // Okay, now we move back to the real code. We instantiate a BookRanking object and fill it with the appropriate data.
        // The BookRanking class is not just a dumb holder for book data. It is also the correct place to put the logic to parse 
        // the raw data we extract from the html and also to serialize and deserialize it to JSON.
        // In other words, here we will get the data off the page. All the complicated stuff is abstracted away to the BookRanking class.

        try {
            // Note: the /follow-sibling bit means:
            // Get me the first span (from the '[1]'') that follows. This same approach should work for all the product detail area.
            bookData.ASIN = detailBulletsWrapper
                    .findElement(By.xpath("//span[contains(text(), 'ASIN')]/following-sibling::span[1]")).getText()
                    .trim();

            // Bestseller ranks. The html here is a bit tricky.
            // Find the best seller section by text
            WebElement bestSellerSpan = detailBulletsWrapper
                    .findElement(By.xpath("//span[contains(text(), 'Best Sellers Rank')]"));

            // Now find the text node that follows the span that contains Best Sellers Rank. This contains Kindle Store ranking

            // Write this next little bit up in my cheat notes... extracting the text is pretty tricky. The following doesn't work but it is interesting why...
            // The xpath says:
            //                1. Get the span that contains 'Best Sellers Rank'
            //                2. Find all the sibling nodes of any type (the * wildcard means this)
            //                3. Return only the first node in that list (because of the [1] )
            //  But because the text I want isn't in a node, we return the FOLLOWING node (which is the <a> tag with text: See Top 100 in Kindle Store)     
            //
            // I got an xpath expression to work using text() rather than *, but that returned text, and Selenium needs it to return a webelement
            // So I'll try a different route.
            // bookData.overallRanking = BookRanking.getRankingFromText(
            //         detailBulletsWrapper
            //                 .findElement(By.xpath(
            //                         "//span[contains(text(), 'Best Sellers Rank')]/following-sibling::*[1]"))
            //                 .getText()
            //                 .trim());

            WebElement bestSellerSpanParent = bestSellerSpan.findElement(By.xpath("..")); // This simply gets the parent. We need it because the text we need is not inside a subnode
            String bestSellerRankings = bestSellerSpanParent.getText();
            String nextRankLine = bestSellerRankings.split("\n")[0];

            // ####  all this parsing malarky needs to be put into the BookRanking class /----- > ######
            int hashIndex = nextRankLine.indexOf('#') + 1; // Position after '#'
            int spaceIndex = nextRankLine.indexOf(' ', hashIndex); // Position of the next space after '#' This should get us the number we want
            if (spaceIndex == -1) {
                // If no space is found, take the substring from '#' to the end of the string
                nextRankLine = nextRankLine.substring(hashIndex).trim();
            } else {
                // Extract the substring between '#' and the next space
                nextRankLine = nextRankLine.substring(hashIndex, spaceIndex).trim();
            }
            bookData.overallRanking = BookRanking.getRankingFromText(nextRankLine); // this returns all of the best seller rankings across all of the charts

            System.out.println("ASIN: " + bookData.ASIN);
            System.out.println("Kindle Store Ranking: " + bookData.overallRanking);
            // catching all exception types is a bit naughty, but this can fail in so many ways, that I just want to bomb out and inspect what Amazon has changed.
        } catch (Exception e) {

            System.out.println("We hit an exception reading the product details." + e.toString());
            bookData.error = true;
            bookData.errorMsg = e.toString();
        }

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
