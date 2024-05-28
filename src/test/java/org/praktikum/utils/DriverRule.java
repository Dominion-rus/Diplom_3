package org.praktikum.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.rules.ExternalResource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.util.ArrayList;
import java.util.List;

public class DriverRule extends ExternalResource {
    private WebDriver driver;

    private List<AfterDriverClosed> afterDriverClosedActions = new ArrayList<>();

    public void addAfterDriverClosedAction(AfterDriverClosed action) {
        afterDriverClosedActions.add(action);
    }
    @Override
    protected void before(){
        initDriver();
    }

    @Override
    protected void after() {
        if (driver != null) {
            driver.quit();
        }
        afterDriverClosedActions.forEach(a -> {
            try {
                a.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void initDriver(){
        if ("firefox".equals(System.getProperty("browser"))) {
            initFirefox();
        }
        else {
            initChrome();
        }
    }

    private void initFirefox() {
        WebDriverManager.firefoxdriver().setup();
        var opts = new FirefoxOptions()
                .configureFromEnv();
        driver = new FirefoxDriver(opts);
    }

    private void initChrome() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
    }

    public WebDriver getDriver(){
        return driver;
    }
}
