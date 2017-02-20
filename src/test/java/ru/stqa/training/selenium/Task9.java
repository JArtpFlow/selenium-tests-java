/*Задание 9. Проверить сортировку стран и геозон в админке

Сделайте сценарии, которые проверяют сортировку стран и геозон (штатов) в учебном приложении litecart.

Test1) на странице http://localhost/litecart/admin/?app=countries&doc=countries
а) проверить, что страны расположены в алфавитном порядке
б) для тех стран, у которых количество зон отлично от нуля -- открыть страницу этой страны и там проверить, что зоны расположены в алфавитном порядке

 Test2) на странице http://localhost/litecart/admin/?app=geo_zones&doc=geo_zones
 зайти в каждую из стран и проверить, что зоны расположены в алфавитном порядке */

package ru.stqa.training.selenium;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;
import java.util.*;
import static org.junit.Assert.*;


public class Task9 {

    private WebDriver driver;

    private Boolean isListOrdered (List<String> stringList) {
        String previous = ""; // empty string: guaranteed to be less than or equal to any other
        for (final String cur: stringList) {
            String current = cur.toLowerCase();
            if (current.compareTo(previous) < 0)
                return false;
            previous = current;
        }
        return true;
    }

    @Before
    public void start() {
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    @Test
    public void Test1() {
        driver.get("http://localhost/litecart/admin/?app=countries&doc=countries");
        driver.findElement(By.name("username")).sendKeys("admin");
        driver.findElement(By.name("password")).sendKeys("admin");
        driver.findElement(By.name("login")).click();
        driver.findElement(By.id("platform"));

        List<WebElement> countries = driver.findElements(By.cssSelector("[name=countries_form] .row"));
        List<String> countriesNames = new ArrayList<>();
        List<Integer> zonesForCheck = new ArrayList<>(); // позиции стран в списке, у которых несколько зон

        Integer i = 0;
        for (WebElement row : countries) {
            WebElement country = row.findElement(By.cssSelector("td:nth-child(5) a"));
            countriesNames.add(country.getText());
            Integer zones = Integer.parseInt(row.findElement(By.cssSelector("td:nth-child(6)")).getText());
            if (zones > 0) {
                zonesForCheck.add(i);
            }
            i++;
        }

        assertTrue("Countries are ordered", isListOrdered(countriesNames));

        // Для каждой страны с несколькими зонами переходим на страницу страны и проверяем упорядоченность зон
        for (Integer zoneNum: zonesForCheck) {
            WebElement country = driver.findElements(By.cssSelector("[name=countries_form] .row")).get(zoneNum);
            country.findElement(By.cssSelector("a")).click();

            List<WebElement> zones = driver.findElements(By.cssSelector("#table-zones tr:not(.header) td:nth-child(3)"));
            List<String> zonesNames = new ArrayList<>();

            zones.forEach(zn -> zonesNames.add(zn.getText()));

            zonesNames.remove(zonesNames.size() - 1); // удаляем последний элемент из списка, т.к. на странице это не зона
            assertTrue("Zones are ordered", isListOrdered(zonesNames));
            driver.navigate().back();
        }
    }

    @Test
    public void Test2() {
        driver.get("http://localhost/litecart/admin/?app=geo_zones&doc=geo_zones");
        driver.findElement(By.name("username")).sendKeys("admin");
        driver.findElement(By.name("password")).sendKeys("admin");
        driver.findElement(By.name("login")).click();
        driver.findElement(By.id("platform"));

        Integer countriesToCheck = driver.findElements(By.cssSelector(".dataTable tr:not(.header):not(.footer)")).size();

        for (int i  = 0; i < countriesToCheck; i++) {
            WebElement country = driver.findElements(By.cssSelector(".dataTable tr:not(.header):not(.footer)")).get(i);
            country.findElement(By.cssSelector("td:nth-child(3) a")).click();

            List<WebElement> zones = driver.findElements(By.cssSelector(".dataTable tr:not(.header) td:nth-child(3) [selected=selected"));
            List<String> zonesNames = new ArrayList<>();
            zones.forEach(zn -> zonesNames.add(zn.getText()));

            assertTrue("Zones are ordered", isListOrdered(zonesNames));

            driver.navigate().back();
        }
    }

    @After
    public void stop() {
        driver.quit();
        driver = null;
    }
}