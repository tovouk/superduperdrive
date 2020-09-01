package com.udacity.jwdnd.course1.cloudstorage.Pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {

    @FindBy(id = "inputUsername")
    WebElement inputUsername;

    @FindBy(id = "inputPassword")
    WebElement inputPassword;

    @FindBy(id = "login")
    WebElement loginButton;

    @FindBy(id = "signup-link")
    WebElement signupLink;

    public LoginPage(WebDriver driver){
        PageFactory.initElements(driver,this);
    }

    public void setUsername(String username){
        inputUsername.sendKeys(username);
    }

    public String getUsername(){
        return inputUsername.getText();
    }

    public void setPassword(String password){
        inputPassword.sendKeys(password);
    }

    public String getPassword(){
        return inputPassword.getText();
    }

    public void login(String username, String password){
        setUsername(username);
        setPassword(password);
        loginButton.click();
    }

    public void goToSignUp(){
        signupLink.click();
    }

}
