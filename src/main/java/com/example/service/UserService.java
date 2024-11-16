package com.example.service;

import com.example.model.UserDemo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.repository.UserRepository;

import java.util.List;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public void saveOrUpdate(UserDemo user) {
        userRepository.save(user);
    }

    public List<UserDemo> getAllUser() {
        return userRepository.findAll();
    }
}