package sandbox;

/**
 * For driving the HumanLegionPage class (before I make it abstract)
 */

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.annotations.VisibleForTesting;

import element.classes.HomePageBanner;
import element.classes.YouTubeVideo;
import element.classes.booktab.AmazonSalesLinks;
import element.classes.booktab.BookTab;
import utils.*;

import java.time.Duration;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static utils.Constants.COPYRIGHT_STATEMENT;
import page.classes.HumanLegionPage;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import utils.TestSession;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static utils.Constants.LEGION_BASE_URL;
import static utils.Constants.SCREENSHOT_FOLDER;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

import org.apache.commons.io.FileUtils;

public class PageClassSandboxTest {

    private TestSession testSession;
    private WebDriver driver;
    private HumanLegionPage testPage;

    @BeforeEach
    public void setUp() {
        testSession = new TestSession();
        driver = testSession.sessionDriver;
        testPage = new HumanLegionPage(driver);
        driver.get(LEGION_BASE_URL);
    }

    @AfterEach
    public void tearDown() {
        testSession.closeTestSession();
    }

    @Test
    public void TestFooterMethods() {
        assertTrue(testPage.verifyFooter());
    }
}
