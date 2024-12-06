package com.example.controller;

import com.example.model.Company;
import com.example.model.User;
import com.example.model.UserDemo;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.example.service.UserService;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RestController()
public class UserController {

    private final UserService userService;
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    private static final Logger logger = Logger.getLogger(UserController.class.getName());

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public String trangChiTiet(@ModelAttribute("userName") String userName, HttpSession session, Model model) {
//        List<UserDemo> list = userService.getAllUser();
//        model.addAttribute("userDemo", list);
//        return "user";
        Integer currentUserId = (Integer) session.getAttribute("currentUserId");

        if (currentUserId != null) {
            List<UserDemo> allUsers = userService.getAllUser();

            List<UserDemo> filteredUsers = allUsers.stream()
                    .filter(user -> !user.getId().equals(currentUserId))
                    .collect(Collectors.toList());
            model.addAttribute("userDemo", filteredUsers);
        } else {
            model.addAttribute("message", "No user logged in");
        }

        return "user";
    }

    @GetMapping("/addUser")
    public String addUser(Model model) {
        model.addAttribute("user", new UserDemo());
        return "addUser";
    }
    @GetMapping("/updateUser/{id}")
    public String showUpdateForm(@PathVariable int id,Model model) {
        UserDemo user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "updateUser";
    }
    @PostMapping("/updateUser/{id}")
    public String updateCompany(@PathVariable int id, String firstName, String lastName,@ModelAttribute("company") Company company, @ModelAttribute("user") UserDemo user) {
        user.setId(id);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setCompany(company);
        userService.saveOrUpdate(user);
        return "redirect:/user";
    }
    @PostMapping("/addUser")
    public String saveUser(@ModelAttribute("user") UserDemo user, RedirectAttributes redirectAttributes) {
        String fullName = user.getFirstName() + " " + user.getLastName();
        redirectAttributes.addFlashAttribute("userName", fullName);
        userService.saveOrUpdate(user);
        return "redirect:/user";
    }
    @GetMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable int id) {
        userService.deleteUserById(id);
        return "redirect:/user";
    }

    // resApi
    @GetMapping("/api/users")
    public List<UserDemo> users() {
        List<UserDemo> users = userService.getAllUser();
        return users;
    }
    @PostMapping("/api/addNewUser")
    @ResponseBody
    public ResponseEntity<UserDemo> saveUser(@RequestBody UserDemo user) {
        System.out.println("Received user: " + user);
        String fullName = user.getFirstName() + " " + user.getLastName();
        System.out.println("Full Name: " + fullName);
        user.setPassword(passwordEncoder().encode(user.getPassword()));
        userService.saveOrUpdate(user);
        return ResponseEntity.ok(user);
    }
    @PutMapping("/api/updateUser/{id}")
    @ResponseBody
    public ResponseEntity<String> update(@PathVariable int id, @RequestBody UserDemo user) {
        UserDemo userExisting = userService.getUserById(id);
        if(userExisting == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id not found");
        }
        userExisting.setId(user.getId());
        userExisting.setFirstName(user.getFirstName());
        userExisting.setLastName(user.getLastName());
        userExisting.setEmail(user.getEmail());
        userExisting.setCompany(user.getCompany());
        userExisting.setPassword(passwordEncoder().encode(user.getPassword()));
        userService.saveOrUpdate(userExisting);
        return ResponseEntity.ok("User with ID " + id + " successfully updated");
    }
    @DeleteMapping("/api/deleteUser/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        UserDemo user = userService.getUserById(id);
        if(user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id not found");
        }
        userService.deleteUserById(id);
        return ResponseEntity.ok("User successfully deleted");
    }
}