package be.ehb.tristan.javaadvanced.internquest.basicsremovelater;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/basic/users/id/{id}")
    public UserOld getUser(@PathVariable Long id){
        return userService.getUserById(id);
    }

    @GetMapping("/basic/users/username/{username}")
    public ModelAndView getUserByUsername(@PathVariable String username) {
        try {
            UserOld user = userService.getUserByUsername(username);
            return new ModelAndView("basicslearning/home");
        } catch (RuntimeException e) {
            ModelAndView modelAndView = new ModelAndView("basicslearning/errorUsername");
            modelAndView.addObject("username", username);
            modelAndView.addObject("message", e.getMessage());
            return modelAndView;
        }
    }
//    public User getUserByUsername(@PathVariable String username){
//        return userService.getUserByUsername(username);
//    }

    @GetMapping("/basic/users")
    public List<UserOld> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/basic/users/partial/{partialUsername}")
    public List<UserOld> getUsersByPartialUsername(@PathVariable String partialUsername) {
        return userService.getUsersByPartialUsername(partialUsername);
    }
}
