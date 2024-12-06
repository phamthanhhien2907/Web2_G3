package com.example.service;

import com.example.model.Company;
import com.example.model.User;
import com.example.model.UserDemo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.repository.UserRepository;

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

    public List<UserDemo> getAllUser() {
        return userRepository.findAll();
    }

}