package projects.tests;

import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import projects.constants.MainRegistrationConstants;
import projects.pages.components.MainRegistrationComponent;
import projects.tests.dataProvider.RegistrationTestData;

import java.time.Duration;

public class MainRegistrationTest extends BaseTest {

    private MainRegistrationComponent mainPage;
    private String originalWindow;

    @BeforeMethod
    public void initPage() {
        mainPage = new MainRegistrationComponent(getDriver());
        mainPage.open();
        originalWindow = getDriver().getWindowHandle();
    }

    // ==================== POSITIVE TESTS ====================

    @Test(priority = 1, description = "Проверка загрузки главной страницы с формой регистрации")
    public void checkMainPageLoads() {
        String title = getDriver().getTitle().toLowerCase();
        Assert.assertTrue(title.contains("selectel"),
                "Заголовок страницы не содержит 'Selectel'. Фактический: " + title);
    }

    @Test(priority = 2, description = "Проверка наличия всех элементов формы регистрации")
    public void checkAllFormElementsExist() {
        Assert.assertTrue(mainPage.isEmailFieldDisplayed(), "Поле Email не отображается");
        Assert.assertTrue(mainPage.isPasswordFieldDisplayed(), "Поле Пароль не отображается");
        Assert.assertTrue(mainPage.isNewsCheckboxDisplayed(), "Чекбокс новостных рассылок не отображается");
        Assert.assertTrue(mainPage.isRegisterButtonDisplayed(), "Кнопка регистрации не отображается");
    }

    @Test(priority = 3, description = "Проверка текста кнопки регистрации")
    public void checkRegisterButtonText() {
        String buttonText = mainPage.getRegisterButtonText();
        Assert.assertTrue(buttonText.contains("Создать аккаунт"),
                "Текст кнопки не содержит 'Создать аккаунт'. Фактический: " + buttonText);
    }

    @Test(priority = 4, description = "Переход в профиль после регистрации через главную страницу")
    public void checkRedirectToProfileAfterRegistration() {
        String testEmail = "test_selectel." + System.currentTimeMillis() + "@mail.ru";

        mainPage.register(testEmail, MainRegistrationConstants.VALID_PASSWORD);

        waitForNewWindowAndSwitch();

        String currentUrl = getDriver().getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("/profile"),
                "Нет перехода в кабинет пользователя. URL: " + currentUrl);

        closeNewWindowAndReturn();
    }

    // ==================== NEGATIVE TESTS: Empty Fields ====================

    @Test(priority = 5, description = "Отправка пустой формы")
    public void submitEmptyForm() {
        mainPage.clickRegister();

        assertValidationErrors(true, false);
    }

    @Test(priority = 6, description = "Пустой email с заполненным паролем")
    public void emptyEmailWithValidPassword() {
        mainPage.fillPassword(MainRegistrationConstants.VALID_PASSWORD)
                .acceptTerms()
                .clickRegister();

        Assert.assertTrue(mainPage.waitForEmailError().isEmailErrorDisplayed(),
                "Ошибка валидации email не отображается");
        Assert.assertTrue(mainPage.getEmailErrorText().contains("email"),
                "Текст ошибки не содержит 'email'. Фактический: " + mainPage.getEmailErrorText());
    }

    @Test(priority = 7, description = "Пустой пароль с заполненным email")
    public void emptyPasswordWithValidEmail() {
        mainPage.fillEmail(MainRegistrationConstants.VALID_EMAIL)
                .acceptTerms()
                .clickRegister();

        Assert.assertTrue(mainPage.isRegisterButtonDisplayed(),
                "Кнопка регистрации не отображается");
    }

    @Test(priority = 8, description = "Регистрация без принятия новостных соглашений")
    public void withoutNewsAcceptance() {
        String testEmail = "test_selectel." + System.currentTimeMillis() + "@mail.ru";

        mainPage.fillEmail(testEmail)
                .fillPassword(MainRegistrationConstants.VALID_PASSWORD)
                .clickRegister();

        waitForNewWindowAndSwitch();

        String currentUrl = getDriver().getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("/profile"),
                "Не перешли на страницу профиля. URL: " + currentUrl);

        closeNewWindowAndReturn();
    }

    // ==================== EMAIL VALIDATION TESTS ====================

    @Test(dataProvider = "invalidEmails", dataProviderClass = RegistrationTestData.class,
            priority = 9, description = "Валидация невалидных email")
    public void invalidEmailValidation(String email, String testCase) {
        mainPage.register(email, MainRegistrationConstants.VALID_PASSWORD);

        Assert.assertTrue(mainPage.isEmailErrorDisplayed(),
                "Ошибка валидации не отображается для: " + testCase);
    }

    // ==================== PASSWORD VALIDATION TESTS ====================

    @Test(dataProvider = "invalidPasswords", dataProviderClass = RegistrationTestData.class,
            priority = 10, description = "Валидация невалидных паролей")
    public void invalidPasswordValidation(String password, String testCase) {
        mainPage.fillEmail(MainRegistrationConstants.VALID_EMAIL)
                .fillPassword(password)
                .clickRegister();

        Assert.assertTrue(mainPage.isPasswordRequirementsDisplayed(),
                "Требования к паролю не отображаются: " + testCase);
    }

    // ==================== UI/UX TESTS ====================

    @Test(priority = 11, description = "Проверка placeholder email поля")
    public void checkEmailPlaceholder() {
        String placeholder = mainPage.getPlaceholderEmailField();
        Assert.assertEquals(placeholder, "Электронная почта",
                "Placeholder email поля не соответствует ожидаемому");
    }

    @Test(priority = 12, description = "Проверка placeholder password поля")
    public void checkPasswordPlaceholder() {
        String placeholder = mainPage.getPlaceholderPasswordField();
        Assert.assertTrue(placeholder.contains("Придумайте пароль"),
                "Placeholder password поля не соответствует ожидаемому. Фактический: " + placeholder);
    }

    @Test(priority = 13, description = "Проверка maxlength email поля")
    public void checkEmailMaxlength() {
        String maxlength = mainPage.getEmailField().getAttribute("maxlength");
        Assert.assertEquals(maxlength, "250",
                "Maxlength email поля не равен 250. Фактический: " + maxlength);
    }

    @Test(priority = 14, description = "Проверка type email поля")
    public void checkEmailType() {
        String type = mainPage.getEmailField().getAttribute("type");
        Assert.assertEquals(type, "email",
                "Type email поля не равен 'email'. Фактический: " + type);
    }

    @Test(priority = 15, description = "Проверка что чекбокс не выбран по умолчанию")
    public void checkCheckboxNotSelectedByDefault() {
        Assert.assertFalse(mainPage.isTermsCheckboxSelected(),
                "Чекбокс новостей не должен быть выбран по умолчанию");
    }

    @Test(priority = 16, description = "Проверка возможности выбрать чекбокс")
    public void checkCheckboxCanBeSelected() {
        mainPage.acceptTerms();

        Assert.assertTrue(mainPage.isTermsCheckboxSelected(),
                "Чекбокс должен быть выбран после клика");
    }

    @Test(priority = 17, description = "Проверка HTTPS соединения")
    public void checkHttpsConnection() {
        String currentUrl = getDriver().getCurrentUrl();
        Assert.assertTrue(currentUrl.startsWith("https://"),
                "Страница не использует HTTPS: " + currentUrl);
    }

    @Test(priority = 18, description = "Очистка полей при перезагрузке страницы")
    public void checkFieldsClearOnPageReload() {
        mainPage.fillEmail(MainRegistrationConstants.VALID_EMAIL)
                .fillPassword(MainRegistrationConstants.VALID_PASSWORD)
                .open();

        String emailValue = mainPage.getEmailField().getAttribute("value");
        Assert.assertTrue(emailValue.isEmpty(),
                "Поля формы не очищаются при перезагрузке страницы");
    }

    // ==================== RESPONSIVE TESTS ====================

    @Test(priority = 19, description = "Проверка отображения формы на мобильном разрешении")
    public void checkFormOnMobileResolution() {
        getDriver().manage().window().setSize(new org.openqa.selenium.Dimension(375, 667));

        mainPage.open();

        Assert.assertTrue(mainPage.isEmailFieldDisplayed(),
                "Email поле не отображается на мобильном разрешении");
        Assert.assertTrue(mainPage.isRegisterButtonDisplayed(),
                "Кнопка регистрации не отображается на мобильном разрешении");

        getDriver().manage().window().maximize();
    }

    @Test(priority = 20, description = "Проверка отображения формы на планшетном разрешении")
    public void checkFormOnTabletResolution() {
        getDriver().manage().window().setSize(new org.openqa.selenium.Dimension(768, 1024));

        mainPage.open();

        Assert.assertTrue(mainPage.isEmailFieldDisplayed(),
                "Email поле не отображается на планшетном разрешении");
        Assert.assertTrue(mainPage.isRegisterButtonDisplayed(),
                "Кнопка регистрации не отображается на планшетном разрешении");

        getDriver().manage().window().maximize();
    }

    // ==================== HELPERS ====================

    private void waitForNewWindowAndSwitch() {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(MainRegistrationConstants.WINDOW_SWITCH_WAIT_SECONDS));
        wait.until(driver -> driver.getWindowHandles().size() > 1);

        for (String windowHandle : getDriver().getWindowHandles()) {
            if (!windowHandle.equals(originalWindow)) {
                getDriver().switchTo().window(windowHandle);
                break;
            }
        }
    }

    private void closeNewWindowAndReturn() {
        getDriver().close();
        getDriver().switchTo().window(originalWindow);
    }

    private void assertValidationErrors(boolean emailError, boolean passwordError) {
        Assert.assertEquals(mainPage.isEmailErrorDisplayed(), emailError, "Email error mismatch");
        Assert.assertEquals(mainPage.isPasswordErrorDisplayed(), passwordError, "Password error mismatch");
    }
}