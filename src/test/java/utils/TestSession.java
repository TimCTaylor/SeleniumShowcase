package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
//import org.openqa.selenium.firefox.FirefoxDriver;
//import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;

import static utils.Constants.BROWSER_DEBUG_HEIGHT;
import static utils.Constants.BROWSER_DEBUG_WIDTH;

import java.time.Duration;
//import org.openqa.selenium.safari.SafariDriver;
//import org.openqa.selenium.safari.SafariOptions;

public class TestSession {
    public WebDriver sessionDriver; // session-wide object. The choice of WebDriver (ChromeDriver/ EdgeDriver etc)
                                    // can be set from the command line.
    public boolean forceErrors; // session-wide variable that can be set from the command line. If true then
                                // some tests will deliberately be set to fail assertions.
                                // This allows us to test our test reporting without needing to touch the tests
                                // (or understand anything about their implementation)
                                // [Implement later] public String loggingMode; // session-wide variable that
                                // can be set from the command line - for logging and diagnostic info. Consider
                                // refactoring into an enum

    /**
     * TestSession constructors.
     * We use the old trick (I first used this back in the 90s!) of chaining
     * overloaded constructors to handle default
     * values and bubble them up so we only do the essential constructor logic in
     * just one of the constructors.
     * This is one of the few places I've encountered so far in my Java life where
     * Python would handle this much better
     * (with named parameters)
     */
    public TestSession() {
        this(getSessionDriverChoice());
    }

    public TestSession(String sessionDriverChoice) {
        this(sessionDriverChoice, getForcedErrorChoice());
    }

    public TestSession(String sessionDriverChoice, boolean forcedErrorChoice) {
        this.forceErrors = forcedErrorChoice;
        // System.out.println("sessionDriverChoice.toUpperCase() evaluates to... " +
        // sessionDriverChoice.toUpperCase());

        // In most cases, we don't actually do anything with the driver options (such as
        // FireFoxOptions(), but the hooks are here if we need them.
        switch (sessionDriverChoice.toUpperCase()) {
            case "CHROME":
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.setBinary("D:\\Coding\\Selenium\\chrome-win64\\chrome.exe"); // I don't have enough space
                                                                                           // on the C drive. :-(
                this.sessionDriver = new ChromeDriver(chromeOptions);
                break;
            case "EDGE": // This could be improved by calling the EdgeDriverService rather than
                         // instantiating the driver directly.
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.addArguments("--inprivate"); // Added this line to avoid the 'sign in to Edge' window.
                edgeOptions.addArguments("--disable-notifications"); // Added this line to disable notifications.
                edgeOptions.addArguments("--disable-features=CookiePrompt"); // Add this line to disable cookie popup
                                                                             // when we open the browser.

                // The next two lines are for headless mode, and I need to set them or the tests will fail when run from Jenkins.
                //TODO: add a command line option to enable headless mode
                edgeOptions.addArguments("--headless"); // Enable headless mode
                edgeOptions.addArguments("--disable-gpu"); // Disable GPU acceleration                                                        
                this.sessionDriver = new EdgeDriver(edgeOptions);
                break;
            case "INTERNET_EXPLORER": // the call to the IEDriver will automatically load EdgeDriver in IE mode
                InternetExplorerOptions ieOptions = new InternetExplorerOptions();
                this.sessionDriver = new InternetExplorerDriver(ieOptions);
                // Commented out Firefox and Safari because I haven't the disk space to install
                // all the drivers. But in a real setup, we'd want to test all of them.
                // case "FIREFOX":
                // FirefoxOptions firefoxOptions = new FirefoxOptions();
                // this.sessionDriver = new FirefoxDriver(firefoxOptions);
                // break;
                // case "SAFARI":
                // SafariOptions safariOptions = new SafariOptions();
                // this.sessionDriver = new SafariDriver(safariOptions);

            default:
                throw new IllegalArgumentException("Invalid WebDriver choice: " + sessionDriverChoice);
        }
    }

    public void closeTestSession() {
        sessionDriver.quit();
    }

    // refactor a commonly called statement for the sake of making the code easier
    // to read
    public void implicitWait(int waitTime) {
        sessionDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(waitTime));
    }

    // force a delay rather than a synchronisation wait. I used this, for example,
    // to ensure that an
    // embedded video plays for a certain duration
    public static void sleep(int sleepTime) { // sleepTime is in seconds
        try {
            Thread.sleep(sleepTime * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    // This allows me to have my test browser window above my IDE. In real life, I
    // would probably add mehods to restore a previous size.
    public void setBrowserToDebugSize() {
        sessionDriver.manage().window()
                .setSize(new org.openqa.selenium.Dimension(BROWSER_DEBUG_WIDTH, BROWSER_DEBUG_HEIGHT));
        sessionDriver.manage().window().setPosition(new org.openqa.selenium.Point(0, 0));
    }

    public void setBrowserToFullScreen() {
        sessionDriver.manage().window().fullscreen();
    }

    private static String getSessionDriverChoice() {
        // Retrieve the environment parameter passed from Maven. Defaults to "CHROME"
        // If we have passed a parameter to the TestCase constructor, then that takes
        // precedence over the command line option.
        // Usage: mvn test -Ddriver=SAFARI to select Safari web driver
        String driver = System.getProperty("driver");
        if (driver == null || driver.isBlank()) {
            return "CHROME";
        } else {
            return driver;
        }
    }

    private static boolean getForcedErrorChoice() {
        // Retrieve the environment parameter passed from Maven
        // Usage: mvn test -Dforce_errors=true
        // If a parameter is passed to the TestSession constructor, then it takes
        // precedence over the command line option
        String forceErrors = System.getProperty("force_errors");
        return Boolean.parseBoolean(forceErrors); // returns false if the string is "false" but also if it fails to
                                                  // parse as a boolean.
    }

}
