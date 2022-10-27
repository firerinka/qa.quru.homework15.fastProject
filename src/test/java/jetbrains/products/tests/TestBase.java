package jetbrains.products.tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.junit5.BrowserPerTestStrategyExtension;
import com.codeborne.selenide.logevents.SelenideLogger;
import jetbrains.products.config.Browser;
import jetbrains.products.config.ConfigReader;
import jetbrains.products.config.ProjectConfiguration;
import jetbrains.products.config.WebConfig;
import jetbrains.products.helpers.Attach;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({BrowserPerTestStrategyExtension.class})
public class TestBase {

    /*@BeforeAll
    static void configure() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());

        DesiredCapabilities capabilities = new DesiredCapabilities();
        if (System.getProperty("selenide.remote") != null) {
            Configuration.remote = System.getProperty("selenide.remote");
            capabilities.setCapability("enableVNC", true);
            capabilities.setCapability("enableVideo", true);
        }

        Configuration.browserCapabilities = capabilities;
        Configuration.baseUrl = "https://www.jetbrains.com/ru-ru/";
        Configuration.timeout = 10_000;

        Configuration.browser = System.getProperty("browser_name", "chrome");
        Configuration.browserVersion = System.getProperty("browser_version", "105.0");
        Configuration.browserSize = System.getProperty("browser_size", "1920x1080");
    }*/

    private static final WebConfig webConfig = ConfigReader.Instance.read();
    private static ProjectConfiguration projectConfiguration = new ProjectConfiguration(webConfig);

    @BeforeAll
    public static void setUp() {
        SelenideLogger.addListener("allure", new AllureSelenide());
        projectConfiguration.webConfig();
        projectConfiguration.apiConfig();
    }

    //TODO переписать, сейчас это не будет работать
    @AfterEach
    void addAttachments() {
        Attach.screenshotAs("Last screenshot");
        Attach.pageSource();
        if (Configuration.browser.equals(Browser.CHROME.name())) {
            Attach.browserConsoleLogs();
        }
        if (projectConfiguration.isRemote()) {
            Attach.addVideo(projectConfiguration.getVideoStorageUrl());
        }
    }

}
