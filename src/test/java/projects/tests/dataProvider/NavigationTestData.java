package projects.tests.dataProvider;

import org.testng.annotations.DataProvider;

import static projects.constants.NavigationConstants.ACADEMY_MENU_ITEMS;
import static projects.constants.NavigationConstants.PRODUCTS_MENU_ITEMS;
import static projects.pages.components.NavigationComponent.NAV_ACADEMY;
import static projects.pages.components.NavigationComponent.NAV_PRODUCTS;

public class NavigationTestData {

    @DataProvider(name = "productLinks")
    public static Object[][] productLinks() {
        return new Object[][]{
                {"Выделенные серверы"},
                {"Облачные серверы"},
                {"Облачные базы данных"},
        };
    }

    @DataProvider(name = "resolutions")
    public static Object[][] resolutions() {
        return new Object[][]{
                {1920, 1080, "Desktop"},
                {1366, 768, "Laptop"},
                {768, 1024, "Tablet"},
        };
    }

    @DataProvider(name = "dropdownItems")
    public static Object[][] dropdownItems() {
        return new Object[][]{
                {NAV_PRODUCTS, PRODUCTS_MENU_ITEMS},
                {NAV_ACADEMY, ACADEMY_MENU_ITEMS},
        };
    }
}