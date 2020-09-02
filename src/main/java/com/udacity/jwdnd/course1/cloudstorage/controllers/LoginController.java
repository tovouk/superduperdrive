package com.udacity.jwdnd.course1.cloudstorage.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("/login")
public class LoginController {
    @GetMapping
    public String getLoginPage(Model model, HttpServletRequest request){
        Map<String, ?> flashAttributeMap = RequestContextUtils.getInputFlashMap(request);
        if(flashAttributeMap != null) {
            model.addAttribute("signupSuccess", flashAttributeMap.get("signupSuccess"));
        }
        return "login";
    }
}
