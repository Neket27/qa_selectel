package projects.pages.components;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import projects.constants.ServicesSwiperConstants;
import projects.pages.BasePage;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServicesSwiperPage extends BasePage {

    // === Locators  ===
    private static final By SWIPER_CONTAINER = By.cssSelector(".swiper-cards__container");
    private static final By SWIPER_WRAPPER = By.cssSelector(".swiper-wrapper");
    private static final By SWIPER_SLIDE = By.xpath("//main/div/div[1]/div/div[1]/div/div");
    private static final By SWIPER_SLIDE_ACTIVE = By.cssSelector(".swiper-slide-active");
    private static final By SWIPER_PREV_BUTTON = By.cssSelector(".swiper-button-prev");
    private static final By SLIDE_LINK = By.cssSelector(".swiper-slide a[href]");
    private static final String TRANSLATE_X_PATTERN_STRING = "translate3d\\((-?\\d+\\.?\\d*)px";

    // === Pattern  ===
    private static final Pattern TRANSLATE_X_PATTERN = Pattern.compile(TRANSLATE_X_PATTERN_STRING);

    // ===  Swipe Parameters  ===
    private static final int SLIDE_WIDTH_PX = 771;
    private static final double TRANSITION_DURATION_SECONDS = 0.3;
    private static final int WAIT_AFTER_SWIPE_SECONDS = 1;
    private static final String TRANSITION_TIMING_FUNCTION = "ease";

    //  === JavaScript  ===
    private static final String SET_TRANSFORM_SCRIPT =
            "arguments[0].style.transform = 'translate3d(%dpx, 0px, 0px)';";
    private static final String SET_TRANSITION_SCRIPT =
            "arguments[0].style.transition = 'transform %fs %s';";

    public ServicesSwiperPage(WebDriver driver) {
        super(driver);
    }

    // === Navigation ===

    public ServicesSwiperPage openMainPage() {
        openUrl(ServicesSwiperConstants.MAIN_URL);
        return this;
    }

    public ServicesSwiperPage scrollToSwiper() {
        scrollToElement(SWIPER_CONTAINER);
        hardWaitSeconds(1);
        return this;
    }

    // === Validation Checks ===

    public boolean isSwiperDisplayed() {
        return isDisplayed(SWIPER_CONTAINER);
    }

    public boolean isPrevButtonDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(SWIPER_PREV_BUTTON));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean isActiveSlideDisplayed() {
        return isDisplayed(SWIPER_SLIDE_ACTIVE);
    }

    public boolean hasMinimumSlides(int minCount) {
        return getSlidesCount() >= minCount;
    }

    // === Slide Operations ===

    public int getSlidesCount() {
        List<WebElement> slides = driver.findElements(SWIPER_SLIDE);
        return slides.size();
    }

    public List<WebElement> getAllSlides() {
        return driver.findElements(SWIPER_SLIDE);
    }

    public WebElement getActiveSlide() {
        return findElement(SWIPER_SLIDE_ACTIVE);
    }

    public String getActiveSlideClass() {
        return getActiveSlide().getAttribute("class");
    }

    public String getSlideLink(int slideIndex) {
        List<WebElement> links = driver.findElements(SLIDE_LINK);
        if (slideIndex < links.size()) {
            return links.get(slideIndex).getAttribute("href");
        }
        return null;
    }

    // === Actions ===

    public ServicesSwiperPage swipeToLeft() {
        WebElement swiperWrapper = findElement(SWIPER_WRAPPER);
        int currentX = getCurrentTranslateX(swiperWrapper);
        int newX = currentX - SLIDE_WIDTH_PX;

        executeSwipeScript(swiperWrapper, newX);
        hardWaitSeconds(WAIT_AFTER_SWIPE_SECONDS);

        return this;
    }

    public ServicesSwiperPage swipeToRight() {
        WebElement swiperWrapper = findElement(SWIPER_WRAPPER);
        int currentX = getCurrentTranslateX(swiperWrapper);
        int newX = currentX + SLIDE_WIDTH_PX;

        executeSwipeScript(swiperWrapper, newX);
        hardWaitSeconds(WAIT_AFTER_SWIPE_SECONDS);

        return this;
    }

    public ServicesSwiperPage clickSlide(int slideIndex) {
        List<WebElement> slides = getAllSlides();
        if (slideIndex < slides.size()) {
            WebElement link = slides.get(slideIndex).findElement(SLIDE_LINK);
            wait.until(ExpectedConditions.elementToBeClickable(link));
            link.click();
        }
        return this;
    }

    // === Wait Methods (fluent) ===

    public ServicesSwiperPage waitForSwiperLoaded() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(SWIPER_CONTAINER));
        return this;
    }

    // === Helpers ===

    private int getCurrentTranslateX(WebElement element) {
        String currentTransform = element.getAttribute("style");
        if (currentTransform != null && currentTransform.contains("translate3d")) {
            String[] parts = currentTransform.split("translate3d\\(");
            if (parts.length > 1) {
                String xPart = parts[1].split(",")[0].replace("px", "").trim();
                try {
                    return Integer.parseInt(xPart.split("\\.")[0]);
                } catch (NumberFormatException e) {
                    return 0;
                }
            }
        }
        return 0;
    }

    public int getWrapperTranslateX() {
        WebElement wrapper = findElement(SWIPER_WRAPPER);
        String transform = wrapper.getAttribute("style");
        Matcher matcher = TRANSLATE_X_PATTERN.matcher(transform);

        if (matcher.find()) {
            return (int) Double.parseDouble(matcher.group(1));
        }
        throw new RuntimeException("Не удалось извлечь translateX из: " + transform);
    }

    private void executeSwipeScript(WebElement element, int translateX) {
        String transformScript = String.format(SET_TRANSFORM_SCRIPT, translateX);
        String transitionScript = String.format(
                SET_TRANSITION_SCRIPT,
                TRANSITION_DURATION_SECONDS,
                TRANSITION_TIMING_FUNCTION
        );

        ((JavascriptExecutor) driver).executeScript(
                transformScript + " " + transitionScript,
                element
        );
    }

}