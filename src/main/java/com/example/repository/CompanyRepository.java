package com.example.repository;

import com.example.model.Company;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CompanyRepository extends CrudRepository<Company, Integer> {
    List<Company> findAll();
}
