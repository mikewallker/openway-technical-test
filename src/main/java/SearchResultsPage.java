import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class SearchResultsPage extends BasePage {

    // Locators
    private By firstProductLink = By.cssSelector(".single-product a");
    private By preloader = By.cssSelector(".preloader");

    public SearchResultsPage(WebDriver driver) {
        super(driver);
    }

    public void selectFirstProduct() {
        wait.until(ExpectedConditions.elementToBeClickable(firstProductLink)).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(preloader));
    }
}