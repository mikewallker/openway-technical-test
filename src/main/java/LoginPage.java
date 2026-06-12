import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class LoginPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Locators
    private By signInLink = By.linkText("Sign In");
    private By emailField = By.name("email");
    private By passwordField = By.name("password");
    private By loginButton = By.id("button-login");
    private By searchBox = By.name("filter_name"); // To verify login success

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void login(String email, String password) {
        wait.until(ExpectedConditions.elementToBeClickable(signInLink)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(emailField)).sendKeys(email);
        driver.findElement(passwordField).sendKeys(password);
        driver.findElement(loginButton).click();

        // Wait for login to complete
        wait.until(ExpectedConditions.visibilityOfElementLocated(searchBox));
    }
}