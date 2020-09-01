package com.udacity.jwdnd.course1.cloudstorage.Pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SignupPage {

    @FindBy(id = "inputFirstName")
    WebElement inputFirstName;

    @FindBy(id = "inputLastName")
    WebElement inputLastName;

    @FindBy(id = "inputUsername")
    WebElement inputUsername;

    @FindBy(id = "inputPassword")
    WebElement inputPassword;

    @FindBy(id = "signup")
    WebElement signupButton;

    @FindBy(id = "login-link")
    WebElement loginLink;

    @FindBy(id = "login-link2")
    WebElement loginLink2;

    public SignupPage(WebDriver driver){
        PageFactory.initElements(driver,this);
    }

    public void setFirstName(String firstName){
        inputFirstName.sendKeys(firstName);
    }

    public String getFirstName(){
        return inputFirstName.getText();
    }

    public void setLastName(String lastName){
        inputLastName.sendKeys(lastName);
    }

    public String getLastName(){
        return inputLastName.getText();
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

    public void signUp(String firstname, String lastname, String username, String password){
        setFirstName(firstname);
        setLastName(lastname);
        setUsername(username);
        setPassword(password);
        signupButton.click();
    }

    public void backToLogin(){
        loginLink.click();
    }

    public void backToLogin2(){
        loginLink2.click();
    }

}
