package org.praktikum.utils;
import com.github.javafaker.Faker;
import io.qameta.allure.Step;
import org.praktikum.models.Credentials;
import org.praktikum.models.User;

public class TestDataGenerator {
    private static final Faker faker = new Faker();

    @Step("Генерация случайных учетных данных")
    public static Credentials getRandomCredentials() {
        String login = faker.bothify("??????####@ya.ru");
        String pwd = faker.regexify("[a-z1-9]{10}");
        return new Credentials(login, pwd);
    }

    @Step("Генерирование случайного пользователя")
    public static User getRandomUser() {
        Credentials credentials = getRandomCredentials();
        String firstName = faker.name().firstName();
        return new User(credentials.getEmail(), credentials.getPassword(), firstName);
    }
}
