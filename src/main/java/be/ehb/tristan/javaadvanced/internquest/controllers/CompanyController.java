package be.ehb.tristan.javaadvanced.internquest.controllers;

import be.ehb.tristan.javaadvanced.internquest.enums.Industry;
import be.ehb.tristan.javaadvanced.internquest.models.Company;
import be.ehb.tristan.javaadvanced.internquest.models.User;
import be.ehb.tristan.javaadvanced.internquest.services.CompanyService;
import be.ehb.tristan.javaadvanced.internquest.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;

@Controller
@RequestMapping("/companies")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private UserService userService;

    @GetMapping("/create")
    public String createCompanyForm(Model model, Authentication authentication) {
        String username = authentication.getName();
        User userUsingURL = userService.getUserByUsername(username);
        if (userUsingURL == null) {
            throw new RuntimeException("You are not authorized to access this page.");
        }
        model.addAttribute("userId", userUsingURL.getId());
        model.addAttribute("company", new Company());
        model.addAttribute("industries", Industry.values());
        return "company/create-company-form";
    }

    @PostMapping("/create/{userId}")
    public String createCompany(@PathVariable Long userId, @ModelAttribute Company company, @RequestParam("industry") Industry industry, Model model, Authentication authentication) {
        String username = authentication.getName();
        User userUsingURL = userService.getUserByUsername(username);
        if (userUsingURL == null || !userUsingURL.getId().equals(userId)) {
            throw new RuntimeException("You are not authorized to access this page.");
        }
        User user = userService.getUserById(userId);

        if (user == null) {
            throw new IllegalArgumentException("User with id " + userId + " not found");
        }

        companyService.validateCompanyInformation(company, industry, user);
        userService.updateUser(user);
        model.addAttribute("user", user);
        return "redirect:/user/info";
    }

    @GetMapping("/edit/{companyId}")
    public String editCompanyForm(@PathVariable Long companyId, Model model, Authentication authentication) {
        Company company = companyService.getCompanyById(companyId);
        String username = authentication.getName();

        boolean isAuthorized = company.getUsers().stream()
                .anyMatch(user -> user.getUsername().equals(username));

        if (!isAuthorized) {
            throw new RuntimeException("You are not authorized to access this page.");
        }

        model.addAttribute("company", company);
        model.addAttribute("industries", Industry.values());
        return "company/edit-company-form";
    }

    @PostMapping("/edit/{companyId}")
    public String changeCompanyInformation(@PathVariable Long companyId, @ModelAttribute Company changedCompany,@RequestParam("industry") Industry industry ,Model model, Authentication authentication) {
        Company company = companyService.getCompanyById(companyId);
        String username = authentication.getName();

        boolean isAuthorized = company.getUsers().stream()
                .anyMatch(user -> user.getUsername().equals(username));

        if (!isAuthorized) {
            throw new RuntimeException("You are not authorized to access this page.");
        }

        companyService.validateCompanyInformation(changedCompany, company, industry);
        return "redirect:/user/info";
    }

    @GetMapping("/list")
    public String listUserCompanies(Model model, Authentication authentication) {
        String username = authentication.getName();
        User userUsingURL = userService.getUserByUsername(username);
        if (userUsingURL == null) {
            throw new RuntimeException("You are not authorized to access this page.");
        }
        Long userId = userUsingURL.getId();

        model.addAttribute("userId", userId);
        model.addAttribute("companies", userUsingURL.getCompanies());
        return "company/list-companies";
    }

    @GetMapping("/delete/{companyId}")
    public String deleteCompany(@PathVariable Long companyId, Authentication authentication) {
        Company company = companyService.getCompanyById(companyId);
        if(company == null) {
            throw new RuntimeException("Company with id " + companyId + " not found");
        }

        String username = authentication.getName();
        boolean userIsAuthorizeToDeleteCompany = company.getUsers().stream()
                .anyMatch(user -> user.getUsername().equals(username));

        if(!userIsAuthorizeToDeleteCompany) {
            throw new RuntimeException("You are not authorized to perform this action.");
        }

        companyService.deleteCompanyById(companyId);
        return "redirect:/user/info";
    }
}