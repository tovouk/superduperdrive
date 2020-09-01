package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.Pages.LoginPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BrowserTests {

    /*
    OPTIONAL:

    Recommended: Use page objects to abstract selenium element selection and actions.

    Test file upload and download with selenium. This will require some extra research!

    @Test
    public void testUserSignupLoginAndSubmitMessage(){
        String username = "burningremedy";
        String password = "nopassword";
        String messageText = "Hello";

        driver.get(baseUrl + "/signup");

        signupPage = new SignupPage(driver);
        signupPage.signUp("Jose","Hinojo",username,password);

        driver.get(baseUrl + "/login");

        loginPage = new LoginPage(driver);
        loginPage.login(username,password);

        chatPage = new ChatPage(driver);
        chatPage.sendMessage(messageText);

        ChatMessage sentMessage = chatPage.getFirstMessage();

        assertEquals(username,sentMessage.getUsername());
        assertEquals(messageText,sentMessage.getMessageText());

    }

     */

    public static WebDriver driver;
    public LoginPage loginPage;
    public String baseUrl;

    @LocalServerPort
    private int port;

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
    }

    @AfterAll
    static void afterAll(){
        driver.quit();
        driver = null;
    }

    @BeforeEach
    public void beforeEach() {
        baseUrl = "http://localhost:" + port;
    }

    @Test
    public void verifyHomePageNotAccessibleForUnauthorizedVisitors(){
        driver.get(baseUrl);

        assertThat(baseUrl).isNotEqualTo(driver.getCurrentUrl());
        assertThat(driver.getCurrentUrl()).isEqualTo(baseUrl + "/login");
    }

    //TODO Write a Selenium test that signs up a new user, logs that user in, verifies that they can access the home page, then logs out and verifies that the home page is no longer accessible.


    //TODO Write a Selenium test that logs in an existing user, creates a note and verifies that the note details are visible in the note list.


    //TODO Write a Selenium test that logs in an existing user with existing notes, clicks the edit note button on an existing note, changes the note data, saves the changes, and verifies that the changes appear in the note list.


    //TODO Write a Selenium test that logs in an existing user with existing notes, clicks the delete note button on an existing note, and verifies that the note no longer appears in the note list.


    //TODO Write a Selenium test that logs in an existing user, creates a credential and verifies that the credential details are visible in the credential list.


    //TODO Write a Selenium test that logs in an existing user with existing credentials, clicks the edit credential button on an existing credential, changes the credential data, saves the changes, and verifies that the changes appear in the credential list.


    //TODO Write a Selenium test that logs in an existing user with existing credentials, clicks the delete credential button on an existing credential, and verifies that the credential no longer appears in the credential list.


}
