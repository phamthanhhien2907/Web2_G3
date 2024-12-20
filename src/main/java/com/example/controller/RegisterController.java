package com.example.controller;

import com.example.model.Company;
import com.example.model.Role;
import com.example.model.User;
import com.example.model.UserDemo;
import com.example.service.RoleService;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class RegisterController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleService roleService;

    // Show registration form
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("userDemo", new UserDemo());
        return "register";
    }

    // Handle registration form submission
    @PostMapping("/register")
    public String registerUser(Model model, @ModelAttribute("userDemo") UserDemo user) {
        if (userService.findByEmail(user.getEmail()) != null) {
            model.addAttribute("error", "Email is already taken");
            return "register";
        }
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        Role userRole = roleService.findByRole("USER"); // Find the "ROLE_USER"
        if (userRole == null) {
            userRole = new Role();
            userRole.setRole("USER");  // Create "ROLE_USER" if it doesn't exist
            roleService.save(userRole);  // Save the role if it's new
        }

        user.setRole(Set.of(userRole));
        userService.saveOrUpdate(user);
        return "redirect:/login";
    }
    @PostMapping("/api/register")
    @ResponseBody
    public ResponseEntity<?> register(@RequestBody UserDemo user) {
        if (user == null || user.getEmail() == null || user.getPassword() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user data.");
        }

        if (userService.findByEmail(user.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email is already takend");
        }
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        Role userRole = roleService.findByRole("USER"); // Find the "ROLE_USER"
        if (userRole == null) {
            userRole = new Role();
            userRole.setRole("USER");  // Create "ROLE_USER" if it doesn't exist
            roleService.save(userRole);  // Save the role if it's new
        }

        user.setRole(Set.of(userRole));
        userService.saveOrUpdate(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
}
