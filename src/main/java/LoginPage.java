import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class LoginPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Locators — XPath with normalize-space is resilient to whitespace/case variants
    // and matches both "Sign In" and " Sign In " (padded text in rendered nav)
    private By signInLink = By.xpath("//a[normalize-space(.)='Sign In']");
    private By emailField = By.name("email");
    private By passwordField = By.name("password");
    private By loginButton = By.id("button-login");
    private By searchBox = By.name("filter_name"); // To verify login success

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    public void login(String email, String password) {
        // Wait for the page body to be fully visible before hunting for the nav link
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("body")));

        // Wait for the Sign In link to be present and clickable
        WebElement signIn = wait.until(ExpectedConditions.elementToBeClickable(signInLink));

        // Scroll the element into view, then attempt a normal click.
        // Fall back to a JavaScript click if the element is obscured (e.g. by a banner).
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", signIn);
            signIn.click();
        } catch (Exception e) {
            System.out.println("Normal click failed, using JS click: " + e.getMessage());
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", signIn);
        }

        wait.until(ExpectedConditions.visibilityOfElementLocated(emailField)).sendKeys(email);
        driver.findElement(passwordField).sendKeys(password);
        driver.findElement(loginButton).click();

        // Wait for login to complete
        wait.until(ExpectedConditions.visibilityOfElementLocated(searchBox));
    }
}