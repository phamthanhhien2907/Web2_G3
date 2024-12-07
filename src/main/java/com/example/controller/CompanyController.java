package com.example.controller;

import com.example.model.Company;
import com.example.model.UserDemo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.service.CompanyService;

import java.util.List;

@RestController()
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("/company")
    public String trangChiTiet(@ModelAttribute("userName") String userName, Model model) {
        List<Company> list = companyService.getAllCompanies();
        model.addAttribute("company", list);
        return "company";
    }

    @GetMapping("/addCompany")
    public String addCompany(Model model) {
        model.addAttribute("company", new Company());
        return "addCompany";
    }

    @PostMapping("addCompany")
    public String addCompany(@ModelAttribute("company") Company company) {
        companyService.saveOrUpdate(company);
        return "company";
    }

    // Hiển thị form cập nhật
    @GetMapping("/updateCompany/{id}")
    public String showUpdateForm(@PathVariable int id, Model model) {
        Company company = companyService.getCompanyById(id);
        model.addAttribute("company", company);
        return "updateCompany";
    }

    // Thực hiện cập nhật
    @PostMapping("/updateCompany/{id}")
    public String updateCompany(@PathVariable int id, @ModelAttribute("company") Company company) {
        company.setId(id); // Đảm bảo rằng ID được cập nhật
        companyService.saveOrUpdate(company);
        return "redirect:/company";
    }

    @GetMapping("/deleteCompany/{id}")
    public String deleteCompany(@PathVariable int id) {
        companyService.deleteCompanyById(id);
        return "redirect:/company";
    }
    @GetMapping("/api/company")
    public List<Company> companies() {
        List<Company> companies = companyService.getAllCompanies();
        return companies;
    }
    @PostMapping("/api/addNewCompany")
    @ResponseBody
    public ResponseEntity<Company> saveUser(@RequestBody Company company) {
        companyService.saveOrUpdate(company);
        return ResponseEntity.ok(company);
    }
    @PutMapping("/api/updateCompany/{id}")
    @ResponseBody
    public ResponseEntity<String> update(@PathVariable int id, @RequestBody Company company) {
        Company companyExisting = companyService.getCompanyById(id);
        if(companyExisting == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id not found");
        }
        companyExisting.setCompanyName(company.getCompanyName());
        companyService.saveOrUpdate(companyExisting);
        return ResponseEntity.ok("User with ID " + id + " successfully updated");
    }
    @DeleteMapping("/api/deleteCompany/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        Company company = companyService.getCompanyById(id);
        if(company == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id not found");
        }
        companyService.deleteCompanyById(id);
        return ResponseEntity.ok("User successfully deleted");
    }
}
