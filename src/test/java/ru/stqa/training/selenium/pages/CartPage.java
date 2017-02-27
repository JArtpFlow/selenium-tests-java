package ru.stqa.training.selenium.pages;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import java.util.List;

import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;


public class CartPage extends Page {

    public CartPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    public void openFromMainPage() {
        driver.findElement(By.cssSelector("#cart a.link")).click();
    }

    public void removeEachProduct() {
        By itemsBy = By.cssSelector("#box-checkout-cart .shortcuts li a");
        int itemsSize = driver.findElements(itemsBy).size();

        if (itemsSize == 0) {  // Если все выбранные продукты
            removeOneProduct();
        } else {
            for (int i = 0; i < itemsSize; i++) {
                List<WebElement> items = driver.findElements(itemsBy);
                if (items.size() > 0) {
                    items.get(0).click();
                    removeOneProduct();
                } else {
                    removeOneProduct();
                }
            }
        }
    }

    private void removeOneProduct() {
        By tableTrBy = By.cssSelector("#order_confirmation-wrapper tr");
        int tableSize = driver.findElements(tableTrBy).size();

        WebElement removeBtn = driver.findElement(By.name("remove_cart_item"));
        wait.until(visibilityOf(removeBtn));
        removeBtn.click();
        wait.until((WebDriver d) -> driver.findElements(tableTrBy).size() == 0 ||  // Когда удалили последний элемент, таблицы нет
                d.findElements(tableTrBy).size() == tableSize-1);  // Если удалили не последний элемент, строки в таблице уменьшились
    }

    public void checkCartIsEmpty() {
        Assert.assertEquals("Check no items in cart", driver.findElement(By.cssSelector("#checkout-cart-wrapper p")).getText(),
                "There are no items in your cart.");
    }

}