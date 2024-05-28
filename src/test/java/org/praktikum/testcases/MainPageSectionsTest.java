package org.praktikum.testcases;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.*;
import org.openqa.selenium.WebDriver;
import org.praktikum.utils.DriverRule;
import org.praktikum.pageobjects.BurgerMainPage;
import static org.praktikum.config.EnvConfig.*;

public class MainPageSectionsTest {
    @Rule
    public DriverRule driverRule = new DriverRule();
    private static WebDriver driver ;
    private BurgerMainPage objBurgerMainPage;

    @Before
    public void setUp(){
        driver = driverRule.getDriver();
        driver.get(BASE_URL);
        objBurgerMainPage = new BurgerMainPage(driver);
    }
    @Test
    @DisplayName("Все разделы продуктов доступны на главной странице")
    @Description("Проверка что все разделы продуктов доступны на главной странице")
    public void testMainPageSections() {
        int currentSectionIndex = objBurgerMainPage.identifySection();
        for(int i = 0; i<objBurgerMainPage.getSectionsQuantity();i++){
            if (i != currentSectionIndex){
                objBurgerMainPage.waitForNSectionVisibility(i);
                Assert.assertTrue(driver.findElement(objBurgerMainPage.returnNSection(i)).isDisplayed());
            }
        }
        objBurgerMainPage.waitForNSectionVisibility(currentSectionIndex);
        Assert.assertTrue(driver.findElement(objBurgerMainPage.returnNSection(currentSectionIndex)).isDisplayed());
    }

}
