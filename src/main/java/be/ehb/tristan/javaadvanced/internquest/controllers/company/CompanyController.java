package be.ehb.tristan.javaadvanced.internquest.controllers.company;

import be.ehb.tristan.javaadvanced.internquest.enums.Industry;
import be.ehb.tristan.javaadvanced.internquest.models.Company;
import be.ehb.tristan.javaadvanced.internquest.models.User;
import be.ehb.tristan.javaadvanced.internquest.services.company.CompanyService;
import be.ehb.tristan.javaadvanced.internquest.services.user.UserService;
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

    @GetMapping("/create/{userId}")
    public String createCompanyForm(@PathVariable Long userId, Model model, Authentication authentication) {
        String username = authentication.getName();
        User userUsingURL = userService.getUserByUsername(username);
        if (userUsingURL == null || !userUsingURL.getId().equals(userId)) {
            throw new RuntimeException("You are not authorized to access this page.");
        }
        User user = userService.getUserById(userId);

        if (user == null) {
            throw new IllegalArgumentException("User with id " + userId + " not found");
        }
        model.addAttribute("userId", userId);
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

        if (company.getAddress() == null || company.getAddress().getStreetName() == null || company.getAddress().getCity() == null) {
            throw new IllegalArgumentException("Address or street name or city not found");
        }

        company.setIndustry(industry);

        if (user.getCompanies() == null) {
            user.setCompanies(new HashSet<>());
        }

        user.getCompanies().add(company);

        if (company.getUsers() == null) {
            company.setUsers(new HashSet<>());
        }

        company.getUsers().add(user);

        companyService.saveCompany(company);
        userService.updateUser(user);
        model.addAttribute("user", user);
        return "redirect:/info";
    }



//    @PostMapping("/create/{userId}")
//    public String createCompany(@PathVariable Long userId, @ModelAttribute Company company, @ModelAttribute Address address, @RequestParam("industry") Industry industry) {
//        User user = userService.getUserById(userId);
//
//        if(user == null) {
//            throw new IllegalArgumentException("User with id " + userId + " not found");
//        }
//        company.setAddress(address);
//        company.setIndustry(industry);
//
//        companyService.saveCompany(company);
//        user.getCompanies().add(company);
//        userService.updateUser(user);
//
//        return "redirect:/users/" + userId;
//    }
}
