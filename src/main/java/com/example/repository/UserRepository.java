package com.example.repository;

import com.example.model.UserDemo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserDemo, Integer>
{
    Optional<UserDemo> findByEmail(String email);

}