/*
Задание 11. Сделайте сценарий регистрации пользователя

Сделайте сценарий для регистрации нового пользователя в учебном приложении litecart (не в админке, а в клиентской части магазина).

Сценарий должен состоять из следующих частей:

1) регистрация новой учётной записи с достаточно уникальным адресом электронной почты (чтобы не конфликтовало с ранее созданными пользователями, в том числе при предыдущих запусках того же самого сценария),
2) выход (logout), потому что после успешной регистрации автоматически происходит вход,
3) повторный вход в только что созданную учётную запись,
4) и ещё раз выход.

В качестве страны выбирайте United States, штат произвольный. При этом формат индекса -- пять цифр.

Можно оформить сценарий либо как тест, либо как отдельный исполняемый файл.

Проверки можно никакие не делать, только действия -- заполнение полей, нажатия на кнопки и ссылки. Если сценарий дошёл до конца, то есть созданный пользователь смог выполнить вход и выход -- значит создание прошло успешно.

В форме регистрации есть капча, её нужно отключить в админке учебного приложения на вкладке Settings -> Security.
 */

package ru.stqa.training.selenium;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.Keys;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;


public class Task11 {

    private WebDriver driver;

    private void Registration(String email, String password) {
        driver.get("http://localhost/litecart/");
        driver.findElement(By.xpath(("//form[@name='login_form']//tbody/tr[contains(.,'New customers click here')]")))
                .click(); // New customers click here

        WebElement customerForm = driver.findElement(By.cssSelector("[name=customer_form]"));
        customerForm.findElement(By.cssSelector("[name=firstname]")).sendKeys("Andrey"); // First Name
        customerForm.findElement(By.cssSelector("[name=lastname]")).sendKeys("Andrey"); // Last Name
        customerForm.findElement(By.cssSelector("[name=address1]")).sendKeys("Andrey"); // Adress 1
        customerForm.findElement(By.cssSelector("[name=postcode]")).sendKeys("56578"); // Postcode
        customerForm.findElement(By.cssSelector("[name=city]")).sendKeys("LA"); // City

        customerForm.findElement(By.cssSelector(".select2-selection.select2-selection--single")).click();  // Country dropdown
        driver.findElement(By.cssSelector(".select2-search--dropdown .select2-search__field"))
                .sendKeys("United States" + Keys.ENTER);  // Country

        customerForm.findElement(By.cssSelector("[name=email]")).sendKeys(email); // Email
        customerForm.findElement(By.cssSelector("[name=phone]")).sendKeys("912353245"); // Phone
        customerForm.findElement(By.cssSelector("[name=password]")).sendKeys(password); // Desired Password
        customerForm.findElement(By.cssSelector("[name=confirmed_password]")).sendKeys(password); // Desired Password
        customerForm.findElement(By.cssSelector("[name=create_account]")).click(); // Create account
        driver.findElement(By.cssSelector("div.notice.success")).isDisplayed(); // success registration
    }

    private void Login (String email, String password) {
        WebElement loginForm = driver.findElement(By.cssSelector("[name=login_form]"));
        loginForm.findElement(By.cssSelector("[name=email]")).sendKeys(email); // Email
        loginForm.findElement(By.cssSelector("[name=password]")).sendKeys(password); // Desired Password
        loginForm.findElement(By.cssSelector("[name=login]")).click(); // Login
    }

    private void Logout() {
        driver.findElement(By.cssSelector("#box-account li:nth-child(4) a")).click(); // Logout
    }

    @Before
    public void start() {
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    }

    @Test
    public void Test1() {
        String email = Integer.toString(ThreadLocalRandom.current().nextInt(99999999)) + "testjavaselenium@testwe.ru";
        String password = "pass123";

        Registration(email, password);
        Logout();

        Login(email, password);
        Logout();
    }

    @After
    public void stop() {
        driver.quit();
        driver = null;
    }
}
