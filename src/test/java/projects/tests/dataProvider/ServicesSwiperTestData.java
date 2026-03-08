package projects.tests.dataProvider;

import org.testng.annotations.DataProvider;

public class ServicesSwiperTestData {

    @DataProvider(name = "responsiveResolutions")
    public static Object[][] responsiveResolutions() {
        return new Object[][]{
                {375, 667, "Mobile"},
                {768, 1024, "Tablet"},
                {1920, 1080, "Desktop"},
        };
    }

    @DataProvider(name = "slideLinks")
    public static Object[][] slideLinks() {
        return new Object[][]{
                {0, "registration", "https://my.selectel.ru/registration"},
                {1, "calculator", "https://selectel.ru/prices/calculator/"},
        };
    }
}