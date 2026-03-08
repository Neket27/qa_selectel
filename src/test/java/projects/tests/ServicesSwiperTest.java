package projects.tests;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import projects.constants.ServicesSwiperConstants;
import projects.pages.components.ServicesSwiperPage;
import projects.tests.dataProvider.ServicesSwiperTestData;

import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ServicesSwiperTest extends BaseTest {

    private ServicesSwiperPage swiperPage;
    private String originalWindowHandle;

    @BeforeMethod
    public void initPage() {
        swiperPage = new ServicesSwiperPage(getDriver());
        swiperPage.openMainPage().scrollToSwiper();
        originalWindowHandle = getDriver().getWindowHandle();
    }

    // ==================== POSITIVE TESTS ====================

    @Test(priority = 1, description = "Компонент Swiper отображается на странице")
    public void checkSwiperDisplayed() {
        Assert.assertTrue(swiperPage.isSwiperDisplayed(),
                "Компонент Swiper не отображается на странице");
    }

    @Test(priority = 2, description = "Swiper содержит минимальное количество слайдов")
    public void checkMinimumSlidesCount() {
        int slidesCount = swiperPage.getSlidesCount();
        Assert.assertTrue(slidesCount >= ServicesSwiperConstants.MIN_SLIDES_COUNT,
                "Количество слайдов меньше минимального. Ожидаемо: >= " +
                        ServicesSwiperConstants.MIN_SLIDES_COUNT + ", Фактически: " + slidesCount);
    }

    @Test(priority = 3, description = "Активный слайд отображается")
    public void checkActiveSlideDisplayed() {
        Assert.assertTrue(swiperPage.isActiveSlideDisplayed(),
                "Активный слайд не отображается");
    }

    @Test(priority = 4, description = "Каждый слайд содержит хотя бы одну ссылку")
    public void checkEachSlideHasAtLeastOneLink() {
        List<WebElement> slides = swiperPage.getAllSlides();

        Assert.assertFalse(slides.isEmpty(), "Слайды не найдены");
        Assert.assertTrue(slides.size() >= ServicesSwiperConstants.MIN_SLIDES_COUNT,
                "Количество слайдов меньше минимального. Ожидаемо: >= " +
                        ServicesSwiperConstants.MIN_SLIDES_COUNT + ", Фактически: " + slides.size());

        for (int i = 0; i < slides.size(); i++) {
            List<WebElement> links = slides.get(i).findElements(org.openqa.selenium.By.tagName("a"));
            Assert.assertFalse(links.isEmpty(),
                    "Слайд #" + (i + 1) + " должен содержать хотя бы 1 ссылку. Фактически: " + links.size());
        }
    }

    // ==================== LINK VALIDATION TESTS ====================

    @Test(priority = 5, description = "Все ссылки на слайдах валидны (начинаются с http)")
    public void checkAllSlideLinksAreValid() {
        List<WebElement> slides = swiperPage.getAllSlides();

        for (int i = 0; i < slides.size(); i++) {
            List<WebElement> links = slides.get(i).findElements(org.openqa.selenium.By.tagName("a"));
            for (int j = 0; j < links.size(); j++) {
                String href = links.get(j).getAttribute("href");
                Assert.assertNotNull(href,
                        "Слайд #" + (i + 1) + " ссылка #" + (j + 1) + ": href не найден");
                Assert.assertTrue(href.startsWith("http"),
                        "Слайд #" + (i + 1) + " ссылка #" + (j + 1) +
                                ": ссылка не начинается с http. Фактически: " + href);
            }
        }
    }

    @Test(priority = 6, description = "Ссылки на слайдах уникальны")
    public void checkSlideLinksAreUnique() {
        List<WebElement> slides = swiperPage.getAllSlides();
        Set<String> uniqueLinks = new HashSet<>();

        Assert.assertFalse(slides.isEmpty(), "Слайды не найдены");

        for (int i = 0; i < slides.size(); i++) {
            List<WebElement> links = slides.get(i).findElements(org.openqa.selenium.By.tagName("a"));
            Assert.assertFalse(links.isEmpty(), "Слайд #" + (i + 1) + " не содержит ссылок");

            for (WebElement link : links) {
                String href = link.getAttribute("href");
                Assert.assertNotNull(href, "Слайд #" + (i + 1) + ": href не найден");
                Assert.assertFalse(href.isEmpty(), "Слайд #" + (i + 1) + ": href пустой");
                Assert.assertFalse(href.startsWith("javascript:"),
                        "Слайд #" + (i + 1) + ": ссылка использует javascript: " + href);
                Assert.assertFalse(uniqueLinks.contains(href),
                        "Дублирующаяся ссылка: " + href);
                uniqueLinks.add(href);
            }
        }
    }

    @Test(dataProvider = "slideLinks", dataProviderClass = ServicesSwiperTestData.class,
            priority = 7, description = "Проверка конкретных ссылок на слайдах")
    public void checkSpecificSlideLinks(int slideIndex, String slideName, String expectedLink) {
        List<WebElement> slides = swiperPage.getAllSlides();
        Assert.assertTrue(slideIndex < slides.size(),
                "Индекс слайда " + slideIndex + " выходит за границы. Всего слайдов: " + slides.size());

        List<WebElement> links = slides.get(slideIndex).findElements(org.openqa.selenium.By.tagName("a"));
        Assert.assertFalse(links.isEmpty(), "Слайд " + slideName + " (#" + slideIndex + ") не содержит ссылок");

        String actualLink = links.get(0).getAttribute("href");
        Assert.assertTrue(actualLink.contains(expectedLink),
                "Ссылка на слайде " + slideName + " не соответствует ожидаемой. " +
                        "Ожидаемо: " + expectedLink + ", Фактически: " + actualLink);
    }

    // ==================== NAVIGATION TESTS ====================

    @Test(priority = 8, description = "Свайп влево переключает слайд")
    public void checkSwipeLeftSwitchesSlide() {
        String firstActiveSlide = swiperPage.getActiveSlideClass();
        swiperPage.swipeToLeft();
        String secondActiveSlide = swiperPage.getActiveSlideClass();

        Assert.assertNotEquals(firstActiveSlide, secondActiveSlide,
                "Активный слайд не изменился после свайпа влево");
    }

    @Test(priority = 9, description = "Свайп вправо переключает слайд")
    public void checkSwipeRightSwitchesSlide() {
        String firstActiveSlide = swiperPage.getActiveSlideClass();
        swiperPage.swipeToRight();
        String secondActiveSlide = swiperPage.getActiveSlideClass();

        Assert.assertNotEquals(firstActiveSlide, secondActiveSlide,
                "Активный слайд не изменился после свайпа вправо");
    }

    @Test(dataProvider = "slideLinks", dataProviderClass = ServicesSwiperTestData.class,
            priority = 10, description = "Проверка ссылок на слайдах через Page Object")
    public void checkSlideLinks(int slideIndex, String slideName, String expectedLink) {
        String actualLink = swiperPage.getSlideLink(slideIndex);
        Assert.assertNotNull(actualLink, "Ссылка на слайде " + slideName + " не найдена");
        Assert.assertTrue(actualLink.contains(expectedLink),
                "Ссылка на слайде " + slideName + " не соответствует ожидаемой. " +
                        "Ожидаемо: " + expectedLink + ", Фактически: " + actualLink);
    }

    @Test(priority = 11, description = "Клик по слайду открывает ссылку")
    public void checkSlideClickOpensLink() {
        swiperPage.clickSlide(0);
        waitForLinkToOpen("/registration");

        String currentUrl = getDriver().getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("registration"),
                "Переход по ссылке слайда не произошёл. URL: " + currentUrl);

        closeNewWindowAndReturn();
    }

    // ==================== RESPONSIVE TESTS ====================

    @Test(dataProvider = "responsiveResolutions", dataProviderClass = ServicesSwiperTestData.class,
            priority = 12, description = "Swiper отображается на разных разрешениях")
    public void checkSwiperOnDifferentResolutions(int width, int height, String deviceName) {
        getDriver().manage().window().setSize(new Dimension(width, height));
        swiperPage.openMainPage().scrollToSwiper();

        Assert.assertTrue(swiperPage.isSwiperDisplayed(),
                "Swiper не отображается на " + deviceName + " (" + width + "x" + height + ")");
        Assert.assertTrue(swiperPage.hasMinimumSlides(3),
                "Недостаточно слайдов на " + deviceName);

        getDriver().manage().window().maximize();
    }

    @Test(priority = 13, description = "Swiper загружается после перезагрузки страницы")
    public void checkSwiperLoadsAfterPageReload() {
        swiperPage.openMainPage();
        Assert.assertTrue(swiperPage.waitForSwiperLoaded().isSwiperDisplayed(),
                "Swiper не загрузился после перезагрузки страницы");
    }

    // ==================== HELPERS ====================

    private void waitForLinkToOpen(String expectedUrlPart) {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(ServicesSwiperConstants.WINDOW_SWITCH_WAIT_SECONDS));
        int originalWindows = getDriver().getWindowHandles().size();

        Set<String> currentWindows = getDriver().getWindowHandles();
        if (currentWindows.size() > originalWindows) {
            for (String windowHandle : currentWindows) {
                if (!windowHandle.equals(originalWindowHandle)) {
                    getDriver().switchTo().window(windowHandle);
                    return;
                }
            }
        } else {
            wait.until(org.openqa.selenium.support.ui.ExpectedConditions.urlContains(expectedUrlPart));
        }
    }

    private void closeNewWindowAndReturn() {
        if (getDriver().getWindowHandles().size() > 1) {
            getDriver().close();
            getDriver().switchTo().window(originalWindowHandle);
        }
    }

}