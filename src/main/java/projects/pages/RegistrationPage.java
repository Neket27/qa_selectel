package projects.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import projects.constants.RegistrationConstants;

public class RegistrationPage extends BasePage {

    // === Locators ===
    private static final By EMAIL_FIELD = By.id("email");
    private static final By PASSWORD_FIELD = By.id("new-password");
    private static final By CHECKBOX = By.cssSelector("input[type='checkbox']");
    private static final By REGISTER_BUTTON = By.cssSelector("button[type='submit']");

    // === Error Locators ===
    private static final By EMAIL_REQUIRED_ERROR = By.cssSelector("[stl='registration__email_input__required_err']");
    private static final By EMAIL_PATTERN_ERROR = By.cssSelector("[stl='registration__email_input__pattern_err']");
    private static final By PASSWORD_REQUIRED_ERROR = By.cssSelector("[stl='registration__password_input__required_err']");
    private static final By PASSWORD_REQUIREMENTS = By.cssSelector("[stl='registration__password_input__pattern_err']");

    public RegistrationPage(WebDriver driver) {
        super(driver);
    }

    // === Navigation ===
    public RegistrationPage open() {
        openUrl(RegistrationConstants.REGISTRATION_URL);
        return this;
    }

    // === Fluent Fill Methods ===
    public RegistrationPage fillEmail(String email) {
        clearAndSendKeys(EMAIL_FIELD, email);
        return this;
    }

    public RegistrationPage fillPassword(String password) {
        clearAndSendKeys(PASSWORD_FIELD, password);
        return this;
    }

    // === Checkbox Methods ===
    public RegistrationPage acceptCheckbox() {
        if (isDisplayed(CHECKBOX)) {
            click(CHECKBOX);
        }
        return this;
    }

    public RegistrationPage acceptAllCheckboxes() {
        return acceptCheckbox(); // Унифицировано
    }

    // === Actions ===
    public RegistrationPage clickRegister() {
        waitUntilClickable(REGISTER_BUTTON);
        click(REGISTER_BUTTON);
        return this;
    }

    // === Registration Flow ===
    public RegistrationPage register(String email, String password) {
        return fillEmail(email)
                .fillPassword(password)
                .acceptAllCheckboxes()
                .clickRegister();
    }

    // === Validation Checks ===
    public boolean isEmailFieldDisplayed() { return isDisplayed(EMAIL_FIELD); }
    public boolean isPasswordFieldDisplayed() { return isDisplayed(PASSWORD_FIELD); }
    public boolean isRegisterButtonDisplayed() { return isDisplayed(REGISTER_BUTTON); }

    public boolean isEmailErrorDisplayed() { return isDisplayed(EMAIL_REQUIRED_ERROR); }
    public boolean isPasswordErrorDisplayed() { return isDisplayed(PASSWORD_REQUIRED_ERROR); }
    public boolean isPasswordRequirementsDisplayed() { return isDisplayed(PASSWORD_REQUIREMENTS); }

    public boolean isAnyEmailErrorDisplayed() {
        return isDisplayed(EMAIL_REQUIRED_ERROR) || isDisplayed(EMAIL_PATTERN_ERROR);
    }

    public boolean isEmailValidationPatternErrorDisplayed() {
        return findElement(EMAIL_PATTERN_ERROR).isDisplayed();
    }

    // === Getters ===
    public String getEmailFieldValue() {
        return findElement(EMAIL_FIELD).getAttribute("value");
    }

    public String getPasswordFieldValue() {
        return findElement(PASSWORD_FIELD).getAttribute("value");
    }

    public String getPasswordFieldType() {
        return findElement(PASSWORD_FIELD).getAttribute("type");
    }

    public String getPasswordRequirementsText() {
        return getText(PASSWORD_REQUIREMENTS);
    }

    // === Wait Methods (fluent) ===
    public RegistrationPage waitForEmailError() {
        waitUntilVisible(EMAIL_REQUIRED_ERROR);
        return this;
    }

    public RegistrationPage waitForPasswordError() {
        waitUntilVisible(PASSWORD_REQUIRED_ERROR);
        return this;
    }

    public RegistrationPage waitForPasswordRequirements() {
        waitUntilVisible(PASSWORD_REQUIREMENTS);
        return this;
    }

    public boolean waitForRegistrationComplete() {
        return waitForUrlContains("/profile");
    }
}