package com.webkorps.friendBook.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

@Controller
public class HomeController {

    @GetMapping({"/", ""})
    public String home() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
     
    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/privacy")
    public String privacy() {
        return "privacy";
    }

    @GetMapping("/forgot-password")
    public String forgotPassword() {
        return "forgot-password";
    }

    @GetMapping("/reset-password")
    public String resetPassword(@RequestParam String token, Model model) {
        model.addAttribute("token", token);
        return "reset-password-form";
    }
    
    @GetMapping("/profile")
    public String profile() {
        return "profile";
    }
}
