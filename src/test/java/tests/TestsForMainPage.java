package tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import config.Credentials;
import helpers.Attach;
import io.qameta.allure.Description;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.remote.DesiredCapabilities;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static helpers.Attach.getSessionId;
import static helpers.CityChange.getCity;
import static helpers.GetProperty.readProperty;
import static io.qameta.allure.Allure.step;

public class TestsForMainPage {

    String good = "iphone",
            city,
            newCity;

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
    @Description("Проверка работы поиска")
    void checkSearch() {
        step("Открытие сайта", () ->
                open("https://www.ozon.ru/"));
        step("Поиск товара", () ->
                $("[name='text']").val(good).pressEnter());
        step("Проверка текста", () ->
                $(".tile-hover-target .item").shouldHave(text(good)));
    }

    @Test
    @Description("Проверка смены города")
    void changeCity() {
        step("Открытие сайта", () ->
                open("https://www.ozon.ru/"));
        step("Проверка текущего города", () ->
                city = $("[tabindex='-1'] div").getText());
        step("Клик на город", () ->
                $("[tabindex='0']").click());
        step("Выбор города", () -> {
            newCity = getCity(city);
            $("._10Zs ._16XE._2HHF").val(newCity).pressEnter();
        });
        step("Проверка выбранного города", () ->
                $("[tabindex='-1'] div").shouldHave(text(newCity)));
    }

    @Test
    @Description("Проверка страницы 'Избранное'")
    void checkOrderList() {
        step("Открытие сайта", () ->
                open("https://www.ozon.ru/"));
        step("Открытие страницы 'Заказы'", () ->
                $("[data-widget='favoriteCounter']").click());
        step("Проверка открытия страницы", () ->
                $(".a4b8").shouldHave(text("В Избранном пока ничего нет")));
    }

    @Test
    @Description("Проверка страницы 'Корзина'")
    void checkCart() {
        step("Открытие сайта", () ->
                open("https://www.ozon.ru/"));
        step("Открытие страницы 'Корзина'", () ->
                $("[data-widget='headerIcon']").click());
        step("Проверка открытия страницы", () ->
                $(".b4k8").shouldHave(text("Корзина пуста")));
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