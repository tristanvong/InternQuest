package be.ehb.tristan.javaadvanced.internquest.controllers;

import be.ehb.tristan.javaadvanced.internquest.exceptions.ActivityNotFoundByIdException;
import be.ehb.tristan.javaadvanced.internquest.exceptions.UnauthorizedAccessException;
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
            throw new UnauthorizedAccessException("You are not authorized to use this page.");
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
    public String createAndAssignActivityToUser(@PathVariable Long userId, @RequestParam Long companyId, @ModelAttribute Activity activity, Model model, Authentication authentication) {
        String username = authentication.getName();
        User userUsingURL = userService.getUserByUsername(username);

        if(userUsingURL == null || !userUsingURL.getId().equals(userId)) {
            throw new UnauthorizedAccessException("You are not authorized to use this page.");
        }

        User user = userService.getUserById(userId);
        if(user == null) {
            throw new UnauthorizedAccessException("User with id " + userId + " not found.");
        }

        Company company = companyService.getCompanyById(companyId);
        if(company == null) {
            throw new UnauthorizedAccessException("Company with id: " + companyId + " not found.");
        }

        activity.getCompanies().add(company);
        activityService.saveActivity(activity);

        if(userUsingURL.getActivities() == null) {
            userUsingURL.setActivities(new HashSet<>());
        }

        userUsingURL.getActivities().add(activity);

        if(activity.getUsers() == null) {
            activity.setUsers(new HashSet<>());
        }

        activity.getUsers().add(userUsingURL);
        userService.updateUser(userUsingURL);
        activityService.saveActivity(activity);
        model.addAttribute("user", userUsingURL);
        return "redirect:/user/info";
    }

    @GetMapping("/update/{activityId}")
    public String showUpdateActivityForm(@PathVariable Long activityId, Model model, Authentication authentication) {
        String username = authentication.getName();
        User userUsingURL = userService.getUserByUsername(username);
        if(userUsingURL == null) {
            throw new UnauthorizedAccessException("You are not authorized to use this page.");
        }

        Activity activity = activityService.getActivityById(activityId);
        if(activity == null) {
            throw new ActivityNotFoundByIdException("Activity with id: " + activityId + " not found");
        }

        boolean isUserAssociatedWithActivity = userUsingURL.getActivities().stream()
                .anyMatch(associatedActivity -> associatedActivity.getId().equals(activityId));

        if(!isUserAssociatedWithActivity) {
            throw new UnauthorizedAccessException("You are not authorized to use this page. Not associated with activity");
        }

        model.addAttribute("userId", userUsingURL.getId());
        model.addAttribute("activity", activity);
        return "activity/update-activity-form";
    }

    @PostMapping("/update/{activityId}")
    public String updateActivity(@PathVariable Long activityId, @RequestParam String activityName, @RequestParam String updatedStatus, Model model, Authentication authentication) {
        String username = authentication.getName();
        User userUsingURL = userService.getUserByUsername(username);
        if(userUsingURL == null) {
            throw new UnauthorizedAccessException("You are not authorized to use this page.");
        }
        Activity activity = activityService.getActivityById(activityId);
        if(activity == null) {
            throw new ActivityNotFoundByIdException("Activity with id: " + activityId + " not found");
        }

        activity.setActivityName(activityName);
        activity.setActivityDescription(updatedStatus);

        activityService.saveActivity(activity);
        model.addAttribute("user", userUsingURL);
        return "redirect:/user/info";
    }

    @GetMapping("/list")
    public String listUserActivities(Model model, Authentication authentication) {
        String username = authentication.getName();
        User userUsingURL = userService.getUserByUsername(username);
        if(userUsingURL == null) {
            throw new UnauthorizedAccessException("You are not authorized to access this page.");
        }
        model.addAttribute("activities", userUsingURL.getActivities());
        return "activity/list-activities";
    }

    @GetMapping("/delete/{activityId}")
    public String deleteActivity(@PathVariable Long activityId, Authentication authentication){
        Activity activity = activityService.getActivityById(activityId);

        if(activity == null) {
            throw new ActivityNotFoundByIdException("Activity with id: " + activityId + " not found");
        }

        String username = authentication.getName();
        boolean isAuthorized = activity.getUsers().stream()
                .anyMatch(user -> user.getUsername().equals(username));

        if(!isAuthorized) {
            throw new UnauthorizedAccessException("You are not authorized to delete this activity.");
        }

        activityService.deleteActivityById(activityId);
        return "redirect:/user/info";
    }
}