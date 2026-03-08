package projects.tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.*;

import java.time.Duration;

public class BaseTest {

    // ThreadLocal для поддержки параллельных тестов
    private static final ThreadLocal<WebDriver> driverPool = new ThreadLocal<>();

    public static WebDriver getDriver() {
        WebDriver driver = driverPool.get();
        if (driver == null) {
            throw new IllegalStateException("WebDriver не инициализирован! Проверьте @BeforeMethod");
        }
        return driver;
    }

    @BeforeMethod
    public void initDriver() {
        ChromeOptions options = new ChromeOptions();

        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--start-maximized");

        WebDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driverPool.set(driver);
    }

    @AfterMethod
    public void tearDown() {
        WebDriver driver = driverPool.get();
        if (driver != null) {
            try {
                driver.quit();
            } catch (Exception e) {
                System.err.println("Ошибка при закрытии драйвера: " + e.getMessage());
            } finally {
                driverPool.remove();
            }
        }
    }
}