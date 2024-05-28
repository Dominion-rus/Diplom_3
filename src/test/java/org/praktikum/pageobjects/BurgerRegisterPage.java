package org.praktikum.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class BurgerRegisterPage extends Page{
    private WebDriver driver;
    private By firstNameField = By.xpath("//fieldset[1]/div/div/input");
    private By emailField = By.xpath("//fieldset[2]/div/div/input");
    private By passwordField = By.xpath("//fieldset[3]/div/div/input");
    private By registerButton = By.xpath("//button[text()='Зарегистрироваться']");

    public By getLoginButton() {
        return loginButton;
    }

    private By loginButton = By.xpath("//a[text()='Войти']");
    public String waitForResultVisibility(String result) {
        return wait(By.xpath(String.format("//*[text()='%s']", result))).getText();
    }
    public void login(By button,String email, String pwd){
        driver.findElement(button).click();
        wait(By.xpath("//*[text()='Войти']"));
        driver.findElement(By.xpath("//fieldset[1]/div/div/input")).sendKeys(email);
        driver.findElement(By.xpath("//fieldset[2]/div/div/input")).sendKeys(pwd);
        driver.findElement(By.xpath("//button[text()='Войти']")).click();
    }
    public boolean isLoggedIn(){
        return wait(By.xpath("//*[text()='Соберите бургер']")).getText().equals("Соберите бургер");
    }
    public void register(String firstName, String email, String password){
        typeFirstName(firstName);
        typeEmail(email);
        typePassword(password);
        pressRegister();
    }
    public BurgerRegisterPage(WebDriver driver){
        super(driver);
        this.driver = driver;
    }
    public void typeFirstName(String firstName) {
        driver.findElement(firstNameField).sendKeys(firstName);
    }
    public void typeEmail(String email) {
        driver.findElement(emailField).sendKeys(email);
    }
    public void typePassword(String password) {
        driver.findElement(passwordField).sendKeys(password);
    }
    public void pressRegister() {
        driver.findElement(registerButton).click();
    }
}