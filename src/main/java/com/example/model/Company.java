package com.example.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.List;
@Entity
@Table(name = "company")
public class Company {
    @Id()
    @Column(name = "company_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column
    private String companyName;

    @OneToMany(mappedBy = "company", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonManagedReference
    private List<UserDemo> users;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public List<UserDemo> getUsers() {
        return users;
    }

    public void setUsers(List<UserDemo> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "Company{" +
                "id=" + id +
                ", companyName='" + companyName + '\'' +
                ", users=" + users +
                '}';
    }
}
