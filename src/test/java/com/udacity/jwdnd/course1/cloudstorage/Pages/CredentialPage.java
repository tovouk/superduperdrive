package com.udacity.jwdnd.course1.cloudstorage.Pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class CredentialPage {

    @FindBy(id = "add-credential")
    WebElement addCredentialButton;

    @FindBy(id = "credential-url")
    WebElement inputUrl;

    @FindBy(id = "credential-username")
    WebElement inputUsername;

    @FindBy(id = "credential-password")
    WebElement inputPassword;

    @FindBy(id = "credential-submit")
    WebElement saveCredentialButton;

    @FindBy(id = "credential-close")
    WebElement cancelCredentialButton;

    @FindBy(className = "edit-credential")
    List<WebElement> editButtons;

    @FindBy(className = "delete-credential")
    List<WebElement> deleteButtons;

    @FindBy(className = "credentialUrl")
    List<WebElement> credentialUrls;

    @FindBy(className = "credentialUsername")
    List<WebElement> credentialUsernames;

    @FindBy(className = "credentialPassword")
    List<WebElement> credentialPasswords;

    public CredentialPage(WebDriver driver){
        PageFactory.initElements(driver,this);
    }

    public void clickAddButton(){
        addCredentialButton.click();
    }

    public void setInputUrl(String url){
        inputUrl.clear();
        inputUrl.sendKeys(url);
    }

    public String getUrl(){
        return inputUrl.getText();
    }

    public void setUsername(String username){
        inputUsername.clear();
        inputUsername.sendKeys(username);
    }

    public String getUsername(){
        return inputUsername.getText();
    }

    public void setPassword(String password){
        inputPassword.clear();
        inputPassword.sendKeys(password);
    }

    public String getPassword(){
        return inputPassword.getText();
    }

    public void clickButton(WebElement element){
        element.click();
    }

    public void saveCredential(){
        saveCredentialButton.click();
    }

    public void createCredential(String url, String username, String password){
        addCredentialButton.click();
        setInputUrl(url);
        setUsername(username);
        setPassword(password);
        saveCredentialButton.click();
    }

    public void editCredential(String url, String username, String password){
        setInputUrl(url);
        setUsername(username);
        setPassword(password);
        saveCredentialButton.click();
    }

    public List<WebElement> getEditButtons(){
        return editButtons;
    }

    public List<WebElement> getDeleteButtons(){
        return deleteButtons;
    }

    public List<WebElement> getUrls(){
        return credentialUrls;
    }

    public List<WebElement> getUsernames(){
        return credentialUsernames;
    }

}
