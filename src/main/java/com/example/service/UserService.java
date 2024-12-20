package com.example.service;

import com.example.model.Company;
import com.example.model.User;
import com.example.model.UserDemo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.List;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    public UserDemo getUserById(int id) {
        return userRepository.findById(id).orElse(null);
    }
    public UserDemo findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
    public void deleteUserById(int id) {
        UserDemo user = userRepository.findById(id).orElse(null);
        userRepository.deleteById(id);
    }
    public void saveOrUpdate(UserDemo user) {
         userRepository.save(user);
    }
    public UserDemo getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        System.out.println("Authentication: " + authentication);

        if (authentication == null) {
            System.out.println("Không có user nào đăng nhập.");
            return null;
        }

        if (!authentication.isAuthenticated()) {
            System.out.println("User không được xác thực.");
            return null;
        }

        String principal = authentication.getPrincipal().toString();
        System.out.println("Principal: " + principal);

        String email = authentication.getName(); // Lấy email hoặc username
        System.out.println("Email/User: " + email);

        return findByEmail(email);
    }

    public List<UserDemo> getAllUser() {
        return userRepository.findAll();
    }

}