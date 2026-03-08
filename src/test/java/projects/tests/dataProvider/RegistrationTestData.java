package projects.tests.dataProvider;


import org.testng.annotations.DataProvider;

public class RegistrationTestData {

    @DataProvider(name = "invalidEmails")
    public static Object[][] invalidEmails() {
        return new Object[][]{
                {"test_selectelmail.ru", "Email без @"},
                {"test_selectel@mail", "Email без домена"},
                {"тест_selectel@mail.ru", "Email с кириллицей"},
        };
    }

    @DataProvider(name = "invalidPasswords")
    public static Object[][] invalidPasswords() {
        return new Object[][]{
                {"Short_1!", "Короткий пароль"},
                {"NoDigitsHere!", "Без цифр"},
                {"NoSpecialChar1", "Пароль без спецсимвола"},
                {"alllowercase1!", "Без заглавных"},
                {"Pa_роль_1_!", "Кириллица в пароле"},
        };
    }




    @DataProvider(name = "securityPayloads")
    public static Object[][] securityPayloads() {
        return new Object[][]{
                {"test@mail.ru'; DROP TABLE users--", "SQL Injection"},
                {"<script>alert('xss')</script>@mail.ru", "XSS"},
                {"user@example.com; cat /etc/passwd", "Command Injection"},
        };
    }
}
