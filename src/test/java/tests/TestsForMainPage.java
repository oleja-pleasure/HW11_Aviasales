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
    static final String MAINPAGE = "https://www.aviasales.ru/",
            CITY = "Москва",
            CITY2 = "Санкт-Петербург";

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
    @DisplayName("Проверка открытия главной страницы")
    void checkOrderList() {
        step("Открытие сайта", () ->
                open(MAINPAGE));
        step("Проверка надписи 'Поиск дешёвых авиабилетов'", () ->
                $("h1.header__title").shouldHave(text("Поиск дешёвых авиабилетов")));
    }

    @Test
    @DisplayName("Проверка ошибки при поиске с одинаковыми городами")
    void checkAuthPage() {
        step("Открытие сайта", () ->
                open(MAINPAGE));
        step("Ввод города в поле 'Откуда'", () ->
                $("#origin").val(CITY));
        step("Ввод города в поле 'Куда'", () ->
                $("#destination").val(CITY));
        step("Нажатие на кнопку 'Поиск'", () ->
                $(".--on-home").click());
        step("Проверка надписи 'Укажите разные города'", () ->
                $(".autocomplete__input-wrapper.--error").shouldHave(text("Укажите разные города")));
    }

    @Test
    @DisplayName("Проверка отображения опроса при поиске билета")
    void checkPromoPage() {
        step("Открытие сайта", () ->
                open(MAINPAGE));
        step("Ввод города в поле 'Откуда'", () ->
                $("#origin").val(CITY));
        step("Ввод города в поле 'Куда'", () ->
                $("#destination").val(CITY2));
        step("Ввод даты в поле 'Когда'", () ->{
                $(".trip-duration__input-wrapper.--departure").click();
                $(".calendar__month:nth-child(2) .calendar-day").click();
    });
        step("Снятие чекбокса открытия booking.com",() ->
                $("#clicktripz").click());
        step("Нажатие на кнопку 'Поиск'", () ->
                $(".--on-home").click());
        step("Проверка отображения надписи 'Помогите нам стать лучше — ответьте на вопрос:'",() ->
                $(".preroll-quiz__question-title").shouldHave(text("Помогите нам стать лучше&nbsp;— ответьте на&nbsp;вопрос:")));
    }

    @Test
    @DisplayName("Проверка открытия страницы 'Спецпредложения'")
    void checkPostamatsPage() {
        step("Открытие сайта", () ->
                open(MAINPAGE));
        step("Нажатие на кнопку 'Сервисы'", () ->
                $(".navbar-services").click());
        step("Открытие страницы 'Спецпредложения'", ()->
                $(".menu__link[title='Спецпредложения']").click());
        step("Проверка надписи 'Поиск спецпредложений на авиабилеты'", () ->
                $(".offer-list-page__header").shouldHave(text("Поиск спецпредложений на авиабилеты")));
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