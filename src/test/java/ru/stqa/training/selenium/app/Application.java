package ru.stqa.training.selenium.app;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import ru.stqa.training.selenium.pages.CartPage;
import ru.stqa.training.selenium.pages.MainPage;
import ru.stqa.training.selenium.pages.ProductPage;

public class Application {

    private WebDriver driver;
    private MainPage mainPage;
    private ProductPage productPage;
    private CartPage cartPage;

    public Application() {
        driver = new ChromeDriver();
        mainPage = new MainPage(driver);
        productPage = new ProductPage(driver);
        cartPage = new CartPage(driver);
    }

    public void quit() {
        driver.quit();
    }

    public void addToCart() {
        mainPage.open();
        mainPage.chooseProduct(0);
        productPage.addToCart();
    }

    public void removeEachProductFromCart() {
        cartPage.openFromMainPage();
        cartPage.removeEachProduct();
    }

    public void checkCartIsEmpty() {
        cartPage.checkCartIsEmpty();
    }

}