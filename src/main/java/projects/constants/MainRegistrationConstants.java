package projects.constants;

public class MainRegistrationConstants extends BaseConstants {

    // URLs
    public static final String MAIN_URL = "https://selectel.ru";
    public static final String REGISTRATION_URL = "https://my.selectel.ru/registration";
    public static final String PROFILE_URL = "https://my.selectel.ru/profile";

    // Test data - Valid
    public static final String VALID_EMAIL = "test_selectel." + System.currentTimeMillis() + "@mail.ru";
    public static final String VALID_PASSWORD = "TestPass123!@#";

    // Test data - Invalid emails
    public static final String INVALID_EMAIL_NO_AT = "test_selectelmail.ru";
    public static final String INVALID_EMAIL_NO_DOMAIN = "test_selectel@mail";
    public static final String INVALID_EMAIL_CYRILLIC = "тест_selectel@mail.ru";

    // Test data - Invalid passwords
    public static final String INVALID_PASSWORD_SHORT = "Test1!";                  // <12 символов
    public static final String INVALID_PASSWORD_NO_SPECIAL = "TestPass1234";       // без спецсимвола

    // Error messages
    public static final String ERROR_EMAIL_REQUIRED = "Введите email";
    public static final String ERROR_EMAIL_INVALID = "некорректный";
    public static final String ERROR_PASSWORD_REQUIRED = "пароль";
    public static final String ERROR_PASSWORD_REQUIREMENTS = "12 символов";

    // Timeouts
    public static final long EXPLICIT_WAIT_SECONDS = 15;
    public static final long WINDOW_SWITCH_WAIT_SECONDS = 15;
}