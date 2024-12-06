package com.example.controller;

import com.example.model.UserDemo;
import com.example.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
    @PostMapping("/login")
    public String login(@RequestParam String email, HttpSession session) {
        UserDemo currentUser = userService.findByEmail(email);
        if (currentUser != null) {
            session.setAttribute("currentUserId", currentUser.getId());
            return "redirect:/home";
        } else {
            return "login?error=true";
        }
    }

}
