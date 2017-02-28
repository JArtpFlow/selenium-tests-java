/*
Задание 17. Проверьте отсутствие сообщений в логе браузера
Сделайте сценарий, который проверяет, не появляются ли в логе браузера сообщения при открытии страниц в учебном
приложении, а именно -- страниц товаров в каталоге в административной панели.
Сценарий должен состоять из следующих частей:
1) зайти в админку
2) открыть каталог, категорию, которая содержит товары
(страница http://localhost/litecart/admin/?app=catalog&doc=catalog&category_id=1)
3) последовательно открывать страницы товаров и проверять, не появляются ли в логе браузера сообщения (любого уровня)
*/

package ru.stqa.training.selenium;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.concurrent.TimeUnit;


public class Task17 {
    private WebDriver driver;
    private WebDriverWait wait;

    @Before
    public void start () {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 5);
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
    }

    @Test
    public void Test() {
        driver.get("http://localhost/litecart/admin");
        driver.findElement(By.name("username")).sendKeys("admin");
        driver.findElement(By.name("password")).sendKeys("admin");
        driver.findElement(By.name("login")).click();
        driver.findElement(By.id("platform"));
        driver.findElement(By.xpath(("//ul[@id='box-apps-menu']/li[contains(.,'Countries')]"))).click();

        String catalogURL = "http://localhost/litecart/admin/?app=catalog&doc=catalog&category_id=1";
        driver.get(catalogURL);

        int products = driver.findElements(By.cssSelector(".dataTable tr:not(.header):not(.footer)")).size();

        for (int i=1; i <= products; i++) {
            driver.findElement(By.xpath(String.format("//table[@class='dataTable']//tr[not(@class='header') and not(@class='footer')][%d]//a", i))).click();
            driver.manage().logs().get("browser").forEach(l -> System.out.println(l));
            driver.get(catalogURL);
        }
    }

    @After
    public void stop() {
        driver.quit();
        driver = null;
    }
}
