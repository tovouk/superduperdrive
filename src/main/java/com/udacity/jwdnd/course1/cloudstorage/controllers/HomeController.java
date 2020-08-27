package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import com.udacity.jwdnd.course1.cloudstorage.models.User;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    private NoteService noteService;
    private UserService userService;

    public HomeController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String getHomePage(Model model){
        model.addAttribute("notes",noteService.getNotes());
        return "home";
    }

    @GetMapping("/nav-files")
    public String getFiles(Model model){
        model.addAttribute("notes",noteService.getNotes());
        return "home";
    }

    @GetMapping("/nav-notes")
    public String getNotes(Model model){
        model.addAttribute("notes",noteService.getNotes());
        return "home";
    }

    @PostMapping("/nav-notes")
    public String postNote(@ModelAttribute Note note , Model model,Authentication authentication){
        User user = userService.getUser(authentication.getName());
        System.out.println(authentication.getName());
        System.out.println(user.getUserId());
        note.setUserId(user.getUserId());
        this.noteService.createNote(note);
        model.addAttribute("notes",this.noteService.getNotes());
        return "home";
    }


}
