package com.udacity.jwdnd.course1.cloudstorage.Pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class NotePage {

    @FindBy(id = "add-note")
    WebElement addNoteButton;

    @FindBy(className = "edit-note")
    WebElement editNoteButton;

    @FindBy(className = "delete-note")
    WebElement deleteNoteButton;

    @FindBy(className = "noteTitle")
    WebElement noteTitle;

    @FindBy(className = "noteDescription")
    WebElement noteDescription;

    @FindBy(id = "emptyNotes")
    WebElement emptyNotes;

    @FindBy(id = "note-id")
    WebElement inputNoteId;

    @FindBy(id = "note-title")
    WebElement inputNoteTitle;

    @FindBy(id = "note-description")
    WebElement inputNoteDescription;

    @FindBy(id = "noteSubmit")
    WebElement noteSubmitButton;

    @FindBy(id = "close-notes")
    WebElement closeNotesButton;

    @FindBy(id = "save-note")
    WebElement saveNoteButton;

    public NotePage(WebDriver driver){
        PageFactory.initElements(driver,this);
    }

    public void setNoteId(String noteId){
        inputNoteId.sendKeys(noteId);
    }

    public String getNoteId(){
        return inputNoteId.getText();
    }

    public void setNoteTitle(String noteTitle){
        inputNoteTitle.sendKeys(noteTitle);
    }

    public String getNoteTitle(){
        return inputNoteTitle.getText();
    }

    public void setNoteDescription(String noteDescription){
        inputNoteDescription.sendKeys(noteDescription);
    }

    public String getNoteDescription(){
        return inputNoteDescription.getText();
    }

    public String getATitle(){
        return noteTitle.getText();
    }

    public String getADescription(){
        return noteTitle.getText();
    }

    public void clickAddNote(String title, String description){
        addNoteButton.click();
        System.out.println("Clicked note Button");
        setNoteTitle(title);
        setNoteDescription(description);
        saveNoteButton.click();
    }


}
