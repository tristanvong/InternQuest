package be.ehb.tristan.javaadvanced.internquest.services.user;

import be.ehb.tristan.javaadvanced.internquest.exceptions.UserAlreadyExistsInDatabaseException;
import be.ehb.tristan.javaadvanced.internquest.exceptions.UserNotFoundByIdGivenException;
import be.ehb.tristan.javaadvanced.internquest.exceptions.UserNotFoundByUsernameGivenException;
import be.ehb.tristan.javaadvanced.internquest.models.User;
import be.ehb.tristan.javaadvanced.internquest.repositories.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundByIdGivenException("User not found with the following ID: " + id));
    }

//    public User getUserByUsername(String username) {
//        User user = userRepository.findByUsername(username);
//        if (user == null) {
//            throw new UserNotFoundByUsernameGiven("User not found with the folowing username: " + username);
//        }
//        return user;
//    }

    public User getUserByUsername(String usn) {
        return userRepository.findByUsername(usn)
                .orElseThrow(() -> new UserNotFoundByUsernameGivenException("User not found with the following username: " + usn));
    }

    public User addUser(User user) {
        Optional<User> existingUser = userRepository.findByUsername(user.getUsername());

        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsInDatabaseException("User already exists: " + user.getUsername());
        }
        return userRepository.save(user);
    }

//    public UserOld addUser(UserOld user) {
//        Optional<UserOld> existingUser = userRepositoryOld.findByUsername(user.getUsername());
//
//        if (existingUser.isPresent()) {
//            throw new RuntimeException("User with username " + user.getUsername() + " already exists");
//        }
//        return userRepositoryOld.save(user);
//    }

}
