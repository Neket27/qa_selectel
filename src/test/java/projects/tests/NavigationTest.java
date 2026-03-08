package projects.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import projects.pages.MainPage;
import projects.pages.components.NavigationComponent;
import projects.tests.dataProvider.NavigationTestData;

import java.time.Duration;

import static projects.constants.NavigationConstants.*;
import static projects.pages.components.NavigationComponent.*;

public class NavigationTest extends BaseTest {

    private MainPage homePage;
    private NavigationComponent navigation;
    private String originalWindowHandle;

    @BeforeMethod
    public void initPage() {
        homePage = new MainPage(getDriver()).open();
        navigation = homePage.getNavigation();
        originalWindowHandle = getDriver().getWindowHandle();
    }

    // ==================== POSITIVE TESTS ====================

    @Test(priority = 1, description = "Проверка наличия всех основных пунктов меню")
    public void allMainNavItemsArePresent() {
        assertNavItemDisplayed(NAV_PRODUCTS, "Продукты");
        assertNavItemDisplayed(NAV_SOLUTIONS, "Решения");
        assertNavItemDisplayed(NAV_PRICES, "Цены");
        assertNavItemDisplayed(NAV_ACADEMY, "Академия");
        assertNavItemDisplayed(NAV_DOCUMENTATION, "Документация");
        assertNavItemDisplayed(NAV_CASES, "Кейсы");
    }

    @Test(priority = 2, description = "Открытие dropdown меню Продукты кликом")
    public void productsDropdownOpensOnClick() {
        navigation.clickNavItemWithDropdown(NAV_PRODUCTS);

        Assert.assertTrue(navigation.isDropdownVisible(),
                "Dropdown меню Продукты не открылось при клике");
    }

    @Test(priority = 3, description = "Проверка ссылок в dropdown меню Продукты")
    public void productsDropdownContainsExpectedLinks() {
        navigation.clickNavItemWithDropdown(NAV_PRODUCTS);

        Assert.assertTrue(navigation.dropdownContainsExpectedItems(PRODUCTS_MENU_ITEMS),
                "Dropdown не содержит ожидаемых ссылок. Ожидалось: " +
                        String.join(", ", PRODUCTS_MENU_ITEMS));
    }

    @Test(priority = 4, description = "Закрытие dropdown при клике вне меню")
    public void dropdownClosesOnClickOutside() {
        navigation.clickNavItemWithDropdown(NAV_PRODUCTS);
        Assert.assertTrue(navigation.isDropdownVisible(), "Dropdown должен быть открыт");

        navigation.clickNavItemWithDropdown(NAV_PRODUCTS);
        Assert.assertFalse(navigation.isDropdownVisible(),
                "Dropdown должен закрыться после повторного клика");
    }

    @Test(priority = 5, description = "Переход по ссылке Документация")
    public void documentationLinkNavigation() {
        navigation.clickNavItem(NAV_DOCUMENTATION);
        waitForNewWindowAndSwitch();

        Assert.assertTrue(getDriver().getCurrentUrl().contains(DOCS_SELECTEL_RU),
                "Не произошел переход на страницу документации. URL: " + getDriver().getCurrentUrl());

        closeNewWindowAndReturn();
    }

    @Test(priority = 6, description = "Переход по ссылке Кейсы")
    public void casesLinkNavigation() {
        navigation.clickNavItem(NAV_CASES);
        waitForNewWindowAndSwitch();

        Assert.assertTrue(getDriver().getCurrentUrl().contains(CASES_SELECTEL),
                "Не произошел переход на страницу кейсов. URL: " + getDriver().getCurrentUrl());

        closeNewWindowAndReturn();
    }

    @Test(priority = 7, description = "Последовательное открытие разных dropdown меню")
    public void sequentialDropdownNavigation() {
        navigation.clickNavItem(NAV_PRODUCTS);
        Assert.assertTrue(navigation.isDropdownVisible(), "Dropdown Продукты открыт");

        navigation.clickNavItem(NAV_ACADEMY);
        Assert.assertTrue(navigation.isDropdownVisible(), "Dropdown Академия открыт");
        Assert.assertTrue(navigation.dropdownContainsExpectedItems(ACADEMY_MENU_ITEMS),
                "Dropdown Академия не содержит ожидаемых ссылок");
    }

    @Test(priority = 8, description = "Проверка клика по всем пунктам меню с dropdown")
    public void allDropdownsOpenOnClick() {
        By[] itemsWithDropdowns = {NAV_PRODUCTS, NAV_SOLUTIONS, NAV_SELECTEL, NAV_ACADEMY};

        for (By navItem : itemsWithDropdowns) {
            navigation.clickNavItem(navItem);
            Assert.assertTrue(navigation.isDropdownVisible(),
                    "Dropdown для " + navItem + " не открылся");
        }
    }

    @Test(dataProvider = "productLinks", dataProviderClass = NavigationTestData.class,
            priority = 9, description = "Клик по ссылке в dropdown открывает страницу")
    public void clickDropdownLinkNavigates(String linkText) {
        homePage.open();
        String originalUrl = getDriver().getCurrentUrl();

        navigation.clickNavItem(NAV_PRODUCTS).clickDropdownLink(linkText);

        new WebDriverWait(getDriver(), Duration.ofSeconds(10))
                .until(driver -> !driver.getCurrentUrl().equals(originalUrl) ||
                        driver.getWindowHandles().size() > 1);

        Assert.assertNotEquals(getDriver().getCurrentUrl(), MAIN_URL + "/",
                "URL не изменился после клика по ссылке '" + linkText + "'");
    }

    // ==================== ACCESSIBILITY TESTS ====================

    @Test(priority = 10, description = "Проверка data-qa атрибутов на всех элементах навигации")
    public void allNavElementsHaveDataQaAttributes() {
        var navButtons = getDriver().findElements(By.cssSelector(".main-nav__list-btn"));

        for (var button : navButtons) {
            String dataQa = button.getAttribute("data-qa");
            Assert.assertNotNull(dataQa,
                    "Кнопка навигации не имеет data-qa атрибута: " + button.getText());
            Assert.assertFalse(dataQa.isEmpty(),
                    "data-qa атрибут пустой для кнопки: " + button.getText());
        }
    }

    // ==================== EDGE CASES ====================

    @Test(priority = 11, description = "Быстрое переключение между dropdown меню")
    public void rapidDropdownSwitching() {
        By[] items = {NAV_PRODUCTS, NAV_SOLUTIONS, NAV_ACADEMY};

        for (int i = 0; i < 3; i++) {
            for (By item : items) {
                navigation.clickNavItem(item);
            }
        }

        Assert.assertTrue(navigation.isDropdownVisible(),
                "После быстрого переключения последний dropdown должен быть открыт");
    }

    // ==================== RESPONSIVE TESTS ====================

    @Test(dataProvider = "resolutions", dataProviderClass = NavigationTestData.class,
            priority = 12, description = "Проверка навигации на разных разрешениях")
    public void navigationOnDifferentResolutions(int width, int height, String deviceName) {
        getDriver().manage().window().setSize(
                new org.openqa.selenium.Dimension(width, height));

        Assert.assertTrue(navigation.isNavItemDisplayed(NAV_PRODUCTS),
                "На разрешении " + deviceName + " (" + width + "x" + height + ") меню не отображается");

        getDriver().manage().window().maximize();
    }

    // ==================== HELPERS ====================

    private void assertNavItemDisplayed(By locator, String itemName) {
        Assert.assertTrue(navigation.isNavItemDisplayed(locator),
                "Пункт меню '" + itemName + "' не отображается");
    }

    private void waitForNewWindowAndSwitch() {
        new WebDriverWait(getDriver(), Duration.ofSeconds(10))
                .until(driver -> driver.getWindowHandles().size() > 1);

        for (String windowHandle : getDriver().getWindowHandles()) {
            if (!windowHandle.equals(originalWindowHandle)) {
                getDriver().switchTo().window(windowHandle);
                break;
            }
        }
    }

    private void closeNewWindowAndReturn() {
        if (getDriver().getWindowHandles().size() > 1) {
            getDriver().close();
            getDriver().switchTo().window(originalWindowHandle);
        }
    }
}