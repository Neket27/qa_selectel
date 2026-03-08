package projects.pages;

import org.openqa.selenium.WebDriver;
import projects.constants.NavigationConstants;
import projects.pages.components.NavigationComponent;

public class MainPage extends BasePage {

    // === Components ===
    private final NavigationComponent navigation;

    public MainPage(WebDriver driver) {
        super(driver);
        this.navigation = new NavigationComponent(driver);
    }

    // === Navigation ===

    public MainPage open() {
        openUrl(NavigationConstants.MAIN_URL);
        return this;
    }

    // === Components Access ===

    public NavigationComponent getNavigation() {
        return navigation;
    }

}