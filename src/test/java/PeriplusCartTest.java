import io.github.cdimascio.dotenv.Dotenv;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class PeriplusCartTest {

    private WebDriver driver;
    private LoginPage loginPage;
    private HomePage homePage;
    private SearchResultsPage searchResultsPage;
    private ProductDetailsPage productDetailsPage;
    private CartPage cartPage;

    @BeforeClass
    public void setupAndLogin() {
        // 1. Setup Driver
        ChromeOptions options = new ChromeOptions();

        // Check if we are running in GitHub Actions
        String isCI = System.getenv("CI");

        if (isCI != null && isCI.equals("true")) {
            // CI Environment: Run invisibly to prevent crashes
            options.addArguments("--headless=new");
            options.addArguments("--window-size=1920,1080");
            options.addArguments("--disable-gpu");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            System.out.println("Running in Headless Mode on CI/CD");
        } else {
            // Local Environment: Open the physical browser
            options.addArguments("--start-maximized");
            System.out.println("Running in UI Mode Locally");
        }

        driver = new ChromeDriver(options);

        // Initialize Page Objects
        loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        searchResultsPage = new SearchResultsPage(driver);
        productDetailsPage = new ProductDetailsPage(driver);
        cartPage = new CartPage(driver);

        // Load credentials gracefully (ignores missing .env file for CI)
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

        String myEmail = dotenv.get("PERIPLUS_EMAIL");
        String myPassword = dotenv.get("PERIPLUS_PASSWORD");

        // Fail-fast validation: Ensure neither variable is null or empty
        if (myEmail == null || myEmail.trim().isEmpty() ||
                myPassword == null || myPassword.trim().isEmpty()) {

            // Clean up the driver before crashing so you don't leave zombie Chrome processes
            if (driver != null) {
                driver.quit();
            }
            throw new IllegalStateException("CRITICAL BOOT ERROR: PERIPLUS_EMAIL or PERIPLUS_PASSWORD environment variables are missing or empty.");
        }

        // 2. Navigate to Periplus
        driver.get("https://www.periplus.com/");

        // 3. Precondition: Login
        loginPage.login(myEmail, myPassword);
        System.out.println("Precondition Met: Login Successful.");

        // 4. Precondition: Clear Cart
        cartPage.clearCart();

        // Return to home page to begin the actual test cleanly
        driver.get("https://www.periplus.com/");
    }

    @Test
    public void testAddToCart() {
        // 1. Search for a product
        homePage.searchProduct("book");

        // 2. Select the first product
        searchResultsPage.selectFirstProduct();

        // Get expected details from the Product page
        String expectedTitle = productDetailsPage.getProductTitle();
        String expectedPrice = productDetailsPage.getProductPrice();
        System.out.println("Testing addition of book: " + expectedTitle);

        // 3. Click Add to Cart
        productDetailsPage.addToCart();

        // 4. Navigate to the Cart Page
        driver.get("https://www.periplus.com/checkout/cart");

        // 5. Verify the cart contents
        cartPage.verifyProductInCart(expectedTitle, expectedPrice);
        System.out.println("Test Passed: Product correctly added to cart.");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}