package com.example.controller;

import com.example.model.UserDemo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.example.service.UserService;

import java.util.List;
import java.util.logging.Logger;

@Controller()
public class UserController {
    private final UserService userService;
    private static final Logger logger = Logger.getLogger(UserController.class.getName());

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public String trangChiTiet(@ModelAttribute("userName") String userName, Model model) {
        List<UserDemo> list = userService.getAllUser();
        model.addAttribute("userDemo", list);
        return "user";
    }

    @GetMapping("/addUser")
    public String addUser(Model model) {
        model.addAttribute("user", new UserDemo());
        return "addUser";
    }

    @PostMapping("/addUser")
    public String saveUser(@ModelAttribute("user") UserDemo user, RedirectAttributes redirectAttributes) {
        String fullName = user.getFirstName() + " " + user.getLastName();
        redirectAttributes.addFlashAttribute("userName", fullName);
        userService.saveOrUpdate(user);
        return "redirect:/user";
    }
}