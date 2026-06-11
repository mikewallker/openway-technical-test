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
        options.addArguments("--start-maximized");
        driver = new ChromeDriver(options);

        // Initialize Page Objects
        loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        searchResultsPage = new SearchResultsPage(driver);
        productDetailsPage = new ProductDetailsPage(driver);
        cartPage = new CartPage(driver);

        // Load credentials
        Dotenv dotenv = Dotenv.load();
        String myEmail = dotenv.get("PERIPLUS_EMAIL");
        String myPassword = dotenv.get("PERIPLUS_PASSWORD");

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