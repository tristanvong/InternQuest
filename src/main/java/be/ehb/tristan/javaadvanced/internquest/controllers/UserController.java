package be.ehb.tristan.javaadvanced.internquest.controllers;

import be.ehb.tristan.javaadvanced.internquest.enums.Role;
import be.ehb.tristan.javaadvanced.internquest.exceptions.FormValueIncorrectException;
import be.ehb.tristan.javaadvanced.internquest.exceptions.UnauthorizedAccessException;
import be.ehb.tristan.javaadvanced.internquest.exceptions.UserNotFoundException;
import be.ehb.tristan.javaadvanced.internquest.models.User;
import be.ehb.tristan.javaadvanced.internquest.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
//for checking if admin/regular user
//    @GetMapping("/test")
//    public String test(Authentication authentication, Model model) {
//        authentication.getAuthorities().forEach(authority -> {
//            System.out.println(authority);
//                }
//        );
//        return "redirect:/user/info";
//    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "user/login-form";
    }

    @GetMapping("/create-user")
    public String createUser(Model model, Authentication authentication) {
        if(authentication != null && authentication.isAuthenticated()){
            throw new UnauthorizedAccessException("You cannot access this page while logged in");
        }
        model.addAttribute("user", new User());
        model.addAttribute("isCreate", true);
        return "user/create-user-form";
    }

    @PostMapping("/create-user")
    public String createUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, Authentication authentication) {
        if(authentication != null && authentication.isAuthenticated()) {
            throw new UnauthorizedAccessException("You cannot perform this action while logged in");
        }
        if (bindingResult.hasErrors()) {
            return "user/create-user-form";
        }

        userService.validateUserAddress(user);
        userService.addUser(user);
        return "user/login-form";
    }

    @GetMapping("/info")
    public String getUserInfo(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        model.addAttribute("username", username);
        return "user/information";
    }

    @GetMapping("/edit-account")
    public String showAccountEditPage(Model model, Authentication authentication) {
        String username = authentication.getName();
        User user = userService.getUserByUsername(username);

        if(user == null) {
            throw new UserNotFoundException("User not found");
        }

        model.addAttribute("user", user);
        return "user/account-edit-form";
    }

    @PostMapping("/edit-account")
    public String updateAccountInformation(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, Authentication authentication) {
        if(bindingResult.hasErrors()) {
            return "user/account-edit-form";
        }
        String username = authentication.getName();
        User userUsingURL = userService.getUserByUsername(username);
        if(userUsingURL == null){
            throw new UserNotFoundException("User not found");
        }
        user.setId(userUsingURL.getId());
        user.setRole(Role.REGULAR_USER);
        userService.editUser(user);
        return "redirect:/user/info";
    }

    @GetMapping("/delete")
    public String deleteUser(Authentication authentication) {
        String username = authentication.getName();
        User userUsingURL = userService.getUserByUsername(username);
        if(userUsingURL == null) {
            throw new UnauthorizedAccessException("You do not have access to this action!");
        }
        try {
            Long id = userUsingURL.getId();
            userService.deleteUser(id);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
        return "redirect:/user/login";
    }
    @GetMapping("user-details")
    public String showUserDetails(Model model, Authentication authentication) {
        String username = authentication.getName();
        User user = userService.getUserByUsername(username);
        if(user == null) {
            throw new UnauthorizedAccessException("You are not authorized to access this page!");
        }

        model.addAttribute("user", user);
        return "user/user";
    }
}