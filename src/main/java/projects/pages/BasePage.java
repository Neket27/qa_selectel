package projects.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Set;

public class BasePage {

    protected WebDriver driver;
    protected WebDriverWait wait;

    // Стандартные таймауты
    protected static final int DEFAULT_TIMEOUT_SECONDS = 10;
    protected static final int PAGE_LOAD_TIMEOUT_SECONDS = 30;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT_SECONDS));
    }

    // === Navigation ===

    public void openUrl(String url) {
        driver.get(url);
        waitForPageLoad();
    }

    public void waitForPageLoad() {
        wait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete'"));
    }

    public boolean waitForUrlContains(String expectedUrlPart) {
        try {
            wait.until(ExpectedConditions.urlContains(expectedUrlPart));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    // === Element Interaction ===

    public void clearAndSendKeys(By locator, String text) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        element.clear();
        element.sendKeys(text);
    }

    public void sendKeys(By locator, String text) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        element.sendKeys(text);
    }

    public void click(By locator) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        element.click();
    }

    public void waitUntilClickable(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public void waitUntilVisible(By locator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public void waitUntilHidden(By locator) {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public boolean waitForNavigation(String originalUrl) {
        return waitForNavigation(originalUrl, DEFAULT_TIMEOUT_SECONDS);
    }

    public boolean waitForNavigation(String originalUrl, int timeoutSeconds) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds))
                    .until(d ->
                            !d.getCurrentUrl().equals(originalUrl) ||
                                    d.getWindowHandles().size() > 1
                    );
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public void waitUntilInvisible(By locator) {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public void waitUntilTextContains(By locator, String text) {
        wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }

    // === Element State Checks ===

    public boolean isDisplayed(By locator) {
        try {
            driver.findElement(locator);
            return true;
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    public boolean isEnabled(By locator) {
        try {
            WebElement element = driver.findElement(locator);
            return element.isEnabled();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    public boolean isSelected(By locator) {
        try {
            WebElement element = driver.findElement(locator);
            return element.isSelected();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    public boolean isExists(By locator) {
        try {
            driver.findElement(locator);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    // === Element Data ===

    public String getText(By locator) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        return element.getText().trim();
    }

    public String getAttribute(By locator, String attributeName) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        return element.getAttribute(attributeName);
    }

    public String getInputValue(By locator) {
        return getAttribute(locator, "value");
    }

    public String getCssClass(By locator) {
        return getAttribute(locator, "class");
    }

    // === Find Elements ===

    public WebElement findElement(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    public List<WebElement> findElements(By locator) {
        return driver.findElements(locator);
    }

    public WebElement findVisibleElement(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    // === Dropdown ===

    public void selectByVisibleText(By locator, String visibleText) {
        WebElement element = findElement(locator);
        Select select = new Select(element);
        select.selectByVisibleText(visibleText);
    }

    public void selectByValue(By locator, String value) {
        WebElement element = findElement(locator);
        Select select = new Select(element);
        select.selectByValue(value);
    }

    public void selectByIndex(By locator, int index) {
        WebElement element = findElement(locator);
        Select select = new Select(element);
        select.selectByIndex(index);
    }

    // === Alert ===

    public void acceptAlert() {
        wait.until(ExpectedConditions.alertIsPresent()).accept();
    }

    public void dismissAlert() {
        wait.until(ExpectedConditions.alertIsPresent()).dismiss();
    }

    public String getAlertText() {
        return wait.until(ExpectedConditions.alertIsPresent()).getText();
    }

    public void sendKeysToAlert(String text) {
        wait.until(ExpectedConditions.alertIsPresent()).sendKeys(text);
    }

    // === Wait ===

    public void waitWithTimeout(By locator, int timeoutSeconds) {
        WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        customWait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    public void hardWait(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void hardWaitSeconds(long seconds) {
        hardWait(seconds * 1000);
    }

    // === Scroll ===

    public void scrollToElement(By locator) {
        WebElement element = findElement(locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public void scrollToElementAndClick(By locator) {
        scrollToElement(locator);
        waitUntilClickable(locator);
        click(locator);
    }

    public void scrollDown(int pixels) {
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, " + pixels + ");");
    }

    public void scrollUp(int pixels) {
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, -" + pixels + ");");
    }

    // === JavaScript ===

    public Object executeJavaScript(String script) {
        return ((JavascriptExecutor) driver).executeScript(script);
    }

    public Object executeJavaScript(String script, Object... args) {
        return ((JavascriptExecutor) driver).executeScript(script, args);
    }

    public void javascriptClick(By locator) {
        WebElement element = findElement(locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    // === Screenshot ===

    public String takeScreenshot() {
        TakesScreenshot ts = (TakesScreenshot) driver;
        return ts.getScreenshotAs(OutputType.BASE64);
    }

    public String takeElementScreenshot(By locator) {
        WebElement element = findElement(locator);
        return element.getScreenshotAs(OutputType.BASE64);
    }

    // === Frame/Window ===

    public void switchToFrame(int index) {
        driver.switchTo().frame(index);
    }

    public void switchToFrame(String nameOrId) {
        driver.switchTo().frame(nameOrId);
    }

    public void switchToFrame(WebElement element) {
        driver.switchTo().frame(element);
    }

    public void switchToParentFrame() {
        driver.switchTo().parentFrame();
    }

    public void switchToDefaultContent() {
        driver.switchTo().defaultContent();
    }

    public void switchToWindow(String windowHandle) {
        driver.switchTo().window(windowHandle);
    }

    public void switchToNewWindow() {
        String currentWindow = driver.getWindowHandle();
        for (String windowHandle : driver.getWindowHandles()) {
            if (!windowHandle.equals(currentWindow)) {
                driver.switchTo().window(windowHandle);
                break;
            }
        }
    }

    public void closeCurrentWindow() {
        driver.close();
    }

    public int getWindowCount() {
        return driver.getWindowHandles().size();
    }

    // === Cookies ===

    public Set<Cookie> getAllCookies() {
        return driver.manage().getCookies();
    }

    public Cookie getCookie(String name) {
        return driver.manage().getCookieNamed(name);
    }

    public void addCookie(Cookie cookie) {
        driver.manage().addCookie(cookie);
    }

    public void deleteCookie(String name) {
        driver.manage().deleteCookieNamed(name);
    }

    public void deleteAllCookies() {
        driver.manage().deleteAllCookies();
    }

    // === Refresh/Navigation ===

    public void refresh() {
        driver.navigate().refresh();
        waitForPageLoad();
    }

    public void goBack() {
        driver.navigate().back();
        waitForPageLoad();
    }

    public void goForward() {
        driver.navigate().forward();
        waitForPageLoad();
    }
}