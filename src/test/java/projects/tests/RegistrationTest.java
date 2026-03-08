package projects.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import projects.constants.RegistrationConstants;
import projects.pages.RegistrationPage;
import projects.tests.dataProvider.RegistrationTestData;


public class RegistrationTest extends BaseTest {

    private RegistrationPage registrationPage;

    @BeforeMethod
    public void initPage() {
        registrationPage = new RegistrationPage(getDriver());
        registrationPage.open();
    }

    // ==================== POSITIVE TESTS ====================

    @Test(description = "Проверка загрузки страницы регистрации")
    public void checkRegistrationPageLoads() {
        String title = getDriver().getTitle().toLowerCase();
        Assert.assertTrue(title.contains("selectel"),
                "Заголовок страницы не содержит 'Selectel'. Фактический: " + title);
    }

    @Test(description = "Проверка наличия всех обязательных элементов формы")
    public void checkAllRequiredFieldsExist() {
        Assert.assertTrue(registrationPage.isEmailFieldDisplayed(), "Поле Email не отображается");
        Assert.assertTrue(registrationPage.isPasswordFieldDisplayed(), "Поле Пароль не отображается");
        Assert.assertTrue(registrationPage.isRegisterButtonDisplayed(), "Кнопка регистрации не отображается");
    }

    @Test(description = "Успешная регистрация с валидными данными")
    public void registrationWithValidData() {
        registrationPage.register(
                RegistrationConstants.VALID_EMAIL,
                RegistrationConstants.VALID_PASSWORD
        );

        Assert.assertTrue(registrationPage.waitForRegistrationComplete(),
                "После регистрации нет редиректа на профиль. URL: " + getDriver().getCurrentUrl());
    }

    @Test(description = "Генерация пароля: отображаются требования")
    public void passwordGeneratorShowsRequirements() {
        registrationPage
                .fillPassword(RegistrationConstants.INVALID_PASSWORD_SHORT)
                .clickRegister();

        Assert.assertTrue(registrationPage.isPasswordRequirementsDisplayed(),
                "Индикатор требований пароля не отображается");
    }

    // ==================== NEGATIVE TESTS: Empty Fields ====================

    @Test(description = "Отправка пустой формы")
    public void submitEmptyForm() {
        registrationPage.clickRegister();

        assertValidationErrors(true, true);
    }

    @Test(description = "Пустая форма с чекбоксом рассылки")
    public void submitEmptyFormWithNewsletter() {
        registrationPage.acceptCheckbox().clickRegister();
        assertValidationErrors(true, true);
    }

    @Test(description = "Пустой email, валидный пароль")
    public void emptyEmailWithValidPassword() {
        registrationPage
                .fillPassword(RegistrationConstants.VALID_PASSWORD)
                .acceptCheckbox()
                .clickRegister();

        Assert.assertTrue(registrationPage.waitForEmailError().isEmailErrorDisplayed(),
                "Ошибка валидации email не отображается");
    }

    @Test(description = "Валидный email, пустой пароль")
    public void emptyPasswordWithValidEmail() {
        registrationPage
                .fillEmail(RegistrationConstants.VALID_EMAIL)
                .acceptCheckbox()
                .clickRegister();

        Assert.assertTrue(registrationPage.waitForPasswordError().isPasswordErrorDisplayed(),
                "Ошибка валидации пароля не отображается");
    }

    // ==================== EMAIL VALIDATION ====================

    @Test(dataProvider = "invalidEmails", dataProviderClass = RegistrationTestData.class,
            description = "Валидация невалидных email")
    public void invalidEmailValidation(String email, String testCase) {
        registrationPage.register(email, RegistrationConstants.VALID_PASSWORD);

        Assert.assertTrue(registrationPage.isEmailValidationPatternErrorDisplayed(),
                "Ошибка валидации не отображается для: " + testCase + " (email: " + email + ")");
    }

    //Bug?
    @Test(description = "Повторная регистрация с тем же email")
    public void duplicateEmailRegistration() {
        String email = RegistrationConstants.VALID_EMAIL_UNIQUE;

        registrationPage.register(email, RegistrationConstants.VALID_PASSWORD);

        registrationPage.open().register(email, RegistrationConstants.VALID_PASSWORD);

        Assert.assertFalse(registrationPage.isEmailErrorDisplayed(),
                "Ошибка регистрации");
    }

    // ==================== PASSWORD VALIDATION ====================

    @Test(dataProvider = "invalidPasswords", dataProviderClass = RegistrationTestData.class,
            description = "Валидация невалидных паролей")
    public void invalidPasswordValidation(String password, String expectedMessagePart) {
        registrationPage.register(RegistrationConstants.VALID_EMAIL, password);

        Assert.assertTrue(registrationPage.waitForPasswordRequirements().isPasswordRequirementsDisplayed(),
                "Ошибка валидации пароля не отображается");

        if (expectedMessagePart != null) {
            Assert.assertTrue(
                    registrationPage.getPasswordRequirementsText().contains(expectedMessagePart),
                    "Сообщение не содержит ожидаемого текста: " + expectedMessagePart
            );
        }
    }

    // ==================== SECURITY TESTS ====================

    //Bug
    @Test(dataProvider = "securityPayloads", dataProviderClass = RegistrationTestData.class,
            description = "Проверка защиты от инъекций в email")
    public void emailInjectionProtection(String payload, String injectionType) {
        registrationPage
                .fillEmail(payload)
                .fillPassword(RegistrationConstants.VALID_PASSWORD)
                .acceptCheckbox()
                .clickRegister();

        Assert.assertTrue(registrationPage.isAnyEmailErrorDisplayed(),
                injectionType + " должна быть отклонена валидацией");

        registrationPage.open();
    }

    @Test(description = "Проверка HTTPS соединения")
    public void checkHttpsConnection() {
        String url = getDriver().getCurrentUrl();
        Assert.assertTrue(url.startsWith("https://"),
                "Страница не использует HTTPS: " + url);
    }

    @Test(description = "Поле пароля имеет тип 'password'")
    public void checkPasswordFieldType() {
        String type = registrationPage.getPasswordFieldType();
        Assert.assertEquals(type, "password", "Поле пароля имеет тип '%s' вместо 'password'");
    }

    // ==================== UI/UX TESTS ====================

    @Test(description = "Очистка полей при перезагрузке страницы")
    public void fieldsPersistenceOnReload() {
        registrationPage
                .fillEmail(RegistrationConstants.VALID_EMAIL)
                .fillPassword(RegistrationConstants.VALID_PASSWORD)
                .open();

        String email = registrationPage.getEmailFieldValue();
        String password = registrationPage.getPasswordFieldValue();

        Assert.assertEquals(email, RegistrationConstants.VALID_EMAIL, "Email должен сохраняться после перезагрузки");
        Assert.assertTrue(password.isEmpty(), "Пароль не должен сохраняться после перезагрузки");
    }

    // ==================== HELPERS ====================

    private void assertValidationErrors(boolean emailError, boolean passwordError) {
        Assert.assertEquals(registrationPage.isEmailErrorDisplayed(), emailError, "Email error");
        Assert.assertEquals(registrationPage.isPasswordErrorDisplayed(), passwordError, "Password error");
    }
}