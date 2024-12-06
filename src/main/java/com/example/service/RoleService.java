package com.example.service;

import com.example.model.Role;
import com.example.repository.RoleRepository; // Assuming you have a repository for Role
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Role findByRole(String roleName) {
        return roleRepository.findByRole(roleName);
    }

    public void save(Role role) {
        roleRepository.save(role);
    }
}
