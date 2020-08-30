package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.models.Credential;
import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import com.udacity.jwdnd.course1.cloudstorage.models.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;

@Controller
public class HomeController {

    private NoteService noteService;
    private UserService userService;
    private CredentialService credentialService;
    private EncryptionService encryptionService;

    public HomeController(NoteService noteService, UserService userService, CredentialService credentialService, EncryptionService encryptionService) {
        this.noteService = noteService;
        this.userService = userService;
        this.credentialService = credentialService;
        this.encryptionService = encryptionService;
    }

    @GetMapping("/")
    public String getHomePage(Authentication authentication,Model model){
        User user = userService.getUser(authentication.getName());
        model.addAttribute("notes",noteService.getNotes(user.getUserId()));
        model.addAttribute("credentials", credentialService.getCredentials(user.getUserId()));
        return "home";
    }

    @GetMapping("/nav-files")
    public String getFiles(Authentication authentication, Model model){
        User user = userService.getUser(authentication.getName());
        model.addAttribute("notes",noteService.getNotes(user.getUserId()));
        model.addAttribute("credentials", credentialService.getCredentials(user.getUserId()));
        return "home";
    }

    @GetMapping("/nav-notes")
    public String getNotes(Authentication authentication, Model model, HttpServletRequest request){
        User user = userService.getUser(authentication.getName());
        model.addAttribute("notes",noteService.getNotes(user.getUserId()));
        model.addAttribute("credentials", credentialService.getCredentials(user.getUserId()));
        Map<String, ?> flashAttributeMap = RequestContextUtils.getInputFlashMap(request);
        if(flashAttributeMap != null){
            model.addAttribute("successMessage",(String) flashAttributeMap.get("successMessage"));
            model.addAttribute("errorMessage",(String) flashAttributeMap.get("errorMessage"));
        }
        return "home";
    }

    @PostMapping("/nav-notes")
    public String postNote(Authentication authentication, @ModelAttribute Note note , Model model){
        User user = userService.getUser(authentication.getName());
        note.setUserId(user.getUserId());
        if(this.noteService.createNote(note) > 0){
            model.addAttribute("successMessage","Note Successfully created");
        }else {
            model.addAttribute("errorMessage", "Could not create note. Please try again");
        }
        model.addAttribute("notes",noteService.getNotes(user.getUserId()));
        model.addAttribute("credentials", credentialService.getCredentials(user.getUserId()));
        return "home";
    }

    @PostMapping("/nav-notes/{noteId}")
    public RedirectView updateNote(Authentication authentication,@PathParam("noteId") Integer noteId, @ModelAttribute Note note,
                                   RedirectAttributes redirectAttributes, HttpServletRequest request){
        User user = userService.getUser(authentication.getName());
        note.setUserId(user.getUserId());
        RedirectView view = new RedirectView("/nav-notes",true);
        if(noteId == note.getNoteId()){
            noteService.updateNote(note);
            redirectAttributes.addFlashAttribute("successMessage","Note Successfully updated");
        }else{
            redirectAttributes.addFlashAttribute("errorMessage","Error Updating Note");
        }
        return view;
    }

    @PostMapping("/nav-notes/delete/{noteId}")
    public RedirectView deleteNote(@PathVariable("noteId") Integer noteId, ModelMap model,
                                   RedirectAttributes redirectAttributes, HttpServletRequest request){
        RedirectView view = new RedirectView("/nav-notes",true);
        if(noteService.deleteNote(noteId) > 0){
            redirectAttributes.addFlashAttribute("successMessage","Note Deleted Successfully");
        }else{
            redirectAttributes.addFlashAttribute("errorMessage","Note was not Deleted. Please try again!");
        }
        return view;
    }

    @GetMapping("/nav-credentials")
    public String getCredentials(Authentication authentication, Model model, HttpServletRequest request){
        User user = userService.getUser(authentication.getName());
        model.addAttribute("notes",noteService.getNotes(user.getUserId()));
        model.addAttribute("credentials", credentialService.getCredentials(user.getUserId()));
        Map<String, ?> flashAttributeMap = RequestContextUtils.getInputFlashMap(request);
        if(flashAttributeMap != null){
            model.addAttribute("credentialSuccess",(String) flashAttributeMap.get("credentialSuccess"));
            model.addAttribute("credentialError",(String) flashAttributeMap.get("credentialError"));
        }
        return "home";
    }

    @PostMapping("/nav-credentials")
    public String postCredential(Authentication authentication, @ModelAttribute Credential credential,Model model){
        User user = userService.getUser(authentication.getName());
        credential.setUserId(user.getUserId());

        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(),encodedKey);
        credential.setKey(encodedKey);
        credential.setPassword(encryptedPassword);

        if(this.credentialService.createCredential(credential) > 0){
            model.addAttribute("credentialSuccess","Credential Successfully created");
        }else {
            model.addAttribute("credentialError", "Could not create Credential. Please try again");
        }
        model.addAttribute("notes",noteService.getNotes(user.getUserId()));
        model.addAttribute("credentials", credentialService.getCredentials(user.getUserId()));
        return "home";
    }

    @PostMapping("/nav-credentials/{credentialId}")
    public RedirectView updateCredential(Authentication authentication, @PathVariable("credentialId") Integer credentialId,
                                         @ModelAttribute Credential credential, RedirectAttributes redirectAttributes,
                                         HttpServletRequest request){
        RedirectView view = new RedirectView("/nav-credentials",true);
        User user = userService.getUser(authentication.getName());
        credential.setUserId(user.getUserId());
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(),encodedKey);
        credential.setKey(encodedKey);
        credential.setPassword(encryptedPassword);
        if(credentialService.updateCredential(credential) > 0){
            redirectAttributes.addFlashAttribute("credentialSuccess","Credential Updated Successfully");
        }else{
            redirectAttributes.addFlashAttribute("credentialError","Credential was not Updated. Please try again!");
        }

        return view;
    }


    @PostMapping("/nav-credentials/delete/{credentialId}")
    public RedirectView deleteCredential(@PathVariable("credentialId") Integer credentialId, RedirectAttributes redirectAttributes){
        RedirectView view = new RedirectView("/nav-credentials",true);
        if(credentialService.deleteCredential(credentialId) > 0){
            redirectAttributes.addFlashAttribute("credentialSuccess","Credential Deleted Successfully");
        }else{
            redirectAttributes.addFlashAttribute("credentialError","Credential was not Deleted. Please try again!");
        }
        return view;
    }


}
