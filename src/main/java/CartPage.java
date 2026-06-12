import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class CartPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Locators
    private By emptyMessageLocator = By.xpath("//div[@class='content' and contains(text(), 'Your shopping cart is empty')]");
    private By removeButtonLocator = By.cssSelector(".btn.btn-cart-remove");
    private By preloader = By.cssSelector(".preloader");
    private By body = By.tagName("body");

    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void verifyProductInCart(String expectedTitle, String expectedPrice) {
        wait.withMessage("Failure: The book title was not found in the shopping cart!")
                .until(ExpectedConditions.textToBePresentInElementLocated(body, expectedTitle));

        wait.withMessage("Failure: The expected price was not found in the cart subtotal!")
                .until(ExpectedConditions.textToBePresentInElementLocated(body, expectedPrice));
    }

    public void clearCart() {
        driver.get("https://www.periplus.com/checkout/cart");

        try {
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.presenceOfElementLocated(emptyMessageLocator),
                    ExpectedConditions.presenceOfElementLocated(removeButtonLocator)
            ));

            List<WebElement> removeButtons = driver.findElements(removeButtonLocator);

            if (removeButtons.isEmpty()) {
                System.out.println("Precondition Met: Cart was already empty.");
                return;
            }

            while (!removeButtons.isEmpty()) {
                wait.until(ExpectedConditions.invisibilityOfElementLocated(preloader));

                WebElement btnToRemove = removeButtons.get(0);
                wait.until(ExpectedConditions.elementToBeClickable(btnToRemove)).click();
                wait.until(ExpectedConditions.stalenessOf(btnToRemove));

                removeButtons = driver.findElements(removeButtonLocator);
            }
            System.out.println("Precondition Met: Cart cleared successfully.");

        } catch (Exception e) {
            System.out.println("An unexpected error occurred during cart cleanup: " + e.getMessage());
        }
    }
}