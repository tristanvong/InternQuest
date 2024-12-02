package be.ehb.tristan.javaadvanced.internquest.controllers.user;

import be.ehb.tristan.javaadvanced.internquest.exceptions.FormValueIncorrectException;
import be.ehb.tristan.javaadvanced.internquest.exceptions.UserNotFoundByIdGivenException;
import be.ehb.tristan.javaadvanced.internquest.exceptions.UserNotFoundByUsernameGivenException;
import be.ehb.tristan.javaadvanced.internquest.models.User;
import be.ehb.tristan.javaadvanced.internquest.services.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String login(Model model) {
        return "user/login-form";
    }

    @GetMapping("/loggedin")
    public String loggedIn() {
        return "user/loggedin";
    }

    @GetMapping("users")
    public String getAllUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        if (users.isEmpty()) {
            throw new RuntimeException("No users found");
        }
        return "user/user-list";
    }

    @GetMapping("new/users/id/{id}")
    public String getUserById(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id);
        if (user == null) {
            throw new UserNotFoundByIdGivenException("User with id " + id + " not found");
        }
        model.addAttribute("user", user);
        return "user/user";
    }

    @GetMapping("new/users/username/{username}")
    public String getUserByUsername(@PathVariable String username, Model model) {
        User user = userService.getUserByUsername(username);
        if (user == null) {
            throw new UserNotFoundByUsernameGivenException("User with id " + username + " not found");
        }
        model.addAttribute("user", user);
        return "user/user";
    }
//    @GetMapping("/users/id/{id}")
//    public User getUserById(@PathVariable Long id) {
//        return userService.getUserById(id);
//    }

    @GetMapping("/create-user")
    public String createUser(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("isCreate", true);
        return "user/create-user-form";
    }

    @PostMapping("/create-user")
    public String createUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "user/create-user-form";
        }
        if(user.getAddress().getPostalCode() > 9999){
            throw new FormValueIncorrectException("Postal code is too big");
        }
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        userService.addUser(user, encodedPassword);
        return "basicslearning/home";
    }

//    public String createUser(@ModelAttribute @Valid UserOld user, BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) {
//            return "basicslearning/create-user";
//        }
//        userServiceOld.addUser(user);
//        return "basicslearning/home";
//    }

    @GetMapping("/test")
    public String test() {
        throw new NullPointerException("This is a test");
//        return "basicslearning/home";
    }
}