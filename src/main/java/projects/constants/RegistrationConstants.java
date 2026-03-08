package projects.constants;

public class RegistrationConstants extends BaseConstants {

    // URLs
    public static final String REGISTRATION_URL = "https://my.selectel.ru/registration";
    public static final String PROFILE_URL = "https://my.selectel.ru/profile/profile";

    // Valid test data
    public static final String VALID_EMAIL = "test_selectel" + System.currentTimeMillis() + "@mail.ru";
    public static final String VALID_PASSWORD = "TestPass123!@#";
    public static final String VALID_EMAIL_UNIQUE = "test_selectel." + System.currentTimeMillis() + "@mail.ru";

    // Invalid passwords
    public static final String INVALID_PASSWORD_SHORT = "Selectel_1";

    public static final int MAX_EMAIL_LENGTH = 100;
}