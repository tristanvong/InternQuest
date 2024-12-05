package be.ehb.tristan.javaadvanced.internquest.services.user;

import be.ehb.tristan.javaadvanced.internquest.dto.LoginDTO;
import be.ehb.tristan.javaadvanced.internquest.exceptions.UserAlreadyExistsInDatabaseException;
import be.ehb.tristan.javaadvanced.internquest.exceptions.UserNotFoundByIdGivenException;
import be.ehb.tristan.javaadvanced.internquest.exceptions.UserNotFoundByUsernameGivenException;
import be.ehb.tristan.javaadvanced.internquest.models.User;
import be.ehb.tristan.javaadvanced.internquest.repositories.user.UserRepository;
import be.ehb.tristan.javaadvanced.internquest.services.general.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTService jwtService;

    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);

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
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

//    public String verify(User user) {
//        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
//
//        if (authentication.isAuthenticated()){
//            return jwtService.generateToken(user.getUsername());
//        }
//        return "Invalid username or password";//TODO exception voor maken
//
//    }
    public String verify(LoginDTO loginDTO) {
        User user = userRepository.findByUsername(loginDTO.getUsername());

        if (user == null) {
            return "Invalid username or password";
        }

        if (bCryptPasswordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            return jwtService.generateToken(user.getUsername());
        } else {
            return "Invalid username or password";
        }
    }
}