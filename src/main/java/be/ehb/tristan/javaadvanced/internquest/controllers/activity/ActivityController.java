package be.ehb.tristan.javaadvanced.internquest.controllers.activity;

import be.ehb.tristan.javaadvanced.internquest.models.Activity;
import be.ehb.tristan.javaadvanced.internquest.models.User;
import be.ehb.tristan.javaadvanced.internquest.services.activity.ActivityService;
import be.ehb.tristan.javaadvanced.internquest.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.HashSet;

@Controller
@RequestMapping("/activities")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @Autowired
    private UserService userService;

    @GetMapping("/create/{userId}")
    public String showAssignActivityForm(@PathVariable Long userId, Model model, Authentication authentication) {
        String username = authentication.getName();
        User userUsingURL = userService.getUserByUsername(username);
        if(userUsingURL == null || !userUsingURL.getId().equals(userId)) {
            throw new RuntimeException("User with id: " + userId + " not found");
        }
        User user = userService.getUserById(userId);
        if(user == null) {
            throw new RuntimeException("User with id: " + userId + " not found");
        }
        model.addAttribute("userId", userId);
        model.addAttribute("activity", new Activity());
        return "activity/create-activity-form";
    }

    @PostMapping("/create/{userId}")
    public String createAndAssignActivity(@PathVariable Long userId, @ModelAttribute Activity activity, Model model, Authentication authentication) {
        String username = authentication.getName();
        User userUsingURL = userService.getUserByUsername(username);
        if(userUsingURL == null || !userUsingURL.getId().equals(userId)) {
            throw new RuntimeException("User with id: " + userId + " not found");
        }
        User user = userService.getUserById(userId);
        if(user == null) {
            throw new RuntimeException("User with id: " + userId + " not found");
        }
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
        return "redirect:/info";
    }
}