package be.ehb.tristan.javaadvanced.internquest.controllers;

import be.ehb.tristan.javaadvanced.internquest.enums.AchievementEnum;
import be.ehb.tristan.javaadvanced.internquest.enums.Rarity;
import be.ehb.tristan.javaadvanced.internquest.models.Company;
import be.ehb.tristan.javaadvanced.internquest.models.User;
import be.ehb.tristan.javaadvanced.internquest.services.AchievementService;
import be.ehb.tristan.javaadvanced.internquest.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Set;

@Controller
@RequestMapping("/achievements")
public class AchievementController {

    @Autowired
    private UserService userService;

    @Autowired
    private AchievementService achievementService;

    @GetMapping("/check/made-account/{userId}")
    public String checkAchievements(@PathVariable Long userId, Authentication authentication) {
        String username = authentication.getName();
        User userUsingURL = userService.getUserByUsername(username);

        if(userUsingURL == null || !userUsingURL.getId().equals(userId)) {
            throw new RuntimeException("You are not authorized to do that action");
        }

        User user = userService.getUserById(userId);
        achievementService.checkAndAssignAchievement(user, AchievementEnum.MADE_AN_ACCOUNT, Rarity.EASY);

        return "redirect:/info";
    }

    @GetMapping("/check/created-company/{userId}")
    public String checkCreatedCompanyAchievement(@PathVariable Long userId, Authentication authentication) {
        String username = authentication.getName();
        User userUsingURL = userService.getUserByUsername(username);

        if(userUsingURL == null || !userUsingURL.getId().equals(userId)) {
            throw new RuntimeException("You are not authorized to do that action");
        }

        User user = userService.getUserById(userId);
        Set<Company> userCompanies = user.getCompanies();

        if(!userCompanies.isEmpty()) {
            achievementService.checkAndAssignAchievement(user, AchievementEnum.CREATED_A_COMPANY, Rarity.EASY);
        }
        return "redirect:/info";
    }

    @GetMapping("/list/{userId}")
    public String listAchievements(@PathVariable Long userId, Model model , Authentication authentication) {
        String username = authentication.getName();
        User userUsingURL = userService.getUserByUsername(username);
        if(userUsingURL == null || !userUsingURL.getId().equals(userId)) {
            throw new RuntimeException("You are not authorized to access this page.");
        }
        User user = userService.getUserById(userId);
        if(user == null) {
            throw new RuntimeException("User with id " + userId + " not found.");
        }
        model.addAttribute("achievements", user.getAchievements());
        return "achievement/list-achievements";
    }
}
