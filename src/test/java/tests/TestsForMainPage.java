package tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import config.Credentials;
import helpers.Attach;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.remote.DesiredCapabilities;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static helpers.Attach.getSessionId;
import static helpers.GetProperty.readProperty;
import static io.qameta.allure.Allure.step;

public class TestsForMainPage {
    static final String MAINPAGE = "https://www.ozon.ru/";

    @BeforeAll
    static void setUpConfig() {
        String login = Credentials.credentials.login();
        String password = Credentials.credentials.password();
        String server = readProperty();
        Configuration.remote = String.format("https://%s:%s@%s/wd/hub", login, password, server);
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());
        Configuration.browser = "chrome";
        Configuration.startMaximized = true;

        DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability("enableVNC", true);
        capabilities.setCapability("enableVideo", true);

        Configuration.browserCapabilities = capabilities;
    }

    @Test
    @DisplayName("Проверка страницы 'Избранное'")
    void checkOrderList() {
        step("Открытие сайта", () ->
                open(MAINPAGE));
        step("Открытие страницы 'Заказы'", () ->
                $("[data-widget='favoriteCounter']").click());
        step("Проверка открытия страницы", () ->
                $(".a4b8").shouldHave(text("В Избранном пока ничего нет")));
    }

    @Test
    @DisplayName("Проверка окошка авторизации")
    void checkAuthPage() {
        step("Открытие сайта", () ->
                open(MAINPAGE));
        step("Нажатие на кнопку авторизации", () ->
                $("svg.b2x8").click());
        step("Проверка надписи", () ->
                $(".b9c1 .b9c7").shouldHave(text("Войдите или зарегистрируйтесь, чтобы продолжить")));
    }

    @Test
    @DisplayName("Проверка работы ссылки 'Все акции и купоны'")
    void checkPromoPage() {
        step("Открытие сайта", () ->
                open(MAINPAGE));
        step("Нажатие на ссылку 'Все акции и купоны'", () ->
                $(".f-body.a0k9").click());
        step("Проверка надписи", () ->
                $(".c5r9 .a0k9").shouldHave(text("Акции и спецпредложения")));
    }

    @Test
    @DisplayName("Проверка открытия страницы 'Постаматы'")
    void checkPostamatsPage() {
        step("Открытие сайта", () ->
                open(MAINPAGE));
        step("Нажатие на ссылку 'Постаматы'", () ->
                $$("ul li").findBy(text("Постаматы")).click());
        step("Проверка надписи", () ->
                $(".content__mob-wrapper h2.title").shouldHave(text("Постамат")));
    }

    @AfterEach
    void attach() {
        String sessionId = getSessionId();
        Attach.screenshotAs("Last screenshot");
        Attach.pageSource();
        Attach.browserConsoleLogs();
        closeWebDriver();
        Attach.addVideo(sessionId);
    }
}