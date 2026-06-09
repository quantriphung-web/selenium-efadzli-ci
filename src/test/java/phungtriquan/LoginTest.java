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
        
        // Cấu hình chạy headless cho CI
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
    void pauseBetweenTests() throws InterruptedException {
        // Giảm thời gian chờ để CI chạy nhanh hơn, hoặc bỏ qua nếu không cần thiết
        // Thread.sleep(1000);
    }

    @AfterAll
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

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
        Thread.sleep(1000); // Đợi scroll hoàn tất
        
        try {
            submit.click();
        } catch (Exception e) {
            // Nếu click thông thường bị chặn, dùng JavaScript click
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submit);
        }

        WebElement status = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("status")));
        String statusText = status.getText().trim();

        Assertions.assertEquals("Congratulations!", statusText, "Expected status to show success message after submitting.");
    }

    @Test
    @Order(2)
    void testLoginKhue() throws InterruptedException {
        driver.get(url);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        WebElement user = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior:'smooth',block:'center'});", user);
        user.clear();
        user.sendKeys("10-Khue");

        WebElement pass = driver.findElement(By.id("password"));
        pass.clear();
        pass.sendKeys("Khue123");

        WebElement submit = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("submitButton")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior:'smooth',block:'center'});", submit);
        Thread.sleep(1000); // Đợi scroll hoàn tất
        
        try {
            submit.click();
        } catch (Exception e) {
            // Nếu click thông thường bị chặn, dùng JavaScript click
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submit);
        }

        WebElement status = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("status")));
        String statusText = status.getText().trim();

        Assertions.assertEquals("Congratulations!", statusText, "Expected status to show success message after submitting.");
    }

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

        // Chờ trang chủ load bằng cách đợi URL thay đổi hoặc một phần tử đặc trưng xuất hiện
        wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("/login")));
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}