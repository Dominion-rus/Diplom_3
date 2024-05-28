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
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.praktikum.pageobjects.BurgerMainPage;
import org.praktikum.providers.UserProvider;
import static org.apache.http.HttpStatus.SC_OK;
import org.praktikum.utils.DriverRule;
import static org.praktikum.config.EnvConfig.*;

@RunWith(Parameterized.class)
public class BurgerMainPageTest {
    private static WebDriver driver ;
    @Rule
    public DriverRule driverRule = new DriverRule();
    private final By button;
    private static BurgerMainPage objBurgerMainPage;
    private final String name;
    private final String pwd;
    private static UserClient userClient;
    private static User user;
    private static String accessToken;



    @Before
    public void setUp(){
        driver = driverRule.getDriver();
        objBurgerMainPage = new BurgerMainPage(driver);
        driver.get(BASE_URL);
        driverRule.addAfterDriverClosedAction(() -> {
            userClient.delete(accessToken);
            Thread.sleep(DEFAULT_SLEEP);
        });
    }

    public BurgerMainPageTest(By button, String name, String pwd){
        this.button = button;
        this.name = name;
        this.pwd = pwd;
    }

    @Parameterized.Parameters
    public static Object[][] getTestData() {
        BurgerMainPage burgerMainPage = new BurgerMainPage(driver);
        userClient = new UserClient();
        user = UserProvider.getRandom();
        return new Object[][]{
                {burgerMainPage.getProfile(), user.getEmail(), user.getPassword()},
                {burgerMainPage.getLoginButton(), user.getEmail(), user.getPassword()}
        };
    }

    @Test
    @DisplayName("Проверка входа в систему")
    @Description("Проверка входа в систему доступна с главной страницы")
    public void testLoginPossibleFromMainPage(){
        ValidatableResponse responseCreate = userClient.create(user);
        responseCreate.assertThat().statusCode(SC_OK);
        ValidatableResponse responseLogin = userClient.login(Credentials.from(user));
        int statusCode = responseLogin.extract().statusCode();
        accessToken = responseLogin.extract().path("accessToken").toString().substring(6).trim();
        Assert.assertEquals(SC_OK, statusCode);
        objBurgerMainPage.login(button,name,pwd);
        Assert.assertTrue(objBurgerMainPage.isLoggedIn());
    }

}