package base;

import com.microsoft.playwright.*;
import utils.ConfigReader;

public class BaseTest {

    protected static Playwright playwright;
    protected static Browser browser;
    protected static BrowserContext context;
    protected static Page page;

    public static Page getPage() {
        playwright = Playwright.create();

        var config = ConfigReader.get();
        Browser browser;

        switch (config.browser.toLowerCase()) {

            case "firefox":
                browser = playwright.firefox().launch(
                        new BrowserType.LaunchOptions().setHeadless(config.headless)
                );
                break;

            default:
                browser = playwright.chromium().launch(
                        new BrowserType.LaunchOptions().setHeadless(config.headless)
                );
        }
        context = browser.newContext();
        return browser.newPage();

    }

    public void setUp() {
        page.navigate(ConfigReader.get().baseUrl);
        System.out.println("Browser launched successfully");

    }

    public void tearDown() {
        playwright.close();
        System.exit(0);
    }
}