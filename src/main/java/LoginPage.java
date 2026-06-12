import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.Duration;

public class LoginPage {
    private static final String LOGIN_URL = "https://www.periplus.com/account/login";

    private WebDriver driver;
    private WebDriverWait wait;

    // Locators
    private By emailField = By.name("email");
    private By passwordField = By.name("password");
    private By loginButton = By.id("button-login");
    private By searchBox = By.name("filter_name"); // To verify login success

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    public void login(String email, String password) {
        // Navigate directly to the login page — bypasses nav-link click entirely.
        // This is immune to bot-detection page differences and popup overlays.
        System.out.println("Navigating directly to login page: " + LOGIN_URL);
        driver.get(LOGIN_URL);

        // Take a screenshot immediately after navigation for CI debugging
        takeScreenshot("01_login_page_loaded");

        // Wait for the email field to appear (confirms the login form is rendered)
        wait.until(ExpectedConditions.visibilityOfElementLocated(emailField)).sendKeys(email);
        driver.findElement(passwordField).sendKeys(password);
        driver.findElement(loginButton).click();

        // Take a screenshot after submitting to capture any errors
        takeScreenshot("02_after_login_submit");

        // Wait for login to complete — search box visible means we are on the home page
        wait.until(ExpectedConditions.visibilityOfElementLocated(searchBox));
    }

    private void takeScreenshot(String name) {
        try {
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File dest = new File("target/screenshots/" + name + ".png");
            dest.getParentFile().mkdirs();
            Files.copy(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Screenshot saved: " + dest.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("Could not save screenshot: " + e.getMessage());
        }
    }
}