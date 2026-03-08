package projects.tests.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.IOException;
import java.io.InputStream;

public class TestConfig {

    public static final Boolean IS_CI;

    private TestConfig() {
        throw new IllegalStateException("Utility class");
    }

    static {
        Boolean ci = null;
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        try (InputStream inputStream = TestConfig.class
                .getClassLoader()
                .getResourceAsStream("config_test.yml")) {

            if (inputStream == null) {
                throw new RuntimeException("Файл config_test.yml не найден в resources!");
            }

            ConfigData config = mapper.readValue(inputStream, ConfigData.class);
            ci = config.getTests() != null ? config.getTests().getCi() : null;

        } catch (IOException e) {
            throw new RuntimeException("Ошибка чтения конфигурации тестов", e);
        }

        IS_CI = ci;
    }

    public static boolean getIsCi() {
        return IS_CI != null && IS_CI;
    }


    static class TestsConstants {
        @JsonProperty("ci")
        private Boolean ci;
        public Boolean getCi() { return ci; }
    }

    static class ConfigData {
        @JsonProperty("tests")
        private TestsConstants tests;
        public TestsConstants getTests() { return tests; }
    }
}