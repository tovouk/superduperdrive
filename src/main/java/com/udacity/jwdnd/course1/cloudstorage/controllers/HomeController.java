package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.models.Credential;
import com.udacity.jwdnd.course1.cloudstorage.models.File;
import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import com.udacity.jwdnd.course1.cloudstorage.models.User;
import com.udacity.jwdnd.course1.cloudstorage.services.*;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;

@Controller
public class HomeController {

    private FileService fileService;
    private NoteService noteService;
    private UserService userService;
    private CredentialService credentialService;
    private EncryptionService encryptionService;

    public HomeController(FileService fileService,NoteService noteService, UserService userService,
                          CredentialService credentialService, EncryptionService encryptionService) {
        this.fileService = fileService;
        this.noteService = noteService;
        this.userService = userService;
        this.credentialService = credentialService;
        this.encryptionService = encryptionService;
    }

    //TODO If a user knows the file, note, or credential ID of another user,
    //make sure they can’t make a direct request through the browser to view, edit, or delete that file, note, or credential
    //unless it is theirs

    @GetMapping("/")
    public String getHomePage(Authentication authentication,Model model){
        User user = userService.getUser(authentication.getName());
        model.addAttribute("files",fileService.getFiles(user.getUserId()));
        model.addAttribute("notes",noteService.getNotes(user.getUserId()));
        model.addAttribute("credentials", credentialService.getCredentials(user.getUserId()));
        return "home";
    }

    @GetMapping("/nav-files")
    public String getFiles(Authentication authentication, Model model, HttpServletRequest request){
        User user = userService.getUser(authentication.getName());
        model.addAttribute("files",fileService.getFiles(user.getUserId()));
        model.addAttribute("notes",noteService.getNotes(user.getUserId()));
        model.addAttribute("credentials", credentialService.getCredentials(user.getUserId()));
        Map<String, ?> flashAttributeMap = RequestContextUtils.getInputFlashMap(request);
        if(flashAttributeMap != null){
            model.addAttribute("fileSuccess",(String) flashAttributeMap.get("fileSuccess"));
            model.addAttribute("fileError",(String) flashAttributeMap.get("fileError"));
        }
        return "home";
    }

    @GetMapping("/nav-files/{filename}")
    public ResponseEntity<Resource> getFile(Authentication authentication,@PathVariable String filename) throws IOException{
        User user = userService.getUser(authentication.getName());
        File file = fileService.getFile(filename,user.getUserId());
        if(file == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; file=\"" + file.getFileName() + "\"")
                .body(new ByteArrayResource(file.getFileData()));
    }

    @PostMapping("/nav-files")
    public String postFile(Authentication authentication, MultipartFile fileUpload, Model model) throws IOException {
        User user = userService.getUser(authentication.getName());
        File file = new File(null,fileUpload.getOriginalFilename(),fileUpload.getContentType(),String.valueOf(fileUpload.getSize()),user.getUserId(),fileUpload.getBytes());
        if(fileService.getFile(file.getFileName(),user.getUserId()) != null){
            model.addAttribute("fileError","File with that name already exists!");
        }else if(this.fileService.insertFile(file) > 0){
            model.addAttribute("fileSuccess","File successfully uploaded");
        }else {
            model.addAttribute("fileError", "Could not upload that file. Please try again");
        }
        model.addAttribute("files",fileService.getFiles(user.getUserId()));
        model.addAttribute("notes",noteService.getNotes(user.getUserId()));
        model.addAttribute("credentials", credentialService.getCredentials(user.getUserId()));
        return "home";
    }

    @PostMapping("/nav-files/delete/{fileId}")
    public RedirectView deleteFile(Authentication authentication, @PathVariable("fileId") Integer fileId, RedirectAttributes redirectAttributes) {
        RedirectView view = new RedirectView("/nav-files", true);

        File file = fileService.getFileById(fileId,userService.getUser(authentication.getName()).getUserId());

        if(file == null) {
            redirectAttributes.addFlashAttribute("fileError", "Error: File not found!");
        } else if (fileService.deleteFile(fileId) > 0) {
            redirectAttributes.addFlashAttribute("fileSuccess", "File Deleted Successfully");
        } else {
            redirectAttributes.addFlashAttribute("fileError", "The File was not Deleted. Please try again!");
        }
        return view;
    }

    @GetMapping("/nav-notes")
    public String getNotes(Authentication authentication, Model model, HttpServletRequest request){
        User user = userService.getUser(authentication.getName());
        model.addAttribute("files",fileService.getFiles(user.getUserId()));
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
        model.addAttribute("files",fileService.getFiles(user.getUserId()));
        model.addAttribute("notes",noteService.getNotes(user.getUserId()));
        model.addAttribute("credentials", credentialService.getCredentials(user.getUserId()));
        return "home";
    }

    @PostMapping("/nav-notes/{noteId}")
    public RedirectView updateNote(Authentication authentication,@PathParam("noteId") Integer noteId, @ModelAttribute Note note,
                                   RedirectAttributes redirectAttributes, HttpServletRequest request){
        User user = userService.getUser(authentication.getName());
        Note realNote = noteService.getNote(noteId);
        realNote.setNoteTitle(note.getNoteTitle());
        realNote.setNoteDescription(note.getNoteDescription());
        RedirectView view = new RedirectView("/nav-notes",true);
        if(realNote.getUserId() != user.getUserId()) {
            redirectAttributes.addFlashAttribute("errorMessage","Error: Note not Found");
        } else if(noteId == realNote.getNoteId()){
            noteService.updateNote(realNote);
            redirectAttributes.addFlashAttribute("successMessage","Note Successfully updated");
        }else{
            redirectAttributes.addFlashAttribute("errorMessage","Error Updating Note");
        }
        return view;
    }

    @PostMapping("/nav-notes/delete/{noteId}")
    public RedirectView deleteNote(Authentication authentication,@PathVariable("noteId") Integer noteId, ModelMap model,
                                   RedirectAttributes redirectAttributes, HttpServletRequest request){
        User user = userService.getUser(authentication.getName());
        Note realNote = noteService.getNote(noteId);
        RedirectView view = new RedirectView("/nav-notes",true);
        if(realNote.getUserId() != user.getUserId()) {
            redirectAttributes.addFlashAttribute("errorMessage","Error: Note not Found");
        } else if (noteService.deleteNote(noteId) > 0){
            redirectAttributes.addFlashAttribute("successMessage","Note Deleted Successfully");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage","Note was not Deleted. Please try again!");
        }
        return view;
    }

    @GetMapping("/nav-credentials")
    public String getCredentials(Authentication authentication, Model model, HttpServletRequest request){
        User user = userService.getUser(authentication.getName());
        model.addAttribute("files",fileService.getFiles(user.getUserId()));
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
        model.addAttribute("files",fileService.getFiles(user.getUserId()));
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
        Credential realCredential = credentialService.getCredential(credentialId,user.getUserId());
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(),encodedKey);
        realCredential.setUrl(credential.getUrl());
        realCredential.setUsername(credential.getUsername());
        realCredential.setKey(encodedKey);
        realCredential.setPassword(encryptedPassword);
        if(realCredential == null) {
            redirectAttributes.addFlashAttribute("credentialError","This credential does not exist");
        } else if (credentialService.updateCredential(realCredential) > 0){
            redirectAttributes.addFlashAttribute("credentialSuccess","Credential Updated Successfully");
        } else {
            redirectAttributes.addFlashAttribute("credentialError","Credential was not Updated. Please try again!");
        }

        return view;
    }


    @PostMapping("/nav-credentials/delete/{credentialId}")
    public RedirectView deleteCredential(Authentication authentication, @PathVariable("credentialId") Integer credentialId,
                                         RedirectAttributes redirectAttributes){
        RedirectView view = new RedirectView("/nav-credentials",true);
        User user = userService.getUser(authentication.getName());
        Credential realCredential = credentialService.getCredential(credentialId,user.getUserId());
        if(realCredential == null) {
            redirectAttributes.addFlashAttribute("credentialError","This credential does not exist");
        }else if(credentialService.deleteCredential(credentialId) > 0){
            redirectAttributes.addFlashAttribute("credentialSuccess","Credential Deleted Successfully");
        }else{
            redirectAttributes.addFlashAttribute("credentialError","Credential was not Deleted. Please try again!");
        }
        return view;
    }


}
