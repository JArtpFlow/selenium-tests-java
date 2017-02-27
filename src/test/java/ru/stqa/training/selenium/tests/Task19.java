package ru.stqa.training.selenium.tests;

import org.junit.Test;

public class Task19 extends TestBase {

    @Test
    public void canRegisterCustomer() {
        app.addToCart();
        app.addToCart();
        app.addToCart();

        app.removeEachProductFromCart();
        app.checkCartIsEmpty();
    }
    
}