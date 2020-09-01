package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.Pages.HomePage;
import com.udacity.jwdnd.course1.cloudstorage.Pages.LoginPage;
import com.udacity.jwdnd.course1.cloudstorage.Pages.NotePage;
import com.udacity.jwdnd.course1.cloudstorage.Pages.SignupPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BrowserTests {

    public static WebDriver driver;
    public LoginPage loginPage;
    public SignupPage signupPage;
    public HomePage homePage;
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

    @Test
    public void verifyLoggedInHomePageLogoutLoginPage(){
        String firstname = "Jose";
        String lastname = "Hinojo";
        String username = "burningremedy";
        String password = "nopassword";

        driver.get(baseUrl + "/signup");

        signupPage = new SignupPage(driver);
        signupPage.signUp(firstname,lastname,username,password);
        signupPage.backToLogin();

        driver.get(baseUrl + "/login");

        loginPage = new LoginPage(driver);
        loginPage.login(username,password);

        driver.get(baseUrl);

        homePage = new HomePage(driver);

        assertThat(homePage.getFileTabText()).isEqualTo("Files");

        homePage.logout();

        assertThat(driver.getCurrentUrl()).isEqualTo(baseUrl + "/login?logout");
    }

    @Test
    public void loginAndCreateNoteThenViewIt(){
        String firstname = "Jose";
        String lastname = "Hinojo";
        String username = "burningremedy";
        String password = "nopassword";

        driver.get(baseUrl + "/signup");

        signupPage = new SignupPage(driver);
        signupPage.signUp(firstname,lastname,username,password);
        signupPage.backToLogin();

        driver.get(baseUrl + "/login");

        loginPage = new LoginPage(driver);
        loginPage.login(username,password);

        HomePage homePage = new HomePage(driver);

        driver.get(baseUrl + "/nav-notes");

        NotePage notePage = new NotePage(driver);

        String title = "Note Title";
        String description = "Note Description";

        WebDriverWait wait = new WebDriverWait(driver,5);

        driver.manage().timeouts().implicitlyWait(1,TimeUnit.SECONDS);

        notePage.clickAddNote(title,description);

        WebElement noteDescription = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.className("noteDescription")));

        assertThat(noteDescription.getText()).isEqualTo(description);

    }

    //TODO Write a Selenium test that logs in an existing user with existing notes, clicks the edit note button on an existing note, changes the note data, saves the changes, and verifies that the changes appear in the note list.


    //TODO Write a Selenium test that logs in an existing user with existing notes, clicks the delete note button on an existing note, and verifies that the note no longer appears in the note list.


    //TODO Write a Selenium test that logs in an existing user, creates a credential and verifies that the credential details are visible in the credential list.


    //TODO Write a Selenium test that logs in an existing user with existing credentials, clicks the edit credential button on an existing credential, changes the credential data, saves the changes, and verifies that the changes appear in the credential list.


    //TODO Write a Selenium test that logs in an existing user with existing credentials, clicks the delete credential button on an existing credential, and verifies that the credential no longer appears in the credential list.


}
