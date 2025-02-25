/** CaptureScreenshot.java
 * An example of common code refactored into a utility class.
 * This takes the selenium.TakeScreenshot method and reduces size before saving it to 
 * a standard folder defined in out Constants class.
 */
package utils;

import static utils.Constants.SCREENSHOT_FOLDER;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.imageio.ImageIO;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.*;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;

public class CaptureScreenshot {
    // testClass and viewName are the first and second elements of the filename,
    // combined with the timestamp to make it unique
    public static void captureScreenshot(WebDriver driver, String testClass, String viewName) {
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            BufferedImage bufferedImage = ImageIO.read(screenshot);
            int originalWidth = bufferedImage.getWidth();
            int originalHeight = bufferedImage.getHeight();
            int targetWidth = 800;
            int targetHeight = (originalHeight * targetWidth) / originalWidth; // a bit of a faff here, but I'm trying
                                                                               // to keep the aspect ratio
            BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, bufferedImage.getType());
            Graphics2D g = resizedImage.createGraphics();
            g.drawImage(bufferedImage, 0, 0, targetWidth, targetHeight, null);
            g.dispose();

            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String filename = SCREENSHOT_FOLDER + testClass + "_" + viewName + "_" + timestamp + ".jpg";
            File screenshotFolder = new File(SCREENSHOT_FOLDER); // Create the folder (the any parent path to it) if it
                                                                 // doesn't exist
            if (!screenshotFolder.exists()) {
                screenshotFolder.mkdirs();
            }
            ImageIO.write(resizedImage, "jpg", new File(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
