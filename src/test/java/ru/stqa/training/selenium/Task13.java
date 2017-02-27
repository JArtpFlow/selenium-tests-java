/*
Задание 13. Сделайте сценарий работы с корзиной
Сделайте сценарий для добавления товаров в корзину и удаления товаров из корзины.
1) открыть главную страницу
2) открыть первый товар из списка
2) добавить его в корзину (при этом может случайно добавиться товар, который там уже есть, ничего страшного)
3) подождать, пока счётчик товаров в корзине обновится
4) вернуться на главную страницу, повторить предыдущие шаги ещё два раза, чтобы в общей сложности в корзине было 3 единицы товара
5) открыть корзину (в правом верхнем углу кликнуть по ссылке Checkout)
6) удалить все товары из корзины один за другим, после каждого удаления подождать, пока внизу обновится таблица
*/

package ru.stqa.training.selenium;


import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import static org.openqa.selenium.support.ui.ExpectedConditions.*;

import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;


public class Task13 {
    private WebDriver driver;
    private WebDriverWait wait;

    private WebElement isElementPresent(By locator) {
        List<WebElement> elements = driver.findElements(locator);
        if (elements.size() > 0) {
            return elements.get(0);
        } else {
            return null;
        }
    }

    private void addToCart() {
        driver.get("http://localhost/litecart");
        List<WebElement> popularProducts = driver.findElements(By.cssSelector("#box-most-popular ul li"));
        popularProducts.get(0).click();

        int productsInCartQuantity1 = Integer.valueOf(driver.findElement(By.cssSelector("#cart .quantity")).getText());

        // для некоторых продуктов необходимо выбирать размер, чтобы добавить их в корзину
        By byProductSize = By.cssSelector("[name='buy_now_form'] [name='options[Size]']");
        WebElement productSize = isElementPresent(byProductSize);
        if (productSize != null) {
            Select dropdown = new Select(driver.findElement(byProductSize));
            dropdown.selectByVisibleText("Small");
        }

        driver.findElement(By.name("add_cart_product")).click(); // Add To Cart

        // Число продуктов в корзине должно уведичиться на единицу
        wait.until((WebDriver d) -> Integer.valueOf(driver.findElement(By.cssSelector("#cart .quantity")).getText()) == productsInCartQuantity1+1);
    }

    private void deleteAndCheck() {
        By tableTrBy = By.cssSelector("#order_confirmation-wrapper tr");
        int tableSize = driver.findElements(tableTrBy).size();

        WebElement removeBtn = driver.findElement(By.name("remove_cart_item"));
        wait.until(visibilityOf(removeBtn));
        removeBtn.click();
        wait.until((WebDriver d) -> driver.findElements(tableTrBy).size() == 0 ||  // Когда удалили последний элемент, таблицы нет
                d.findElements(tableTrBy).size() == tableSize-1);  // Если удалили не последний элемент, строки в таблице уменьшились
    }

    @Before
    public void start () {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 5);
    }

    @Test
    public void Test() {
        addToCart();
        addToCart();
        addToCart();

        int productsInCartQuantity = Integer.parseInt(driver.findElement(By.cssSelector("#cart .quantity")).getText());
        Assert.assertEquals("Products in cart must be 3", productsInCartQuantity, 3);

        driver.findElement(By.cssSelector("#cart a.link")).click(); // Переход в корзину

        By itemsBy = By.cssSelector("#box-checkout-cart .shortcuts li a");
        int itemsSize = driver.findElements(itemsBy).size();

        if (itemsSize == 0) {  // Если сначала все три выбранных продукта одинаковые
            deleteAndCheck();
        } else {
            for (int i = 0; i < itemsSize; i++) {
                List<WebElement> items = driver.findElements(itemsBy);
                if (items.size() > 0) {
                    items.get(0).click();
                    deleteAndCheck();
                } else {
                    deleteAndCheck();
                }
            }
        }

        Assert.assertEquals("Check no items in cart", driver.findElement(By.cssSelector("#checkout-cart-wrapper p")).getText(),
                "There are no items in your cart.");
    }

    @After
    public void stop() {
        driver.quit();
        driver = null;
    }
}
