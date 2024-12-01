package be.ehb.tristan.javaadvanced.internquest.basicsremovelater;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<UserOld> getAllUsers() {
        return userRepository.findAll();
    }

    public UserOld getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public UserOld getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public UserOld addUser(UserOld user) {
        Optional<UserOld> existingUser = userRepository.findByUsername(user.getUsername());

        if (existingUser.isPresent()) {
            throw new RuntimeException("User with username " + user.getUsername() + " already exists");
        }
        return userRepository.save(user);
    }

    public List<UserOld> getUsersByPartialUsername(String partialUsername) {
        return userRepository.findByUsernameContainingOrderByUsernameAsc(partialUsername);
    }
}
