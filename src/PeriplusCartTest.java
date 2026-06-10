import io.github.cdimascio.dotenv.Dotenv;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;

public class PeriplusCartTest {

    WebDriver driver;
    WebDriverWait wait;

    @BeforeClass
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        // Set an explicit wait of 10 seconds
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test
    public void testAddToCart() {
        // Load the .env file
        Dotenv dotenv = Dotenv.load();

        // Fetch the password and email from the .env file
        String myPassword = dotenv.get("PERIPLUS_PASSWORD");
        String myEmail = dotenv.get("PERIPLUS_EMAIL");

        // 1. Navigate to Periplus
        driver.get("https://www.periplus.com/");

        // 2. Login (Assuming there is a sign-in button with these selectors - you may need to inspect the live site to adjust locators)
        WebElement signInLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Sign In")));
        signInLink.click();

        WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("email"))); // Update locator if needed
        emailField.sendKeys(myEmail);

        WebElement passwordField = driver.findElement(By.name("password")); // Update locator if needed
        passwordField.sendKeys(myPassword);

        driver.findElement(By.id("button-login")).click(); // Update locator if needed

        System.out.println("Login Successful!");

        // 3. Search for a product
        WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("filter_name")));
        searchBox.sendKeys("Harry Potter");
        searchBox.submit();

        // Wait for the loading overlay to completely disappear first
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".preloader")));

        // 4. Click on the first product and add to cart
        WebElement firstProduct = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".single-product a")));
        firstProduct.click();

        WebElement addToCartBtn = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".btn-add-to-cart")));
        addToCartBtn.click();

        // 5. Verify successful addition (e.g., checking a success message or cart counter)
        WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".alert-success")));
        Assert.assertTrue(successMessage.getText().contains("Success: You have added"), "Product was not successfully added to the cart.");

        System.out.println("Test Passed: Product successfully added to cart!");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}