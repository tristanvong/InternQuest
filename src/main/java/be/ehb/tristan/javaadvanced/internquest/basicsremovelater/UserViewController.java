package be.ehb.tristan.javaadvanced.internquest.basicsremovelater;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserViewController {

    @Autowired
    private UserService userService;

    @PostMapping("/basic/createuser")
//    public String createUser(@ModelAttribute User user) {
//        userService.addUser(user);
//        return "home";
//    }
    public String createUser(@ModelAttribute @Valid UserOld user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "basicslearning/create-user";
        }
        userService.addUser(user);
        return "basicslearning/home";
    }

    @GetMapping("/basic/createuser")
    public String creatUser(Model model) {
        model.addAttribute("user", new UserOld());
        return "basicslearning/create-user";
    }

    @GetMapping("/basic/home")
    public String home() {
        return "basicslearning/home";
    }
}
