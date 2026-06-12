import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class SearchResultsPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Locators
    private By firstProductLink = By.cssSelector(".single-product a");
    private By preloader = By.cssSelector(".preloader");

    public SearchResultsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    public void selectFirstProduct() {
        wait.until(ExpectedConditions.elementToBeClickable(firstProductLink)).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(preloader));
    }
}