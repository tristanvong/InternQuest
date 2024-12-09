package be.ehb.tristan.javaadvanced.internquest.controllers;

import be.ehb.tristan.javaadvanced.internquest.enums.AchievementEnum;
import be.ehb.tristan.javaadvanced.internquest.enums.Rarity;
import be.ehb.tristan.javaadvanced.internquest.exceptions.RequirementsNotMetException;
import be.ehb.tristan.javaadvanced.internquest.exceptions.UnauthorizedAccessException;
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

    @GetMapping("/check/made-account")
    public String checkMadeAnAccountAchievement(Authentication authentication) {
        String username = authentication.getName();
        User userUsingURL = userService.getUserByUsername(username);

        if(userUsingURL == null) {
            throw new UnauthorizedAccessException("You are not authorized to do that action");
        }

        User user = userService.getUserById(userUsingURL.getId());
        achievementService.checkAndAssignAchievement(user, AchievementEnum.MADE_AN_ACCOUNT, Rarity.EASY);

        return "redirect:/user/info";
    }

    @GetMapping("/check/created-company")
    public String checkCreatedCompanyAchievement(Authentication authentication) {
        String username = authentication.getName();
        User userUsingURL = userService.getUserByUsername(username);

        if(userUsingURL == null) {
            throw new UnauthorizedAccessException("You are not authorized to do that action");
        }

        User user = userService.getUserById(userUsingURL.getId());
        Set<Company> userCompanies = user.getCompanies();

        if(!userCompanies.isEmpty()) {
            achievementService.checkAndAssignAchievement(user, AchievementEnum.CREATED_A_COMPANY, Rarity.EASY);
        }else {
            throw new RequirementsNotMetException("You do not meet the requirements to earn this achievement");
        }
        return "redirect:/user/info";
    }

    @GetMapping("/check")
    public String checkAchievements(Authentication authentication) {
        String username = authentication.getName();
        User userUsingURL = userService.getUserByUsername(username);

        if (userUsingURL == null) {
            throw new UnauthorizedAccessException("You are not authorized to do that action");
        }

        User user = userService.getUserById(userUsingURL.getId());

        try {
            achievementService.checkAndAssignAchievement(user, AchievementEnum.MADE_AN_ACCOUNT, Rarity.EASY);
        } catch (Exception e) {
            System.err.println("Error assigning MADE_AN_ACCOUNT: " + e.getMessage());
        }

        try {
            achievementService.checkAndAssignAchievement(user, AchievementEnum.CREATED_A_COMPANY, Rarity.EASY);
        } catch (Exception e) {
            System.err.println("Error assigning CREATED_A_COMPANY: " + e.getMessage());
        }

        return "redirect:/achievements/list";
    }

    @GetMapping("/list")
    public String listAchievements(Model model , Authentication authentication) {
        String username = authentication.getName();
        User userUsingURL = userService.getUserByUsername(username);
        if(userUsingURL == null) {
            throw new UnauthorizedAccessException("You are not authorized to access this page.");
        }
        model.addAttribute("achievements", userUsingURL.getAchievements());
        return "achievement/list-achievements";
    }
}