/*
Задание 12. Сделайте сценарий добавления товара
Сделайте сценарий для добавления нового товара (продукта) в учебном приложении litecart (в админке).
Для добавления товара нужно открыть меню Catalog, в правом верхнем углу нажать кнопку "Add New Product", заполнить поля с информацией о товаре и сохранить.
Достаточно заполнить только информацию на вкладках General, Information и Prices. Скидки (Campains) на вкладке Prices можно не добавлять.
Переключение между вкладками происходит не мгновенно, поэтому после переключения можно сделать небольшую паузу (о том, как делать более правильные ожидания, будет рассказано в следующих занятиях).
Картинку с изображением товара нужно уложить в репозиторий вместе с кодом. При этом указывать в коде полный абсолютный путь к файлу плохо, на другой машине работать не будет. Надо средствами языка программирования преобразовать относительный путь в абсолютный.
После сохранения товара нужно убедиться, что он появился в каталоге (в админке). Клиентскую часть магазина можно не проверять.
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
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;


public class Task12 {
    private WebDriver driver;
    private WebDriverWait wait;

    @Before
    public void start () {
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        wait = new WebDriverWait(driver, 5);
    }

    @Test
    public void Test() {
        driver.get("http://localhost/litecart/admin/");
        driver.findElement(By.name("username")).sendKeys("admin");
        driver.findElement(By.name("password")).sendKeys("admin");
        driver.findElement(By.name("login")).click();
        driver.findElement(By.id("platform"));

        driver.findElement(By.xpath(("//ul[@id='box-apps-menu']/li[contains(.,'Catalog')]"))).click();

        int rowCount1 = driver.findElements(By.cssSelector(".dataTable tr")).size();

        driver.findElement(By.xpath(("//a[@class='button' and contains(.,' Add New Product')]"))).click();

        // General tab
        driver.findElement(By.xpath(("//div[@id='tab-general']//tr[1]/td/label[contains(.,' Enabled')]"))).click();

        String productName = "Super new duck" + Integer.toString(ThreadLocalRandom.current().nextInt(10000));
        driver.findElement(By.name("name[en]")).sendKeys(productName);

        driver.findElement(By.name("code")).sendKeys("12345");

        driver.findElement(By.xpath(("//div[@id='tab-general']//td[contains(., 'Product Groups')]//td[contains(., 'Male')]//..//input"))).click();

        WebElement quantity = driver.findElement(By.name("quantity"));
        quantity.clear();
        quantity.sendKeys("2");

        Path currentPath = Paths.get(System.getProperty("user.dir"));
        Path filePath = Paths.get(currentPath.toString(), "img", "bestduck.png");
        driver.findElement(By.name("new_images[]")).sendKeys(filePath.toString());

        WebElement dataValidFrom = driver.findElement(By.xpath(("//input[@name='date_valid_from']")));
        dataValidFrom.click();
        dataValidFrom.sendKeys("10122016");

        WebElement dataValidTo = driver.findElement(By.xpath(("//input[@name='date_valid_to']")));
        dataValidTo.click();
        dataValidTo.sendKeys("10122150");

        // Information tab
        driver.findElement(By.cssSelector("a[href='#tab-information']")).click();

        Select manufacturer = new Select(driver.findElement(By.name("manufacturer_id")));
        manufacturer.selectByVisibleText("ACME Corp.");

        driver.findElement(By.name("keywords")).sendKeys("duck, waterproof");
        driver.findElement(By.name("short_description[en]")).sendKeys("Best duck, best present on birthday, get it now!");
        driver.findElement(By.cssSelector(".trumbowyg-editor")).sendKeys("Waterproof duck");
        driver.findElement(By.name("head_title[en]")).sendKeys("Waterproof duck Ducky");
        driver.findElement(By.name("meta_description[en]")).sendKeys("duck");

        //Prices tab
        driver.findElement(By.cssSelector("a[href='#tab-prices']")).click();

        WebElement purchasePrice = driver.findElement(By.name("purchase_price"));
        purchasePrice.clear();
        purchasePrice.sendKeys("27");

        WebElement pricesUSD = driver.findElement(By.name("prices[USD]"));
        pricesUSD.clear();
        pricesUSD.sendKeys("27.68");

        WebElement pricesEUR = driver.findElement(By.name("prices[EUR]"));
        pricesEUR.clear();
        pricesEUR.sendKeys("25.90");

        driver.findElement(By.name("save")).click();

        // Проверки успешности создания
        WebElement noticeSuccess = driver.findElement(By.cssSelector(".notice.success"));
        Assert.assertEquals("Changes were successfully saved.", noticeSuccess.getText());
        driver.findElement(By.xpath(String.format("//table[@class='dataTable']//td[contains(.,'%s')]", productName)));
        int rowCount2 = driver.findElements(By.cssSelector(".dataTable tr")).size();
        Assert.assertEquals("Product appear in table", rowCount2, rowCount1+1);
    }

    @After
    public void stop() {
        driver.quit();
        driver = null;
    }
}
