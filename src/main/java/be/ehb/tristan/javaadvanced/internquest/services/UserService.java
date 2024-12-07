package be.ehb.tristan.javaadvanced.internquest.services;

import be.ehb.tristan.javaadvanced.internquest.enums.Role;
import be.ehb.tristan.javaadvanced.internquest.exceptions.UserAlreadyExistsInDatabaseException;
import be.ehb.tristan.javaadvanced.internquest.exceptions.UserNotFoundByIdGivenException;
import be.ehb.tristan.javaadvanced.internquest.exceptions.UserNotFoundByUsernameGivenException;
import be.ehb.tristan.javaadvanced.internquest.models.Company;
import be.ehb.tristan.javaadvanced.internquest.models.User;
import be.ehb.tristan.javaadvanced.internquest.repositories.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private CompanyService companyService;

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

    public String verify(User user) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        if (authentication.isAuthenticated()){
            return jwtService.generateToken(user.getUsername());
        }
        return "Invalid username or password";//TODO exception voor maken

    }

    public void addCompanyToUser(Long userId, Long companyId) {
        User user = getUserById(userId);
        Company company = companyService.getCompanyById(companyId);
        if(user == null) {
            throw new UserNotFoundByIdGivenException("User not found with the following ID: " + userId);
        }
        if(company == null) {
            throw new UserNotFoundByIdGivenException("Company not found with the following ID: " + companyId);
        }
        user.getCompanies().add(company);
        userRepository.save(user);
    }

    public void updateUser(User user){
        userRepository.save(user);
    }

    public void editUser(User user){
        User existingUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found with the following ID: " + user.getId()));

        String encryptedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setDescription(user.getDescription());
        existingUser.setAddress(user.getAddress());
        existingUser.setPassword(encryptedPassword);
        existingUser.setRole(Role.REGULAR_USER);

        userRepository.save(existingUser);
    }
//    public String verify(LoginDTO loginDTO) {
//        User user = userRepository.findByUsername(loginDTO.getUsername());
//
//        if (user == null) {
//            return "Invalid username or password";
//        }
//
//        if (bCryptPasswordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
//            return jwtService.generateToken(user.getUsername());
//        } else {
//            return "Invalid username or password";
//        }
//    }
}