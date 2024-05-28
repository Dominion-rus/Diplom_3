package org.praktikum.testcases;

import io.qameta.allure.junit4.DisplayName;
import org.praktikum.clients.UserClient;
import io.restassured.response.ValidatableResponse;
import jdk.jfr.Description;
import org.praktikum.models.Credentials;
import org.praktikum.models.User;
import org.junit.*;
import org.openqa.selenium.WebDriver;
import org.praktikum.utils.DriverRule;
import org.praktikum.pageobjects.BurgerRegisterPage;
import org.praktikum.providers.UserProvider;

import static org.apache.http.HttpStatus.SC_OK;
import static org.praktikum.config.EnvConfig.*;

public class LoginFromBurgerRegisterPageTest {
    @Rule
    public DriverRule driverRule = new DriverRule();
    private WebDriver driver;
    private BurgerRegisterPage objBurgerRegisterPage;
    private UserClient userClient;
    private User user;
    private String accessToken;
    private int statusCode;
    private ValidatableResponse responseLogin;
    private ValidatableResponse responseCreate;


    @Before
    public void setUp(){
        driver = driverRule.getDriver();
        driver.get(BASE_URL + "register");
        objBurgerRegisterPage = new BurgerRegisterPage(driver);
        userClient = new UserClient();
        user = UserProvider.getRandom();
        driverRule.addAfterDriverClosedAction(() -> {
            userClient.delete(accessToken);
            Thread.sleep(DEFAULT_SLEEP);
        });

    }
    @Test
    @DisplayName("Вход на странице регистрации")
    @Description("Проверка входа доступна на странице регистрации")
    public void testLoginPossible(){
        responseCreate = userClient.create(user);
        responseCreate.assertThat().statusCode(SC_OK);
        responseLogin = userClient.login(Credentials.from(user));
        statusCode = responseLogin.extract().statusCode();
        accessToken = responseLogin.extract().path("accessToken").toString().substring(6).trim();
        Assert.assertEquals(SC_OK, statusCode);
        objBurgerRegisterPage.login(objBurgerRegisterPage.getLoginButton(),user.getEmail(),user.getPassword());
        Assert.assertTrue(objBurgerRegisterPage.isLoggedIn());
    }

}