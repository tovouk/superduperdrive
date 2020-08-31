package com.udacity.jwdnd.course1.cloudstorage.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyErrorController implements ErrorController {

    @GetMapping("/error")
    public String handleError(){
        //current authentication based errors arise when restarting the application
        //the User itself doesn't exist but Authentication retains the Name, at the very least
        //thus the application believes it is authenticated
        return "login";
    }

    @Override
    public String getErrorPath() {
        return null;
    }
}
