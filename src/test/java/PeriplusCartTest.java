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

        // Grab the exact title and price of the book before adding it
        WebElement titleElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".h2")));
        String expectedTitle = titleElement.getText();

        WebElement priceElement = driver.findElement(By.cssSelector(".quickview-price"));
        String expectedPrice = priceElement.getText();

        System.out.println("Adding book: " + expectedTitle + " | Price: " + expectedPrice);

        // Click Add to Cart
        WebElement addToCartBtn = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".btn btn-add-to-cart")));
        addToCartBtn.click();

        // 5. Navigate to the Cart Page
        // Wait for the success banner just to ensure the system registered the click before we navigate away
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".ti-check modal-check")));

        // Find the shopping cart button at the top of the screen and click it
        WebElement cartIcon = wait.until(ExpectedConditions.elementToBeClickable(By.id("show-your-cart_mobile")));
        cartIcon.click();

        // 6. The Deep Verification
        // Check if the title exists inside the cart table
        WebElement cartTable = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".table-bordered"))); // Example locator for the cart table
        String cartText = cartTable.getText();

        Assert.assertTrue(cartText.contains(expectedTitle), "Failure: The book title was not found in the shopping cart!");

        // Check if the expected price is reflected in the cart
        Assert.assertTrue(cartText.contains(expectedPrice), "Failure: The expected price was not found in the cart subtotal!");

        System.out.println("Test Passed: Product exists in cart and price matches!");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}