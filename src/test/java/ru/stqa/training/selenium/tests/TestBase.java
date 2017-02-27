package ru.stqa.training.selenium.tests;

import org.junit.Before;
import ru.stqa.training.selenium.app.Application;
import ru.stqa.training.selenium.pages.MainPage;

public class TestBase {

    public static ThreadLocal<Application> tlApp = new ThreadLocal<>();
    public Application app;
    public MainPage mainPage;



    @Before
    public void start() {
        if (tlApp.get() != null) {
            app = tlApp.get();
            return;
        }

        app = new Application();
        tlApp.set(app);



        Runtime.getRuntime().addShutdownHook(
                new Thread(() -> { app.quit(); app = null; }));
    }

}