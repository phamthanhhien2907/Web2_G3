package com.example.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "user_demo")
public class UserDemo {
    @Id()
    @Column()
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column()
    private String firstName;

    @Column()
    private String lastName;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = true, referencedColumnName = "company_id")
    @JsonBackReference
    private Company company;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @Override
    public String toString() {
        return "UserDemo{" +
                "lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                '}';
    }
}
