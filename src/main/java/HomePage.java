import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class HomePage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Locators
    private By searchBox = By.name("filter_name");
    private By preloader = By.cssSelector(".preloader");

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void searchProduct(String keyword) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(searchBox)).sendKeys(keyword);
        driver.findElement(searchBox).submit();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(preloader));
    }
}