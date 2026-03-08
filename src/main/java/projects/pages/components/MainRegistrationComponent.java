package projects.pages.components;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import projects.constants.RegistrationConstants;
import projects.pages.BasePage;

import java.util.List;

public class MainRegistrationComponent extends BasePage {

    // === Locators: Email ===
    private static final By EMAIL_INPUT = By.cssSelector("input[name='email']");
    private static final By EMAIL_ERROR = By.cssSelector("label.input-label--error span.input-label__error");
    private static final By EMAIL_LABEL = By.xpath("//form[@class='registration-form']//label[1]");

    // === Locators: Password ===
    private static final By PASSWORD_INPUT = By.cssSelector("input[type='password']");
    private static final By PASSWORD_TOOLTIP = By.xpath("//form/label[2]/span[3]");
    private static final By PASSWORD_LABEL = By.xpath("//form/label[2]/span[1]");
    private static final By PASSWORD_ERROR = By.xpath("//form/label[2]/span[3]");

    // === Locators: Checkbox ===
    private static final By NEWS_CHECKBOX_FOR_CLICK = By.xpath("//form/label[3]/span[1]");
    private static final By NEWS_CHECKBOX_INPUT = By.xpath("//form/label[3]/span[1]/input");

    // === Locators: Button ===
    private static final By REGISTER_BUTTON = By.xpath("//form/button");

    // === Locators: General Errors ===
    private static final By GENERAL_ERROR = By.cssSelector(".ant-alert-error");

    public MainRegistrationComponent(WebDriver driver) {
        super(driver);
    }

    // === Navigation ===

    public MainRegistrationComponent open() {
        openUrl(RegistrationConstants.BASE_URL);
        return this;
    }

    public MainRegistrationComponent openRegistrationPage() {
        openUrl(RegistrationConstants.REGISTRATION_URL);
        return this;
    }

    // === Fluent Fill Methods ===

    public MainRegistrationComponent fillEmail(String email) {
        clearAndSendKeys(EMAIL_INPUT, email);
        return this;
    }

    public MainRegistrationComponent fillPassword(String password) {
        WebElement passwordInput = findElement(PASSWORD_INPUT);
        passwordInput.clear();
        passwordInput.sendKeys(password);
        return this;
    }

    // === Checkbox Methods ===

    public MainRegistrationComponent acceptTerms() {
        if (isDisplayed(NEWS_CHECKBOX_FOR_CLICK)) {
            click(NEWS_CHECKBOX_FOR_CLICK);
        }
        return this;
    }

    // === Actions ===

    public MainRegistrationComponent clickRegister() {
        waitUntilClickable(REGISTER_BUTTON);
        click(REGISTER_BUTTON);
        return this;
    }

    // === Registration Flow ===

    public MainRegistrationComponent register(String email, String password) {
        return fillEmail(email)
                .fillPassword(password)
                .acceptTerms()
                .clickRegister();
    }

    // === Validation Checks ===

    public boolean isEmailFieldDisplayed() {
        return isDisplayed(EMAIL_INPUT);
    }

    public boolean isPasswordFieldDisplayed() {
        return isDisplayed(PASSWORD_INPUT);
    }

    public boolean isRegisterButtonDisplayed() {
        return isDisplayed(REGISTER_BUTTON);
    }

    public boolean isRegisterButtonEnabled() {
        return isEnabled(REGISTER_BUTTON);
    }

    public boolean isNewsCheckboxDisplayed() {
        return isDisplayed(NEWS_CHECKBOX_FOR_CLICK);
    }

    public boolean isTermsCheckboxSelected() {
        try {
            WebElement checkbox = findElement(NEWS_CHECKBOX_INPUT);
            return checkbox.isSelected();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isEmailErrorDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(EMAIL_ERROR));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean isPasswordErrorDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(PASSWORD_ERROR));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean isGeneralErrorDisplayed() {
        return isDisplayed(GENERAL_ERROR);
    }

    public boolean isPasswordRequirementsDisplayed() {
        return isDisplayed(PASSWORD_TOOLTIP);
    }

    // === Getters ===

    public String getEmailValue() {
        return getInputValue(EMAIL_INPUT);
    }

    public String getEmailErrorText() {
        try {
            return getText(EMAIL_ERROR).trim();
        } catch (Exception e) {
            return "";
        }
    }

    public String getPasswordErrorText() {
        try {
            return getText(PASSWORD_ERROR).trim();
        } catch (Exception e) {
            return "";
        }
    }

    public String getRegisterButtonText() {
        return getText(REGISTER_BUTTON);
    }

    public String getPlaceholderEmailField() {
        return getText(EMAIL_LABEL).trim();
    }

    public String getPlaceholderPasswordField() {
        return getText(PASSWORD_LABEL).trim();
    }

    public WebElement getEmailField() {
        return findElement(EMAIL_INPUT);
    }

    public WebElement getPasswordField() {
        List<WebElement> inputs = driver.findElements(PASSWORD_INPUT);
        return inputs.isEmpty() ? null : inputs.get(0);
    }

    // === Wait Methods (fluent) ===

    public MainRegistrationComponent waitForEmailError() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(EMAIL_ERROR));
        return this;
    }

    public MainRegistrationComponent waitForPasswordError() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(PASSWORD_ERROR));
        return this;
    }

    public void waitForRegistrationPage() {
        wait.until(ExpectedConditions.urlContains("/registration"));
    }

    public void waitForProfilePage() {
        wait.until(ExpectedConditions.urlContains("/profile"));
    }
}