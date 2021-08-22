package tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import com.github.javafaker.Faker;
import config.CredentialsConfig;
import io.qameta.allure.selenide.AllureSelenide;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.remote.DesiredCapabilities;
import pages.RegistrationPage;
import org.junit.jupiter.api.Test;
import tests.TestBase;

import static io.qameta.allure.Allure.step;
import static java.lang.String.format;


public class FormTest  {
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

        CredentialsConfig credentials = ConfigFactory.create(CredentialsConfig.class);
        String login = credentials.login();
        String password = credentials.password();
        Configuration.browserCapabilities = capabilities;
        Configuration.remote = format("https://%s:%s@selenoid.autotests.cloud/wd/hub/",login, password);
        Configuration.baseUrl = "https://demoqa.com";
        Configuration.startMaximized = true;

    }

    @Test
     void nameFillTest(){

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
