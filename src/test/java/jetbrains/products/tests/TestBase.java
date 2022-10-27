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
    private static final WebConfig webConfig = ConfigReader.Instance.read();
    private static ProjectConfiguration projectConfiguration = new ProjectConfiguration(webConfig);

    @BeforeAll
    public static void setUp() {
        SelenideLogger.addListener("allure", new AllureSelenide());
        projectConfiguration.webConfig();
        projectConfiguration.apiConfig();
    }

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
