package projects.constants;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.IOException;
import java.io.InputStream;

public class BaseConstants {

    public static final String BASE_URL;
    public static final String EMAIL;
    public static final String PASSWORD;

    protected BaseConstants() {
        throw new IllegalStateException("Utility class");
    }

    // Статический блок инициализации - выполняется один раз при загрузке класса
    static {
        String url = "";
        String email = "";
        String password = "";

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        try (InputStream inputStream = BaseConstants.class
                .getClassLoader()
                .getResourceAsStream("config.yml")) {

            if (inputStream == null) {
                throw new RuntimeException("Файл config.yml не найден в resources!");
            }

            // Читаем во вспомогательный объект
            ConfigData config = mapper.readValue(inputStream, ConfigData.class);

            url = config.getBase().getUrl();
            email = config.getLogin().getEmail();
            password = config.getLogin().getPassword();

        } catch (IOException e) {
            throw new RuntimeException("Ошибка чтения конфигурации", e);
        }

        // Присваиваем значения финальным статическим полям
        BASE_URL = url;
        EMAIL = email;
        PASSWORD = password;
    }

    static class LoginConfig {
        @JsonProperty("email")
        private String email;

        @JsonProperty("password")
        private String password;

        public String getEmail() { return email; }
        public String getPassword() { return password; }
    }

    static class BaseConfig {
        @JsonProperty("url")
        private String url;

        public String getUrl() { return url; }
    }

    static class ConfigData {
        @JsonProperty("base")
        private BaseConfig base;

        @JsonProperty("login")
        private LoginConfig login;

        public BaseConfig getBase() { return base; }
        public LoginConfig getLogin() { return login; }
    }
}


