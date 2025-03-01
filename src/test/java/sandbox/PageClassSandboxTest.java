package sandbox;

/**
 * For driving the HumanLegionPage class (before I make it abstract)
 */

import static org.junit.jupiter.api.Assertions.assertTrue;
import page.classes.HumanLegionPage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import utils.TestSession;

import static utils.Constants.LEGION_BASE_URL;

// HumanLegionPage is an abstract class, so we need to create a test class that extends it.
class TestHumanLegionPage extends HumanLegionPage {
    public TestHumanLegionPage(WebDriver driver) {
        super(driver);
    }
}

public class PageClassSandboxTest {

    private TestSession testSession;
    private WebDriver driver;
    private HumanLegionPage testPage;

    @BeforeEach
    public void setUp() {
        testSession = new TestSession();
        driver = testSession.sessionDriver;
        testPage = new TestHumanLegionPage(driver);
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
