package com.example.controller;

import com.example.model.Company;
import com.example.model.Role;
import com.example.model.User;
import com.example.model.UserDemo;
import com.example.repository.UserRepository;
import com.example.security.JwtService;
import com.example.service.CompanyService;
import com.example.service.RoleService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.example.service.UserService;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RestController()
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    private final UserService userService;
    private final CompanyService companyService;
    private final RoleService roleService;
    private final UserRepository userRepository;
    private final ProviderManager authenticationManager;
    private final JwtService jwtService;

    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    private static final Logger logger = Logger.getLogger(UserController.class.getName());

    public UserController(UserService userService, CompanyService companyService, RoleService roleService, UserRepository userRepository, ProviderManager authenticationManager, JwtService jwtService) {
        this.userService = userService;
        this.companyService = companyService;
        this.roleService = roleService;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @GetMapping("/user")
    public String trangChiTiet(@ModelAttribute("userName") String userName, HttpSession session, Model model) {
        List<UserDemo> list = userService.getAllUser();
        model.addAttribute("userDemo", list);
        return "user";
//        Integer currentUserId = (Integer) session.getAttribute("currentUserId");
//
//        if (currentUserId != null) {
//            List<UserDemo> allUsers = userService.getAllUser();
//
//            List<UserDemo> filteredUsers = allUsers.stream()
//                    .filter(user -> !user.getId().equals(currentUserId))
//                    .collect(Collectors.toList());
//            model.addAttribute("userDemo", filteredUsers);
//        } else {
//            model.addAttribute("message", "No user logged in");
//        }
//
//        return "user";
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

    @GetMapping("/api/get-current")
    public ResponseEntity<?> getCurrentUser() {
        // Lấy thông tin Authentication từ SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() ||
                authentication.getPrincipal().equals("anonymousUser")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
        }

        // Lấy thông tin email của người dùng
        String email = authentication.getName();
        System.out.println("Current User Email: " + email);

        // Lấy thông tin chi tiết của user từ database
        UserDemo user = userService.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        // Trả về thông tin user
        return ResponseEntity.ok(user);
    }

    @GetMapping("/api/users")
    public List<UserDemo> getUserDetail() {
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
        user.setCompany(user.getCompany());
        Role userRole = roleService.findByRole("ADMIN"); // Find the "ROLE_USER"
        if (userRole == null) {
            userRole = new Role();
            userRole.setRole("ADMIN");  // Create "ROLE_USER" if it doesn't exist
            roleService.save(userRole);  // Save the role if it's new
        }

        user.setRole(Set.of(userRole));
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