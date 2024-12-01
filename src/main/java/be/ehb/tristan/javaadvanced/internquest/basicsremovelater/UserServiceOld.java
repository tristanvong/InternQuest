package be.ehb.tristan.javaadvanced.internquest.basicsremovelater;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceOld {

    @Autowired
    private UserRepositoryOld userRepositoryOld;

    public List<UserOld> getAllUsers() {
        return userRepositoryOld.findAll();
    }

    public UserOld getUserByUsername(String username) {
        return userRepositoryOld.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public UserOld getUserById(Long id) {
        return userRepositoryOld.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public UserOld addUser(UserOld user) {
        Optional<UserOld> existingUser = userRepositoryOld.findByUsername(user.getUsername());

        if (existingUser.isPresent()) {
            throw new RuntimeException("User with username " + user.getUsername() + " already exists");
        }
        return userRepositoryOld.save(user);
    }

    public List<UserOld> getUsersByPartialUsername(String partialUsername) {
        return userRepositoryOld.findByUsernameContainingOrderByUsernameAsc(partialUsername);
    }
}
