package ru.stqa.training.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import java.util.List;


public class MainPage extends Page {

    public MainPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    public void open() {
        driver.get("http://localhost/litecart");
    }

    public void chooseProduct(int indexInList) {
        List<WebElement> popularProducts = driver.findElements(By.cssSelector("#box-most-popular ul li"));
        popularProducts.get(indexInList).click();
    }

}