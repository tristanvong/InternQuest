package be.ehb.tristan.javaadvanced.internquest.basicsremovelater;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserViewController {

    @Autowired
    private UserService userService;

    @PostMapping("/createuser")
    public String createUser(@ModelAttribute User user) {
        userService.addUser(user);
        return "home";
    }

    @GetMapping("/createuser")
    public String creatUser(Model model) {
        model.addAttribute("user", new User());
        return "create-user";
    }
}
