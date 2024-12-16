package com.example.controller;

import com.example.model.UserDemo;
import com.example.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {

    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }
//    @PostMapping("/api/login")
//    @ResponseBody
//    public ResponseEntity<?> loginUser(@RequestBody UserDemo user) {
//        UserDemo currentUser = userService.findByEmail(user.getEmail());
//
//        System.out.println("currentUser" + currentUser);
//        if (currentUser != null) {
//            return ResponseEntity.status(HttpStatus.CREATED).body(currentUser);
//        } else {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                    .body("Invalid email not found.");
//        }
//    }
//    @GetMapping("/login")
//    public String login() {
//        return "login";
//    }
//    @PostMapping("/login")
//    public String login(@RequestParam String email, HttpSession session) {
//        UserDemo currentUser = userService.findByEmail(email);
//
//        if (currentUser != null) {
//            session.setAttribute("currentUserId", currentUser.getId());
//            return "redirect:/home";
//        } else {
//            return "login?error=true";
//        }
//    }


}
