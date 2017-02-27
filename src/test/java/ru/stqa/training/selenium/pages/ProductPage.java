package ru.stqa.training.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

public class ProductPage extends Page {

    public ProductPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    public void addToCart() {
        int productsInCartQuantity1 = Integer.valueOf(driver.findElement(By.cssSelector("#cart .quantity")).getText());
        By byProductSize = By.cssSelector("[name='buy_now_form'] [name='options[Size]']");
        WebElement productSize = isElementPresent(byProductSize);
        if (productSize != null) {
            Select dropdown = new Select(driver.findElement(byProductSize));
            dropdown.selectByVisibleText("Small");
        }
        driver.findElement(By.name("add_cart_product")).click(); // Add To Cart;
        wait.until((WebDriver d) -> Integer.valueOf(driver.findElement(By.cssSelector("#cart .quantity")).getText()) == productsInCartQuantity1+1);
    }

}