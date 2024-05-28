package org.praktikum.testcases;

import io.qameta.allure.junit4.DisplayName;
import org.praktikum.clients.UserClient;
import io.restassured.response.ValidatableResponse;
import jdk.jfr.Description;
import org.praktikum.models.Credentials;
import org.praktikum.models.User;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.praktikum.utils.DriverRule;
import org.praktikum.pageobjects.BurgerRegisterPage;
import org.praktikum.providers.UserProvider;
import java.util.List;

import static org.apache.http.HttpStatus.SC_OK;
import static org.praktikum.config.EnvConfig.*;

@RunWith(Parameterized.class)
public class BurgerRegisterPageTest {
    @Rule
    public DriverRule driverRule = new DriverRule();
    private final String name;
    private final String email;
    private final String pwd;
    private final String expected;
    private static final List<String> EXPECTEDS = List.of("Некорректный пароль", "Вход");
    private static final String NAME = "name";
    private static final String EMAIL = "email";
    private static final String PASS = "12345";
    private  WebDriver driver;
    private BurgerRegisterPage objBurgerRegisterPage;
    private static UserClient userClient;
    private static User user;
    private static String accessToken;

    @Before
    public void setUp() {
        driver = driverRule.getDriver();
        driver.get(BASE_URL + "register");
        objBurgerRegisterPage = new BurgerRegisterPage(driver);
        driverRule.addAfterDriverClosedAction(() -> {
            userClient.delete(accessToken);
            Thread.sleep(DEFAULT_SLEEP);
        });
    }

    public BurgerRegisterPageTest(String name, String email, String pwd, String expected) {
        driver = new ChromeDriver();
        this.name = name;
        this.email = email;
        this.pwd = pwd;
        this.expected = expected;
    }

    @Parameterized.Parameters
    public static Object[][] getTestData() {
        userClient = new UserClient();
        user = UserProvider.getRandom();
        return new Object[][]{
                {NAME, EMAIL, PASS, EXPECTEDS.get(0)},
                {user.getName(),user.getEmail(),user.getPassword(), EXPECTEDS.get(1)},
        };
    }
    @Test
    @DisplayName("Регистрация с правильным/неправильным паролем")
    @Description("Проверка регистрации с правильным/неправильным паролем")
    public void testRegister() {
        objBurgerRegisterPage.register(name, email, pwd);
        ValidatableResponse responseLogin = userClient.login(Credentials.from(user));
        int statusCode = responseLogin.extract().statusCode();
        if(statusCode == SC_OK){
            accessToken = responseLogin.extract().path("accessToken").toString().substring(6).trim();
        }
        Assert.assertEquals(objBurgerRegisterPage.waitForResultVisibility(expected), expected);
    }

}
