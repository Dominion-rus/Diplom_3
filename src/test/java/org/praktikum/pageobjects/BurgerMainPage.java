package org.praktikum.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import java.util.List;

public class BurgerMainPage extends Page{
    private WebDriver driver;
    private By loginButton = By.xpath("//button[text()='Войти в аккаунт']");
    private By profile = By.xpath("//div/header/nav/a");
    private List<String> sectionNames = List.of("Булки","Соусы","Начинки");
    private List <By> sectionCaptions = List.of(By.xpath("//*[text()='Булки']"),
            By.xpath("//*[text()='Соусы']"),
            By.xpath("//*[text()='Начинки']"));
    private List <By> sections = List.of(By.xpath("//*/div/main/section[1]/div[2]/h2[1]"),
            By.xpath("//*/div/main/section[1]/div[2]/h2[2]"),
            By.xpath("//*/div/main/section[1]/div[2]/h2[3]"));
    private final int sectionsQuantity = sections.size();
    private By constructor = By.xpath("//*/div/header/nav/ul/li[1]/a");
    private By logout = By.xpath("//*[text()='Выход']");

    private By logo = By.xpath("//div/a[@href='/']");

    public By getLoginButton() {
        return loginButton;
    }
    public By getProfile() {
        return profile;
    }
    public int getSectionsQuantity() {
        return sectionsQuantity;
    }
    public void openConstructor() {
        driver.findElement(constructor).click();
        wait(By.xpath("//*[text()='Соберите бургер']"));
    }
    public String getConstructorOpenedHeader(){
       return wait(By.xpath("//*[text()='Соберите бургер']")).getText();
    }
    public void openProfile() {
        driver.findElement(profile).click();
        wait(By.xpath("//*[text()='Профиль']"));
    }
    public String getProfileOpenedHeader(){
        return wait(By.xpath("//*[text()='Профиль']")).getText();
    }
    public void openMainPage(){
        driver.findElement(logo).click();
        wait(By.xpath("//*[text()='Соберите бургер']"));
    }
    public void logoutMainPage(){
        driver.findElement(logout).click();
        wait(By.xpath("//*[text()='Вход']"));
    }
    public String logoutHeader(){
        return wait(By.xpath("//*[text()='Вход']")).getText();
    }

    public BurgerMainPage(WebDriver driver){
        super(driver);
        this.driver = driver;
    }
    public void clickLoginButton(By button){
        driver.findElement(button).click();
    }
    public void login(By button, String email, String pwd){
        clickLoginButton(button);
        wait(By.xpath("//*[text()='Вход']"));
        driver.findElement(By.xpath("//fieldset[1]/div/div/input")).sendKeys(email);
        driver.findElement(By.xpath("//fieldset[2]/div/div/input")).sendKeys(pwd);
        driver.findElement(By.xpath("//button[text()='Войти']")).click();
    }
    public boolean isLoggedIn(){
        return wait(By.xpath("//*[text()='Соберите бургер']")).getText().equals("Соберите бургер");
    }

    public int identifySection(){
        String current = driver.findElement(By.className("tab_tab_type_current__2BEPc")).getText();
        return sectionNames.indexOf(current);
    }
    public void waitForNSectionVisibility(int n){
        driver.findElement(sectionCaptions.get(n)).click();
        wait(returnNSection(n));
    }
    public By returnNSection(int n){
        return sections.get(n);
    }
}
