package projects.constants;

public class RegistrationConstants extends BaseConstants {

    // URLs
    public static final String REGISTRATION_URL = "https://my.selectel.ru/registration";
    public static final String PROFILE_URL = "https://my.selectel.ru/profile/profile";

    // Password policy
    public static final int PASSWORD_MIN_LENGTH = 12;

    // Valid test data
    public static final String VALID_EMAIL = "test_selectel" + System.currentTimeMillis() + "@mail.ru";
    public static final String VALID_PASSWORD = "TestPass123!@#";
    public static final String VALID_EMAIL_UNIQUE = "test_selectel." + System.currentTimeMillis() + "@mail.ru";

    // Invalid emails
    public static final String INVALID_EMAIL_NO_AT = "test_selectelmail.ru";
    public static final String INVALID_EMAIL_NO_DOMAIN = "test_selectel@mail";
    public static final String INVALID_EMAIL_CYRILLIC = "Тест_selectel@mail.ru";

    // Invalid passwords
    public static final String INVALID_PASSWORD_SHORT = "Selectel_1";              // <12
    public static final String INVALID_PASSWORD_NO_DIGIT = "Test_selectel_t";      // нет цифры
    public static final String INVALID_PASSWORD_NO_SPECIAL = "Test_selectel1";     // нет спецсимвола
    public static final String INVALID_PASSWORD_NO_UPPER = "test_selectel_1";      // нет заглавной
    public static final String INVALID_PASSWORD_CYRILLIC = "Tест_selectel_1";      // кириллица

    // Безопасность
    public static final String SQL_INJECTION_EMAIL = "test@mail.ru'; DROP TABLE--";
    public static final String XSS_INJECTION_EMAIL = "\"><img src=x onerror=1>";
    public static final String COMMAND_INJECTION_EMAIL = "user@example.com; whoami";
}