import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public abstract class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;

    // Global nav element — visible on all pages
    private By cartTotalBadgeLocator = By.id("cart_total");

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public int getCartBadgeCount() {
        try {
            String countText = wait.until(ExpectedConditions.visibilityOfElementLocated(cartTotalBadgeLocator))
                                   .getText()
                                   .trim();
            return Integer.parseInt(countText);
        } catch (Exception e) {
            // If the badge is empty or not present, assume the cart has 0 items
            return 0;
        }
    }
}
