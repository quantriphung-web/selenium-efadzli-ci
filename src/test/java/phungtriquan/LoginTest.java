package phungtriquan;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginTest {
    private WebDriver driver;
    private final String url = "https://efadzli.com/software_testing/index.php?view=user_login";

    @BeforeEach
    void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();

        String headless = System.getProperty("headless");
        if ("true".equalsIgnoreCase(headless)) {
            options.addArguments("--headless=new");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--window-size=1920,1080");
        }

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void testLoginCorrect() throws InterruptedException {
        driver.get(url);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement user = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior:'smooth',block:'center'});", user);
        user.clear();
        user.sendKeys("Adam");

        WebElement pass = driver.findElement(By.id("password"));
        pass.clear();
        pass.sendKeys("Adam123");

        WebElement submit = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("submitButton")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior:'smooth',block:'center'});", submit);
        Thread.sleep(1000);

        try {
            submit.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submit);
        }

        WebElement status = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("status")));
        Assertions.assertEquals("Congratulations!", status.getText().trim(),
                "Expected success message after correct login.");
    }

    @Test
    void testLoginIncorrect() throws InterruptedException {
        driver.get(url);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement user = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior:'smooth',block:'center'});", user);
        user.clear();
        user.sendKeys("adam"); // sai: chữ thường

        WebElement pass = driver.findElement(By.id("password"));
        pass.clear();
        pass.sendKeys("0000"); // sai password

        WebElement submit = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("submitButton")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior:'smooth',block:'center'});", submit);
        Thread.sleep(1000);

        try {
            submit.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submit);
        }

        WebElement status = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("status")));
        Assertions.assertEquals("Login failed! Please try again.", status.getText().trim(),
                "Expected failure message for incorrect credentials.");
    }
}