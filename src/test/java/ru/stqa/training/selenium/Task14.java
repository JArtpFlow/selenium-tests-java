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
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.SystemClock;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;


public class Task14 {
    private WebDriver driver;
    private WebDriverWait wait;

    @Before
    public void start () {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 5);
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
    }

    private String thereIsWindowOtherThan(Set<String> oldWindows) {
        Set<String> oldWindows1 = driver.getWindowHandles();
        for (String handle: oldWindows1) {
            if (!oldWindows.contains(handle)) {
                return handle;
            }
        }
        return null;
    }

    @Test
    public void Test() {
        driver.get("http://localhost/litecart/admin");
        driver.findElement(By.name("username")).sendKeys("admin");
        driver.findElement(By.name("password")).sendKeys("admin");
        driver.findElement(By.name("login")).click();
        driver.findElement(By.id("platform"));
        driver.findElement(By.xpath(("//ul[@id='box-apps-menu']/li[contains(.,'Countries')]"))).click();

        driver.findElement(By.xpath("//td[@id='content']//a[@class='button' and contains(.,' Add New Country')]")).click();

        String mainWindow = driver.getWindowHandle();
        Set<String> oldWindows = driver.getWindowHandles();

        List<WebElement> externalLinks = driver.findElements(By.cssSelector(".fa.fa-external-link"));

        for (WebElement externalLink: externalLinks) {
            externalLink.click();
            String newWindow = wait.until((WebDriver d) -> thereIsWindowOtherThan(oldWindows));
            driver.switchTo().window(newWindow);
            driver.close();
            driver.switchTo().window(mainWindow);
        }
    }

    @After
    public void stop() {
        driver.quit();
        driver = null;
    }
}
