package be.ehb.tristan.javaadvanced.internquest.controllers;

import be.ehb.tristan.javaadvanced.internquest.models.Activity;
import be.ehb.tristan.javaadvanced.internquest.models.Company;
import be.ehb.tristan.javaadvanced.internquest.models.User;
import be.ehb.tristan.javaadvanced.internquest.services.ActivityService;
import be.ehb.tristan.javaadvanced.internquest.services.CompanyService;
import be.ehb.tristan.javaadvanced.internquest.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/activities")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @Autowired
    private UserService userService;

    @Autowired
    private CompanyService companyService;

    @GetMapping("/create")
    public String showAssignActivityForm(Model model, Authentication authentication) {
        String username = authentication.getName();
        User userUsingURL = userService.getUserByUsername(username);
        if(userUsingURL == null) {
            throw new RuntimeException("You are not authorized to use this page.");
        }

        Set<Company> availableCompanies = userUsingURL.getCompanies()
                .stream()
                .filter(company -> company.getActivities() == null || company.getActivities().isEmpty())
                .collect(Collectors.toSet());

        model.addAttribute("userId", userUsingURL.getId());
        model.addAttribute("activity", new Activity());
        model.addAttribute("userCompanies", availableCompanies);
        return "activity/create-activity-form";
    }

    @PostMapping("/create/{userId}")
    public String createAndAssignActivity(@PathVariable Long userId,@RequestParam Long companyId ,@ModelAttribute Activity activity, Model model, Authentication authentication) {
        String username = authentication.getName();
        User userUsingURL = userService.getUserByUsername(username);

        if(userUsingURL == null || !userUsingURL.getId().equals(userId)) {
            throw new RuntimeException("User with id: " + userId + " not authorized to use this page.");
        }

        User user = userService.getUserById(userId);

        if(user == null) {
            throw new RuntimeException("User with id: " + userId + " not found");
        }

        Company company = companyService.getCompanyById(companyId);

        if(company == null) {
            throw new RuntimeException("Company with id: " + companyId + " not found");
        }

        activity.getCompanies().add(company);
        activityService.saveActivity(activity);

        if(user.getActivities() == null) {
            user.setActivities(new HashSet<>());
        }

        user.getActivities().add(activity);

        if(activity.getUsers() == null) {
            activity.setUsers(new HashSet<>());
        }

        activity.getUsers().add(user);

        userService.updateUser(user);
        activityService.saveActivity(activity);

        model.addAttribute("user", user);
        return "redirect:/user/info";
    }

    @GetMapping("/update/{userId}")
    public String showUpdateActivityForm(@PathVariable Long userId, @RequestParam Long activityId, Model model, Authentication authentication) {
        String username = authentication.getName();
        User userUsingURL = userService.getUserByUsername(username);

        if (userUsingURL == null || !userUsingURL.getId().equals(userId)) {
            throw new RuntimeException("User with id: " + userId + " is not authorized to access this page.");
        }

        User user = userService.getUserById(userId);
        if (user == null) {
            throw new RuntimeException("User with id: " + userId + " not found");
        }

        Activity activity = activityService.getActivityById(activityId);
        if (activity == null) {
            throw new RuntimeException("Activity with id: " + activityId + " not found");
        }

        boolean isAssociated = user.getActivities().stream()
                .anyMatch(associatedActivity -> associatedActivity.getId().equals(activityId));

        if (!isAssociated) {
            throw new RuntimeException("Activity with id: " + activityId + " is not associated with user: " + user.getUsername());
        }

        model.addAttribute("userId", userId);
        model.addAttribute("activity", activity);
        return "activity/update-activity-form";
    }

    @PostMapping("/update/{userId}")
    public String updateActivity(@PathVariable Long userId, @RequestParam Long activityId,@RequestParam String activityName ,@RequestParam String updatedStatus, Model model, Authentication authentication) {
        String username = authentication.getName();
        User userUsingURL = userService.getUserByUsername(username);

        if(userUsingURL == null || !userUsingURL.getId().equals(userId)) {
            throw new RuntimeException("User with id: " + userId + " is not authorized to access this page.");
        }

        User user = userService.getUserById(userId);

        if(user == null) {
            throw new RuntimeException("User with id: " + userId + " not found");
        }

        Activity activity = activityService.getActivityById(activityId);

        if(activity == null) {
            throw new RuntimeException("Activity with id: " + activityId + " not found");
        }

        activity.setActivityName(activityName);
        activity.setActivityDescription(updatedStatus);

        activityService.saveActivity(activity);
        model.addAttribute("user", user);
        return "redirect:/user/info";
    }

    @GetMapping("/list")
    public String listUserActivities(Model model, Authentication authentication) {
        String username = authentication.getName();
        User userUsingURL = userService.getUserByUsername(username);
        if(userUsingURL == null) {
            throw new RuntimeException("You are not authorized to access this page.");
        }
        model.addAttribute("activities", userUsingURL.getActivities());
        return "activity/list-activities";
    }
}