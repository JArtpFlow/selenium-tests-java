/*
Задание 14. Проверьте, что ссылки открываются в новом окне
Сделайте сценарий, который проверяет, что ссылки на странице редактирования страны открываются в новом окне.
Сценарий должен состоять из следующих частей:
1) зайти в админку
2) открыть пункт меню Countries (или страницу http://localhost/litecart/admin/?app=countries&doc=countries)
3) открыть на редактирование какую-нибудь страну или начать создание новой
4) возле некоторых полей есть ссылки с иконкой в виде квадратика со стрелкой -- они ведут на внешние страницы и
открываются в новом окне, именно это и нужно проверить.
Конечно, можно просто убедиться в том, что у ссылки есть атрибут target="_blank". Но в этом упражнении требуется
именно кликнуть по ссылке, чтобы она открылась в новом окне, потом переключиться в новое окно, закрыть его, вернуться
обратно, и повторить эти действия для всех таких ссылок.
Не забудьте, что новое окно открывается не мгновенно, поэтому требуется ожидание открытия окна.
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
