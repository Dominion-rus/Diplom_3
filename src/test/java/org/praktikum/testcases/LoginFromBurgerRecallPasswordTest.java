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
import org.praktikum.pageobjects.BurgerRecallPasswordPage;
import org.praktikum.providers.UserProvider;
import static org.praktikum.config.EnvConfig.*;

import static org.apache.http.HttpStatus.SC_OK;

public class LoginFromBurgerRecallPasswordTest {
    @Rule
    public DriverRule driverRule = new DriverRule();
    private WebDriver driver;
    private BurgerRecallPasswordPage objBurgerRecallPasswordPage;
    private UserClient userClient;
    private User user;
    private String accessToken;
    @Before
    public void setUp() {
        driver = driverRule.getDriver();
        driver.get( BASE_URL + "forgot-password");
        objBurgerRecallPasswordPage = new BurgerRecallPasswordPage(driver);
        userClient = new UserClient();
        user = UserProvider.getRandom();
        driverRule.addAfterDriverClosedAction(() -> {
            userClient.delete(accessToken);
            Thread.sleep(DEFAULT_SLEEP);
        });
    }
    @Test
    @DisplayName("Вход на странице восстановления пароля")
    @Description("Проверка входа доступна на странице восстановления пароля")
    public void testLoginPossible(){
        ValidatableResponse responseCreate = userClient.create(user);
        responseCreate.assertThat().statusCode(SC_OK);
        ValidatableResponse responseLogin = userClient.login(Credentials.from(user));
        int statusCode = responseLogin.extract().statusCode();
        accessToken = responseLogin.extract().path("accessToken").toString().substring(6).trim();
        Assert.assertEquals(SC_OK, statusCode);
        objBurgerRecallPasswordPage.login(user.getEmail(),user.getPassword());
        Assert.assertEquals("Соберите бургер",  objBurgerRecallPasswordPage.getConstructorOpenedHeader());
    }

}