package org.praktikum.testcases;

import io.qameta.allure.junit4.DisplayName;
import org.praktikum.clients.UserClient;
import io.restassured.response.ValidatableResponse;
import jdk.jfr.Description;
import org.praktikum.models.Credentials;
import org.praktikum.models.User;
import org.junit.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.praktikum.utils.DriverRule;
import org.praktikum.pageobjects.BurgerMainPage;
import org.praktikum.providers.UserProvider;
import static org.praktikum.config.EnvConfig.*;

import static org.apache.http.HttpStatus.SC_OK;

public class ProfilePageTest {
    @Rule
    public DriverRule driverRule = new DriverRule();
    private WebDriver driver;
    private BurgerMainPage objBurgerMainPage;
    private UserClient userClient;
    private User user;
    private String accessToken;
    @Before
    public void setUp() {
        driver = driverRule.getDriver();
        driver.get(BASE_URL);
        objBurgerMainPage = new BurgerMainPage(driver);
        driverRule.addAfterDriverClosedAction(() -> {
            userClient.delete(accessToken);
            Thread.sleep(DEFAULT_SLEEP);
        });

    }
    @Test
    @DisplayName("Открывается страница профиля")
    @Description("Проверка того,что Открывается страница профиля ")
    public void profileCanBeOpened(){
        objBurgerMainPage = new BurgerMainPage(driver);
        userClient = new UserClient();
        user = UserProvider.getRandom();
        ValidatableResponse responseCreate = userClient.create(user);
        responseCreate.assertThat().statusCode(SC_OK);
        ValidatableResponse responseLogin = userClient.login(Credentials.from(user));
        int statusCode = responseLogin.extract().statusCode();
        accessToken = responseLogin.extract().path("accessToken").toString().substring(6).trim();
        Assert.assertEquals(SC_OK, statusCode);
        objBurgerMainPage.login(objBurgerMainPage.getLoginButton(),user.getEmail(),user.getPassword());
        Assert.assertTrue(objBurgerMainPage.isLoggedIn());
        objBurgerMainPage.openProfile();
        Assert.assertEquals("Профиль",objBurgerMainPage.getProfileOpenedHeader());
    }

}
