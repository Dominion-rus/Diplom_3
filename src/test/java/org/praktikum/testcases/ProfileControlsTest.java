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
import org.praktikum.pageobjects.BurgerMainPage;
import org.praktikum.providers.UserProvider;

import static org.apache.http.HttpStatus.SC_OK;
import static org.praktikum.config.EnvConfig.*;

public class ProfileControlsTest {
    @Rule
    public DriverRule driverRule = new DriverRule();
    private WebDriver driver;
    private BurgerMainPage objBurgerMainPage;
    private UserClient userClient;
    private User user;
    private String accessToken;
    private int statusCode;
    private ValidatableResponse responseLogin;
    private ValidatableResponse responseCreate;
    @Before
    public void setUp() throws InterruptedException {
        driver = driverRule.getDriver();
        driver.get(BASE_URL);
        objBurgerMainPage = new BurgerMainPage(driver);
        userClient = new UserClient();
        user = UserProvider.getRandom();
        responseCreate = userClient.create(user);
        Thread.sleep(300);
        responseCreate.assertThat().statusCode(SC_OK);
        Thread.sleep(300);
        responseLogin = userClient.login(Credentials.from(user));
        Thread.sleep(300);
        statusCode = responseLogin.extract().statusCode();
        Thread.sleep(300);
        Assert.assertEquals(SC_OK, statusCode);
        accessToken = responseLogin.extract().path("accessToken").toString().substring(6).trim();
        driverRule.addAfterDriverClosedAction(() -> {
            userClient.delete(accessToken);
            Thread.sleep(DEFAULT_SLEEP);
        });
    }

    @Test
    @DisplayName("Секция конструктора доступна")
    @Description("Проверка доступности секции конструктора")
    public void constructorCanBeOpened(){
        objBurgerMainPage.login(objBurgerMainPage.getLoginButton(),user.getEmail(),user.getPassword());
        Assert.assertTrue(objBurgerMainPage.isLoggedIn());
        objBurgerMainPage.openProfile();
        Assert.assertEquals("Профиль",objBurgerMainPage.getProfileOpenedHeader());
        objBurgerMainPage.openConstructor();
        Assert.assertEquals("Соберите бургер",objBurgerMainPage.getConstructorOpenedHeader());
    }
    @Test
    @DisplayName("Логотип кликабелен и ведет на главную страницу")
    @Description("Проверка того,что Логотип кликабелен и ведет на главную страницу")
    public void mainPageCanBeOpened(){
        objBurgerMainPage.login(objBurgerMainPage.getLoginButton(),user.getEmail(),user.getPassword());
        Assert.assertTrue(objBurgerMainPage.isLoggedIn());
        objBurgerMainPage.openProfile();
        Assert.assertEquals("Профиль",objBurgerMainPage.getProfileOpenedHeader());
        objBurgerMainPage.openMainPage();
        Assert.assertEquals("Соберите бургер",objBurgerMainPage.getConstructorOpenedHeader());
    }
    @Test
    @DisplayName("Выход из системы возможен на странице профиля")
    @Description("Проверка того,что Выход из системы возможен на странице профиля")
    public void logoutPossible(){
        objBurgerMainPage.login(objBurgerMainPage.getLoginButton(),user.getEmail(),user.getPassword());
        Assert.assertTrue(objBurgerMainPage.isLoggedIn());
        objBurgerMainPage.openProfile();
        Assert.assertEquals("Профиль",objBurgerMainPage.getProfileOpenedHeader());
        objBurgerMainPage.logoutMainPage();
        Assert.assertEquals("Вход",objBurgerMainPage.logoutHeader());
    }

}
