package be.ehb.tristan.javaadvanced.internquest.services.user;

import be.ehb.tristan.javaadvanced.internquest.exceptions.UserAlreadyExistsInDatabaseException;
import be.ehb.tristan.javaadvanced.internquest.exceptions.UserNotFoundByIdGivenException;
import be.ehb.tristan.javaadvanced.internquest.exceptions.UserNotFoundByUsernameGivenException;
import be.ehb.tristan.javaadvanced.internquest.models.User;
import be.ehb.tristan.javaadvanced.internquest.repositories.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundByIdGivenException("User not found with the following ID: " + id));
    }

    public User getUserByUsername(String username) {
//        return userRepository.findByUsername(username)
//                .orElseThrow(() -> new UserNotFoundByUsernameGivenException("User not found with the following username: " + username));
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UserNotFoundByUsernameGivenException("User not found with the following username: " + username);
        }
        return user;
    }

    public User addUser(User user) {
        User doesUserExist = userRepository.findByUsername(user.getUsername());

        if (doesUserExist != null) {
            throw new UserAlreadyExistsInDatabaseException("User already exists with username: " + user.getUsername());
        }

        return userRepository.save(user);
    }
}
