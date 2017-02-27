package ru.stqa.training.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;


public class Page {

    protected WebDriver driver;
    protected WebDriverWait wait;

    public Page(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, 10);
    }

    public WebElement isElementPresent(By locator) {
        List<WebElement> elements = driver.findElements(locator);
        if (elements.size() > 0) {
            return elements.get(0);
        } else {
            return null;
        }
    }
}