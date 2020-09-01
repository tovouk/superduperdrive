package com.udacity.jwdnd.course1.cloudstorage.Pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class NotePage {

    @FindBy(id = "add-note")
    WebElement addNoteButton;

    @FindBy(className = "edit-note")
    List<WebElement> editNoteButtons;

    @FindBy(className = "delete-note")
    List<WebElement> deleteNoteButtons;

    @FindBy(className = "noteTitle")
    List<WebElement> noteTitles;

    @FindBy(className = "noteDescription")
    List<WebElement> noteDescriptions;

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

    public void clearNoteDescription(){
        inputNoteDescription.clear();
    }

    public List<WebElement> getTitles(){
        return noteTitles;
    }

    public List<WebElement> getDescriptions(){ return noteDescriptions; }

    public List<WebElement> getEditNoteButtons(){
        return editNoteButtons;
    }

    public List<WebElement> getDeleteNoteButtons(){
        return deleteNoteButtons;
    }

    public void clickButton(WebElement element){
        element.click();
    }

    public void clickAddNote(String title, String description){
        addNoteButton.click();
        setNoteTitle(title);
        setNoteDescription(description);
        saveNoteButton.click();
    }

    public void editOpenNoteDescriptionAndSave(String description){
        setNoteDescription(description);
        saveNoteButton.click();
    }

}
