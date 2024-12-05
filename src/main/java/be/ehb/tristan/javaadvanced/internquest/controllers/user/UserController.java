    package be.ehb.tristan.javaadvanced.internquest.controllers.user;

    import be.ehb.tristan.javaadvanced.internquest.dto.LoginDTO;
    import be.ehb.tristan.javaadvanced.internquest.exceptions.FormValueIncorrectException;
    import be.ehb.tristan.javaadvanced.internquest.exceptions.UserNotFoundByIdGivenException;
    import be.ehb.tristan.javaadvanced.internquest.exceptions.UserNotFoundByUsernameGivenException;
    import be.ehb.tristan.javaadvanced.internquest.models.User;
    import be.ehb.tristan.javaadvanced.internquest.services.general.JWTService;
    import be.ehb.tristan.javaadvanced.internquest.services.user.UserService;
    import jakarta.validation.Valid;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.ResponseEntity;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.security.core.userdetails.UserDetailsService;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.validation.BindingResult;
    import org.springframework.web.bind.annotation.*;

    import java.util.HashMap;
    import java.util.List;
    import java.util.Map;

    @Controller
    public class UserController {

        @Autowired
        private UserService userService;

        @GetMapping("/login")
        public String showLoginPage() {
            return "user/login-form";
        }
        @PostMapping("/login")
        public ResponseEntity<Map<String, String>> login(@RequestBody LoginDTO loginDTO) {
            String token = userService.verify(loginDTO);

            if ("Invalid username or password".equals(token)) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("message", "Invalid credentials");
                return ResponseEntity.status(401).body(errorResponse);
            }

            Map<String, String> response = new HashMap<>();
            response.put("token", token);

            return ResponseEntity.ok(response);
        }
        @GetMapping("users")
        public String getAllUsers(Model model) {
            List<User> users = userService.getAllUsers();
            model.addAttribute("users", users);
            if (users.isEmpty()) {
                throw new RuntimeException("No users found");
            }
            return "user/user-list";
        }
        @GetMapping("new/users/id/{id}")
        public String getUserById(@PathVariable Long id, Model model) {
            User user = userService.getUserById(id);
            if (user == null) {
                throw new UserNotFoundByIdGivenException("User with id " + id + " not found");
            }
            model.addAttribute("user", user);
            return "user/user";
        }
        @GetMapping("new/users/username/{username}")
        public String getUserByUsername(@PathVariable String username, Model model) {
            User user = userService.getUserByUsername(username);
            if (user == null) {
                throw new UserNotFoundByUsernameGivenException("User with id " + username + " not found");
            }
            model.addAttribute("user", user);
            return "user/user";
        }
        @GetMapping("/create-user")
        public String createUser(Model model) {
            model.addAttribute("user", new User());
            model.addAttribute("isCreate", true);
            return "user/create-user-form";
        }
        @PostMapping("/create-user")
        public String createUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, Model model) {
            if (bindingResult.hasErrors()) {
                return "user/create-user-form";
            }
            if(user.getAddress().getPostalCode() > 9999){
                throw new FormValueIncorrectException("Postal code is too big");
            }
            userService.addUser(user);
            return "user/blank-page";
        }
        @GetMapping("/test")
        public String test() {
            throw new NullPointerException("This is a test");
        //        return "basicslearning/home";
        }
        @GetMapping("/info")
        public String getUserInfo(Model model) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            model.addAttribute("username", username);
            return "user/information";
        }
}