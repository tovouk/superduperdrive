package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.Pages.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BrowserTests {

    public static WebDriver driver;
    public LoginPage loginPage;
    public SignupPage signupPage;
    public HomePage homePage;
    public NotePage notePage;
    public CredentialPage credentialPage;
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
        String firstname = "Jose";
        String lastname = "Hinojo";
        String username = "burningremedy";
        String password = "nopassword";

        driver.get(baseUrl + "/signup");

        signupPage = new SignupPage(driver);
        signupPage.signUp(firstname,lastname,username,password);
        signupPage.backToLogin();

        driver.get(baseUrl + "/login");

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(username,password);

        HomePage homePage = new HomePage(driver);
        homePage.clickNotesTab();

        driver.get(baseUrl + "/nav-notes");

        NotePage notePage = new NotePage(driver);
        driver.manage().timeouts().implicitlyWait(1,TimeUnit.SECONDS);
        notePage.clickAddNote("Old Note", "Old Note Description");

        driver.get(baseUrl + "/nav-credentials");

        credentialPage = new CredentialPage(driver);
        credentialPage.createCredential("http://home.com","homer","simpson");


        driver.manage().timeouts().implicitlyWait(1,TimeUnit.SECONDS);
        homePage.logout();

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
        String username = "burningremedy1";
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

        String username = "burningremedy";
        String password = "nopassword";
        driver.get(baseUrl + "/login");

        loginPage = new LoginPage(driver);
        loginPage.login(username,password);

        HomePage homePage = new HomePage(driver);

        driver.get(baseUrl + "/nav-notes");

        notePage = new NotePage(driver);

        String title = "Note Title";
        String description = "Note Description";

        driver.manage().timeouts().implicitlyWait(1,TimeUnit.SECONDS);

        notePage.clickAddNote(title,description);

        List<WebElement> descriptionList = notePage.getDescriptions();

        assertThat(descriptionList.get(descriptionList.size()-1).getText()).isEqualTo(description);

    }

    @Test
    public void editExistingNote(){
        String username = "burningremedy";
        String password = "nopassword";
        driver.get(baseUrl + "/login");

        loginPage = new LoginPage(driver);
        loginPage.login(username,password);

        HomePage homePage = new HomePage(driver);

        driver.get(baseUrl + "/nav-notes");

        notePage = new NotePage(driver);

        List<WebElement> editButtons = notePage.getEditNoteButtons();

        notePage.clickButton(editButtons.get(0));
        notePage.clearNoteDescription();
        notePage.editOpenNoteDescriptionAndSave("New Description");

        List<WebElement> descriptionList = notePage.getDescriptions();

        assertThat(descriptionList.get(0).getText()).isEqualTo("New Description");

    }

    @Test
    public void deleteNoteAndConfirmDeletion(){
        String username = "burningremedy";
        String password = "nopassword";
        driver.get(baseUrl + "/login");

        loginPage = new LoginPage(driver);
        loginPage.login(username,password);

        HomePage homePage = new HomePage(driver);

        driver.get(baseUrl + "/nav-notes");

        notePage = new NotePage(driver);
        List<WebElement> deleteButtons = notePage.getDeleteNoteButtons();

        notePage.clickButton(deleteButtons.get(0));

        List<WebElement> descriptionList = notePage.getDescriptions();

        if(descriptionList.size() > 0){
            assertThat(descriptionList.get(0).getText()).isEqualTo("Old Note Description");
        }else{
            assertThat(descriptionList).isEmpty();
        }
    }

    @Test
    public void LoginAndCreateCredentialThenVerify(){
        String username = "burningremedy";
        String password = "nopassword";
        driver.get(baseUrl + "/login");

        loginPage = new LoginPage(driver);
        loginPage.login(username,password);

        HomePage homePage = new HomePage(driver);

        driver.get(baseUrl + "/nav-credentials");

        credentialPage = new CredentialPage(driver);

        String url = "https://somewebsite.com";
        String username1 = "username";
        String password1 = "password";

        credentialPage.createCredential(url,username1,password1);

        List<WebElement> urlList = credentialPage.getUrls();

        assertThat(urlList.get(urlList.size()-1).getText()).isEqualTo(url);
    }

    //TODO Write a Selenium test that logs in an existing user with existing credentials, clicks the edit credential button on an existing credential, changes the credential data, saves the changes, and verifies that the changes appear in the credential list.


    //TODO Write a Selenium test that logs in an existing user with existing credentials, clicks the delete credential button on an existing credential, and verifies that the credential no longer appears in the credential list.


}
