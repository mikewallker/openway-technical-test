import io.github.cdimascio.dotenv.Dotenv;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

public class PeriplusCartTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeClass
    public void setupAndLogin() {
        // 1. Setup Driver
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Load credentials
        Dotenv dotenv = Dotenv.load();
        String myEmail = dotenv.get("PERIPLUS_EMAIL");
        String myPassword = dotenv.get("PERIPLUS_PASSWORD");

        // 2. Navigate to Periplus
        driver.get("https://www.periplus.com/");

        // 3. Precondition: Login
        WebElement signInLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Sign In")));
        signInLink.click();

        WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("email")));
        emailField.sendKeys(myEmail);

        driver.findElement(By.name("password")).sendKeys(myPassword);
        driver.findElement(By.id("button-login")).click();

        // Wait for login to complete by waiting for the search bar to be usable
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("filter_name")));
        System.out.println("Precondition Met: Login Successful.");

        // 4. Precondition: Clear Cart
        clearCart();
    }

    @Test
    public void testAddToCart() {

        // 1. Search for a product
        WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("filter_name")));
        searchBox.sendKeys("Harry Potter");
        searchBox.submit();

        // Wait for the loading overlay to completely disappear
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".preloader")));

        // 2. Select the first product
        WebElement firstProduct = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".single-product a")));
        firstProduct.click();

        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".preloader")));

        WebElement titleElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("h2")));
        String expectedTitle = titleElement.getText();

        WebElement priceElement = driver.findElement(By.cssSelector(".quickview-price"));
        String expectedPrice = priceElement.getText();

        System.out.println("Testing addition of book: " + expectedTitle);

        // 3. Click Add to Cart
        WebElement addToCartBtn = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".btn.btn-add-to-cart")));
        addToCartBtn.click();

        // Wait for the success modal/banner
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".ti-check.modal-check")));

        // 4. Navigate to the Cart Page
        driver.get("https://www.periplus.com/checkout/cart");

        // 5. Verify the cart contents
        wait.withMessage("Failure: The book title was not found in the shopping cart!")
                .until(ExpectedConditions.textToBePresentInElementLocated(By.tagName("body"), expectedTitle));

        wait.withMessage("Failure: The expected price was not found in the cart subtotal!")
                .until(ExpectedConditions.textToBePresentInElementLocated(By.tagName("body"), expectedPrice));

        System.out.println("Test Passed: Product correctly added to cart.");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private void clearCart() {
        // 1. Updated exact cart URL
        driver.get("https://www.periplus.com/checkout/cart");

        try {
            // 2. Define our exact locators based on the live DOM
            By emptyMessageLocator = By.xpath("//div[@class='content' and contains(text(), 'Your shopping cart is empty')]");
            By removeButtonLocator = By.cssSelector(".btn.btn-cart-remove");

            // 3. The "No-Wait" Check: Whichever element renders first unblocks the code instantly
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.presenceOfElementLocated(emptyMessageLocator),
                    ExpectedConditions.presenceOfElementLocated(removeButtonLocator)
            ));

            // 4. Now that the page is verified as fully loaded, check for buttons
            List<WebElement> removeButtons = driver.findElements(removeButtonLocator);

            if (removeButtons.size() == 0) {
                System.out.println("Precondition Met: Cart was already empty.");
                driver.get("https://www.periplus.com/");
                return; // Exit the method immediately, no time wasted!
            }

            // 5. If we reach here, buttons exist. Proceed with robust deletion.
            while (removeButtons.size() > 0) {
                wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".preloader")));

                WebElement btnToRemove = removeButtons.get(0);
                wait.until(ExpectedConditions.elementToBeClickable(btnToRemove)).click();

                // Wait for the UI to destroy the button before grabbing the next one
                wait.until(ExpectedConditions.stalenessOf(btnToRemove));

                // Re-fetch the updated list
                removeButtons = driver.findElements(removeButtonLocator);
            }
            System.out.println("Precondition Met: Cart cleared successfully.");

        } catch (Exception e) {
            System.out.println("An unexpected error occurred during cart cleanup: " + e.getMessage());
        }

        // Navigate back to home page to continue the test naturally
        driver.get("https://www.periplus.com/");
    }
}

