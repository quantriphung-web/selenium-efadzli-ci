package phungtriquan;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LoginTest {
    private WebDriver driver;
    private final String url = "https://efadzli.com/software_testing/index.php?view=user_login";

    @BeforeAll
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
    void pauseBetweenTests() {
        // no-op
    }

    @AfterAll
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    // T01: Đăng nhập đúng thông tin
    @Test
    @Order(1)
    void testLoginAdam() throws InterruptedException {
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
        String statusText = status.getText().trim();

        Assertions.assertEquals("Congratulations!", statusText,
                "Expected success message after correct login.");
    }

    // T02: Đăng nhập sai thông tin
    @Test
    @Order(2)
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
        String statusText = status.getText().trim();

        Assertions.assertEquals("Login failed! Please try again.", statusText,
                "Expected failure message for incorrect credentials.");
    }

    // T03: Đăng nhập TLU
    @Test
    @Order(3)
    void testLoginSinhVienTLU() {
        driver.get("https://sinhvien1.tlu.edu.vn/#/login");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement user = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
        user.clear();
        user.sendKeys("2351067109");

        WebElement pass = driver.findElement(By.id("password"));
        pass.clear();
        pass.sendKeys("triquan123456789");

        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("/html/body/ui-view/div/div/div[2]/div/div/button")
        ));
        loginButton.click();

        wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("/login")));
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}