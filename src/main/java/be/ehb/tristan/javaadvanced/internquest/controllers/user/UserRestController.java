package be.ehb.tristan.javaadvanced.internquest.controllers.user;

import be.ehb.tristan.javaadvanced.internquest.dto.LoginDTO;
import be.ehb.tristan.javaadvanced.internquest.dto.RegisterDTO;
import be.ehb.tristan.javaadvanced.internquest.enums.Role;
import be.ehb.tristan.javaadvanced.internquest.models.User;
import be.ehb.tristan.javaadvanced.internquest.services.general.JWTService;
import be.ehb.tristan.javaadvanced.internquest.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserRestController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterDTO registerDTO) {
        try {
            User user = new User();
            user.setUsername(registerDTO.getUsername());
            String rawPassword = registerDTO.getPassword();
            String encodedPassword = bCryptPasswordEncoder.encode(rawPassword);
            user.setPassword(encodedPassword);
            user.setFirstName(registerDTO.getFirstName());
            user.setLastName(registerDTO.getLastName());
            user.setRole(Role.REGULAR_USER);
            userService.addUser(user);

            System.out.println("Encoded Password: " + encodedPassword);
            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error registering user: " + e.getMessage());
        }
    }

//    @PostMapping("/login")
//    public ResponseEntity<Map<String, String>> login(@RequestBody LoginDTO loginDTO) {
//        System.out.println("test");
//        System.out.println("password " + loginDTO.getPassword());
//        System.out.println("encoded " + bCryptPasswordEncoder.encode(loginDTO.getPassword()));
//
//
//        try {
//            Authentication authentication = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword())
//            );
//
//            System.out.println("encoded " + bCryptPasswordEncoder.encode(loginDTO.getPassword()));
//
//            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//            String token = jwtService.generateToken(userDetails.getUsername());
//
//            Map<String, String> response = new HashMap<>();
//            response.put("token", token);
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            Map<String, String> errorResponse = new HashMap<>();
//            errorResponse.put("message", "Invalid username or password");
//            return ResponseEntity.status(401).body(errorResponse);
//        }
//    }
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody LoginDTO loginDTO) {
        try {
            User user = userService.getUserByUsername(loginDTO.getUsername());
            if (user == null) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("message", "Username or password is invalid/null");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
            }

            System.out.println("Entered Password: " + loginDTO.getPassword());
            System.out.println("Stored Password Hash: " + user.getPassword());

            boolean passwordMatches = bCryptPasswordEncoder.matches(loginDTO.getPassword().trim(), user.getPassword());
            if (!passwordMatches) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("message", "Wrong password");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
            }

            Authentication authentication = new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword());
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtService.generateToken(userDetails.getUsername());

            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Error logging user: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }


    @GetMapping("/protected")
    public ResponseEntity<String> protectedEndpoint() {
        return ResponseEntity.ok("You have access to this protected endpoint");
    }
}
