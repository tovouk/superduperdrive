package com.udacity.jwdnd.course1.cloudstorage.Pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage {

    @FindBy(id = "nav-files-tab")
    WebElement navFilesTab;

    @FindBy(id = "nav-notes-tab")
    WebElement navNotesTab;

    @FindBy(id = "nav-credentials-tab")
    WebElement navCredentialsTab;

    @FindBy(id = "logout-button")
    WebElement logoutButton;

    public HomePage(WebDriver driver){
        PageFactory.initElements(driver,this);
    }

    public String getFileTabText(){
        return navFilesTab.getText();
    }

    public String getNotesTabText(){
        return navNotesTab.getText();
    }

    public void clickNotesTab(){
        navNotesTab.click();
    }

    public String getCredentialsTabText(){
        return navCredentialsTab.getText();
    }

    public void logout(){
        logoutButton.click();
    }

}
