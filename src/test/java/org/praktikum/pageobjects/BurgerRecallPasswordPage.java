package org.praktikum.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
public class BurgerRecallPasswordPage extends Page{
    private WebDriver driver;
    public BurgerRecallPasswordPage(WebDriver driver){
        super(driver);
        this.driver = driver;
    }
    private By loginButton = By.xpath("//a[text()='Войти']");
    public void login(String email, String pwd){
        driver.findElement(loginButton).click();
        wait(By.xpath("//*[text()='Войти']"));
        driver.findElement(By.xpath("//fieldset[1]/div/div/input")).sendKeys(email);
        driver.findElement(By.xpath("//fieldset[2]/div/div/input")).sendKeys(pwd);
        driver.findElement(By.xpath("//button[text()='Войти']")).click();
    }
    public String getConstructorOpenedHeader(){
        return wait(By.xpath("//*[text()='Соберите бургер']")).getText();
    }
}
