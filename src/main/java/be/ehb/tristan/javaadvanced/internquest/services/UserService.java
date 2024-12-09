package be.ehb.tristan.javaadvanced.internquest.services;

import be.ehb.tristan.javaadvanced.internquest.enums.Role;
import be.ehb.tristan.javaadvanced.internquest.exceptions.UserAlreadyExistsInDatabaseException;
import be.ehb.tristan.javaadvanced.internquest.exceptions.UserCreationException;
import be.ehb.tristan.javaadvanced.internquest.exceptions.UserNotFoundByIdGivenException;
import be.ehb.tristan.javaadvanced.internquest.exceptions.UserNotFoundByUsernameGivenException;
import be.ehb.tristan.javaadvanced.internquest.models.Company;
import be.ehb.tristan.javaadvanced.internquest.models.User;
import be.ehb.tristan.javaadvanced.internquest.repositories.AchievementRepository;
import be.ehb.tristan.javaadvanced.internquest.repositories.ActivityRepository;
import be.ehb.tristan.javaadvanced.internquest.repositories.CompanyRepository;
import be.ehb.tristan.javaadvanced.internquest.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private AchievementRepository achievementRepository;

    @Autowired
    private CompanyRepository companyRepository;

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
                .orElseThrow(() -> new UserNotFoundByIdGivenException("User not found with the following ID: " + user.getId()));

        String encryptedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setDescription(user.getDescription());
        existingUser.setAddress(user.getAddress());
        existingUser.setPassword(encryptedPassword);
        existingUser.setRole(Role.REGULAR_USER);

        userRepository.save(existingUser);
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundByIdGivenException("User not found with the following ID: " + userId));

        user.getCompanies().forEach(company -> {
            company.getUsers().remove(user);
            companyRepository.save(company);
        });

        companyRepository.flush();

        user.getAchievements().forEach(achievement -> {
            achievement.getUsers().remove(user);
            achievementRepository.save(achievement);
        });

        user.getActivities().forEach(activity -> {
            activity.getUsers().remove(user);
            activityRepository.save(activity);
        });

        achievementRepository.flush();
        activityRepository.flush();

        userRepository.delete(user);

        user.getCompanies().forEach(company -> {
            if (company.getUsers().isEmpty()) {
                companyRepository.delete(company);
            }
        });
    }

    public void validateUserAddress(User user){
        String street = user.getAddress().getStreetName();
        int houseNumber = user.getAddress().getHouseNumber();
        int postalCode = user.getAddress().getPostalCode();
        String city = user.getAddress().getCity();
        String country = user.getAddress().getCountry();
        int busNumber = user.getAddress().getBusNumber();
        boolean isPostalCodeValid = true;

        if(postalCode < 1000 || postalCode > 9999){
            isPostalCodeValid = false;
        }

        if(!(street == null || street.isEmpty())){
            if(houseNumber <= 0 || isPostalCodeValid || city == "" || country == ""){
                throw new UserCreationException("Address is optional but needs to be valid if used.");
            }
        }

        if(houseNumber != 0){
            if(street == "" || isPostalCodeValid || city == "" || country == ""){
                throw new UserCreationException("Address is optional but needs to be valid if used.");
            }
        }

        if(isPostalCodeValid){
           if(street == "" || houseNumber <= 0 || city == "" || country == ""){
               throw new UserCreationException("Address is optional but needs to be valid if used.");
           }
        }

        if(!(city == null || city.isEmpty())){
            if(street == "" || houseNumber <= 0 || isPostalCodeValid || city == "" || country == ""){
                throw new UserCreationException("Address is optional but needs to be valid if used.");
            }
        }

        if(!(country == null || country.isEmpty())){
            if(street == "" || houseNumber <= 0 || isPostalCodeValid || city == "" || country == ""){
                throw new UserCreationException("Address is optional but needs to be valid if used.");
            }
        }

        if(!isPostalCodeValid && postalCode != 0){
            throw new UserCreationException("Address is optional but needs to be valid if used.");
        }
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