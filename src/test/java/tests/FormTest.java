package tests;

import com.codeborne.selenide.logevents.SelenideLogger;
import com.github.javafaker.Faker;
import io.qameta.allure.selenide.AllureSelenide;
import org.openqa.selenium.remote.DesiredCapabilities;
import pages.RegistrationPage;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


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

    @Test
    void nameFillTest() {

        registrationPage.openPage(); //перешли по ссылке
        registrationPage.enterFirstName(name)
                .enterLastName(surname)
                .enterEmail(email)
                .enterPhoneNumber(phone)
                .chooseGender(gender)
                .setBirthDate("19", "July", "1990")
                .chooseSubj(subj)
                .chooseHobby(hobby)
                .loadPic(picPath)
                .enterAddress(address)
                .chooseState(state)
                .chooseCity(city)
                .submit();

        //проверка
        registrationPage.checkResultsTitle();
        registrationPage.checkResultsValue(name + " " + surname);
        registrationPage.checkResultsValue(email);
        registrationPage.checkResultsValue(phone);
        registrationPage.checkResultsValue("19 July,1990");
        registrationPage.checkResultsValue(subj);
        registrationPage.checkResultsValue("wat.jpeg");
        registrationPage.checkResultsValue(address);
        registrationPage.checkResultsValue(state + " " + city);
    }
}
