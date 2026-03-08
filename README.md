📖 QA Selectel — Автотесты

Проект содержит автотесты для проверки функциональности сайта Selectel.

Тесты можно запускать:

локально через IntelliJ IDEA

через Maven в командной строке

### Установка и запуск
1. Клонирование репозитория
   git clone https://github.com/ваш-username/qa_selectel.git
   cd qa_selectel

2. Установка Java 21

#### Windows
Скачать JDK:
https://adoptium.net/

###$ macOS
```bash
brew install openjdk@21
```

#### Linux (Ubuntu/Debian)
```bash
sudo apt update
sudo apt install openjdk-21-jdk -y
```

3. Проверка установки Java
 ```bash
  java -version
```
Должно вывести примерно: openjdk version "21"

4. Скачивание зависимостей Maven
```bash
mvn dependency:resolve
```

5. Установка Google Chrome

Тесты запускаются в браузере Chrome.
Windows / macOS
https://www.google.com/chrome/

Linux
```bash
sudo apt install google-chrome-stable -y
```

### ▶️ Использование

##### При локальном запуске в config файле тестов(src/test/resources/config_test.yml) выставить параметр tests.ci: false

1. Запуск через IntelliJ IDEA (рекомендуется)

Откройте проект в IntelliJ IDEA
Перейдите в  src/test/java/projects/tests

Нажмите ▶️ рядом с тестом

Пример:
```angular2html
class RegistrationTest
registrationWithValidData → Run
```

2. Запуск через Maven
Запуск всех тестов
```bash
mvn test
```
Запуск конкретного тест-класса
```bash
mvn test -Dtest=RegistrationTest
```
Запуск конкретного теста
```bash
mvn test -Dtest=RegistrationTest#registrationWithValidData
```
Очистка и запуск тестов
```bash
mvn clean test
```
