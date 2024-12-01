package be.ehb.tristan.javaadvanced.internquest.controllers.user;

import be.ehb.tristan.javaadvanced.internquest.basicsremovelater.UserOld;
import be.ehb.tristan.javaadvanced.internquest.models.User;
import be.ehb.tristan.javaadvanced.internquest.services.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

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
        userService.addUser(user);
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
        return "basicslearning/home";
    }
}