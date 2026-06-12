import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class ProductDetailsPage extends BasePage {

    // Locators
    private By titleElement = By.tagName("h2");
    private By priceElement = By.cssSelector(".quickview-price");
    private By addToCartBtn = By.cssSelector(".btn.btn-add-to-cart");
    private By successModal = By.cssSelector(".ti-check.modal-check");

    public ProductDetailsPage(WebDriver driver) {
        super(driver);
    }

    public String getProductTitle() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(titleElement)).getText();
    }

    public String getProductPrice() {
        return driver.findElement(priceElement).getText();
    }


    public void addToCart() {
        wait.until(ExpectedConditions.elementToBeClickable(addToCartBtn)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(successModal));
    }
}