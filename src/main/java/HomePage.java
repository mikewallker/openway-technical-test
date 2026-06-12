import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class HomePage extends BasePage {

    // Locators
    private By searchBox = By.name("filter_name");
    private By preloader = By.cssSelector(".preloader");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public void searchProduct(String keyword) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(searchBox)).sendKeys(keyword);
        driver.findElement(searchBox).submit();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(preloader));
    }
}