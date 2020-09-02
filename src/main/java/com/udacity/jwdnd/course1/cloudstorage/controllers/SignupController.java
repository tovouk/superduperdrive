package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.models.User;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("/signup")
public class SignupController {

    private final UserService userService;

    public SignupController(UserService userService){
        this.userService = userService;
    }

    @GetMapping
    public String getSignupPage(Model model,HttpServletRequest request){
        Map<String, ?> flashAttributeMap = RequestContextUtils.getInputFlashMap(request);
        if(flashAttributeMap != null){
            model.addAttribute("signupError",(String) flashAttributeMap.get("signupError"));
        }
        return "signup";
    }

    @PostMapping
    public RedirectView signupUser(@ModelAttribute User user, RedirectAttributes redirectAttributes){
        String signupError = null;
        RedirectView view = new RedirectView("/signup", true);

        if(!userService.isUsernameAvailable(user.getUsername())){
            signupError = "The username already exists!";
        }

        if(signupError == null){
            int rowsAdded = userService.createUser(user);
            if(rowsAdded <= 0){
                signupError = "There was an error signing you up. Please try again!";
            }
        }

        if(signupError == null){
            view = new RedirectView("/login", true);
            redirectAttributes.addFlashAttribute("signupSuccess",true);
            return view;
        }else{
            redirectAttributes.addFlashAttribute("signupError",signupError);
        }

        return view;
    }

}
