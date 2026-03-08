package projects.pages.components;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import projects.constants.NavigationConstants;
import projects.pages.BasePage;

import java.util.ArrayList;
import java.util.List;

public class NavigationComponent extends BasePage {

    public NavigationComponent(WebDriver driver) {
        super(driver);
    }

    // === Locators ===
    private static final By DROPDOWN_CONTAINER = By.cssSelector(".main-nav__dropdown--visible");
    private static final By DROPDOWN_VISIBLE = By.cssSelector(".nav-dropdown[opened-menu='products']");
    private static final By MENU_LINKS = By.cssSelector(".menu-link__title, .nav-dropdown__categories-item a, nav a");
    public static final By NAV_PRODUCTS = By.cssSelector("[data-qa='main-nav__group-products']");
    public static final By NAV_SOLUTIONS = By.cssSelector("[data-qa='main-nav__group-solutions']");
    public static final By NAV_SELECTEL = By.cssSelector("[data-qa='main-nav__group-selectel']");
    public static final By NAV_PRICES = By.cssSelector("[data-qa='main-nav__group-prices']");
    public static final By NAV_ACADEMY = By.cssSelector("[data-qa='main-nav__group-academy']");
    public static final By NAV_DOCUMENTATION = By.cssSelector("[data-qa='main-nav__link-documentation']");
    public static final By NAV_CASES = By.cssSelector("[data-qa='main-nav__link-cases']");

    // === Actions ===

    public NavigationComponent clickNavItem(By navButtonLocator) {
        click(navButtonLocator);
        return this;
    }
    public NavigationComponent clickNavItemWithDropdown(By navButtonLocator) {
        click(navButtonLocator);
        return this;
    }
    public NavigationComponent clickDropdownLink(String linkText) {
        WebElement link = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath(String.format("//a[contains(.,'%s')]", linkText))
                )
        );
        link.click();
        return this;
    }

    // === Validation Methods ===

    public boolean isNavItemDisplayed(By navButtonLocator) {
        return isDisplayed(navButtonLocator);
    }

    public boolean isDropdownVisible() {
        try {
            return isDisplayed(DROPDOWN_CONTAINER);
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public List<String> getDropdownLinksText() {
        List<WebElement> links = findElements(MENU_LINKS);
        List<String> linkTexts = new ArrayList<>();

        for (WebElement link : links) {
            String text = link.getText().trim();
            if (!text.isEmpty()) {
                linkTexts.add(text);
            }
        }

        return linkTexts;
    }

    public boolean dropdownContainsExpectedItems(String[] expectedItems) {
        List<String> actualLinks = getDropdownLinksText();

        for (String expected : expectedItems) {
            boolean found = actualLinks.stream()
                    .anyMatch(actual -> actual.contains(expected));
            if (!found) {
                return false;
            }
        }
        return true;
    }

    public void waitForDropdownVisible() {
        waitUntilVisible(DROPDOWN_VISIBLE);
    }
    public void waitForDropdownHidden() {
       waitUntilHidden(DROPDOWN_VISIBLE);
    }
}