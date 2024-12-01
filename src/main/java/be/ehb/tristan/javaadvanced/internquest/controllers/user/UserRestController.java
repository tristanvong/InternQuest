package be.ehb.tristan.javaadvanced.internquest.controllers.user;

import be.ehb.tristan.javaadvanced.internquest.exceptions.UserAlreadyExistsInDatabaseException;
import be.ehb.tristan.javaadvanced.internquest.exceptions.UserNotFoundByIdGiven;
import be.ehb.tristan.javaadvanced.internquest.models.User;
import be.ehb.tristan.javaadvanced.internquest.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserRestController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/testerror")
    public void testError(){
        throw new UserAlreadyExistsInDatabaseException("User already exists in database");
    }

    @GetMapping("/users/id/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping("/users/username/{username}")
    public User getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }
}
