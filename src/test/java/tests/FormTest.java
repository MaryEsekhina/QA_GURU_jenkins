package tests;

import com.codeborne.selenide.logevents.SelenideLogger;
import com.github.javafaker.Faker;
import helpers.Attach;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterEach;
import org.openqa.selenium.remote.DesiredCapabilities;
import pages.RegistrationPage;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;


public class FormTest {
    RegistrationPage registrationPage = new RegistrationPage();

    Faker faker = new Faker();
    String name = faker.name().firstName();
    String surname = faker.name().lastName();
    String email = faker.internet().emailAddress();
    String phone = faker.phoneNumber().subscriberNumber(10);
    String gender = faker.demographic().sex();
    String subj = "English";
    String hobby = "Reading";
    String picPath = "src/test/java/../resources/wat.jpeg";
    String address = faker.address().fullAddress();
    String state = "Haryana";
    String city = "Karnal";


    @BeforeAll
    static void setup() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("enableVNC", true);
        capabilities.setCapability("enableVideo", true);

        Configuration.browserCapabilities = capabilities;
        Configuration.remote = "https://user1:1234@selenoid.autotests.cloud/wd/hub/";
        Configuration.baseUrl = "https://demoqa.com";
        Configuration.startMaximized = true;

    }
    @AfterEach
    void tearDown (){
        Attach.screenshotAs("Скриншот");
        Attach.pageSource();
        Attach.browserConsoleLogs();

    }

    @Test
     void nameFillTest() {

        step("Открываем github.com", () -> registrationPage.openPage()); //перешли по ссылке
        step("Вводим основную информацию", () -> registrationPage.enterFirstName(name)
                .enterLastName(surname)
                .enterEmail(email)
                .enterPhoneNumber(phone)
                .chooseGender(gender)
                .setBirthDate("19", "July", "1990"));
        step("Выбираем предмет и хобби", () -> registrationPage.chooseSubj(subj)
                .chooseHobby(hobby));
        step("Загружаем картинку", () -> registrationPage.loadPic(picPath));
        step("Вводим адрес", () -> registrationPage.enterAddress(address)
                .chooseState(state)
                .chooseCity(city));
        step("Нажимаем Submit", () -> registrationPage.submit());

        //проверка
        step("Нажимаем Submit", () -> {registrationPage.checkResultsTitle();
        registrationPage.checkResultsValue(name + " " + surname);
        registrationPage.checkResultsValue(email);
        registrationPage.checkResultsValue(phone);
        registrationPage.checkResultsValue("19 July,1990");
        registrationPage.checkResultsValue(subj);
        registrationPage.checkResultsValue("wat.jpeg");
        registrationPage.checkResultsValue(address);
        registrationPage.checkResultsValue(state + " " + city);});
    }
}
