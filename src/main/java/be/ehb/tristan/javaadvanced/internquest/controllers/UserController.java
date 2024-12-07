    package be.ehb.tristan.javaadvanced.internquest.controllers;

    import be.ehb.tristan.javaadvanced.internquest.enums.Role;
    import be.ehb.tristan.javaadvanced.internquest.exceptions.FormValueIncorrectException;
    import be.ehb.tristan.javaadvanced.internquest.exceptions.UserNotFoundByIdGivenException;
    import be.ehb.tristan.javaadvanced.internquest.exceptions.UserNotFoundByUsernameGivenException;
    import be.ehb.tristan.javaadvanced.internquest.models.User;
    import be.ehb.tristan.javaadvanced.internquest.services.UserService;
    import jakarta.validation.Valid;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.validation.BindingResult;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;

    @Controller
    public class UserController {

        @Autowired
        private UserService userService;

        @GetMapping("/login")
        public String showLoginPage() {
            return "user/login-form";
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
        public String createUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult) {
            if (bindingResult.hasErrors()) {
                return "user/create-user-form";
            }
            if(user.getAddress().getPostalCode() > 9999){
                throw new FormValueIncorrectException("Postal code is too big");
            }
            userService.addUser(user);
            return "user/login-form";
        }

        @GetMapping("/info")
        public String getUserInfo(Model model) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            model.addAttribute("username", username);
            return "user/information";
        }

        @GetMapping("/account")
        public String showAccountEditPage(Model model, Authentication authentication) {
            String username = authentication.getName();
            User user = userService.getUserByUsername(username);

            if(user == null) {
                throw new RuntimeException("User not found");
            }

            model.addAttribute("user", user);
            return "user/account-edit-form";
        }

        @PostMapping("/account")
        public String updateAccountInformation(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, Authentication authentication) {
            if(bindingResult.hasErrors()) {
                return "user/account-edit-form";
            }

            String username = authentication.getName();
            User userUsingURL = userService.getUserByUsername(username);
            if(userUsingURL == null){
                throw new RuntimeException("User not found");
            }
            user.setId(userUsingURL.getId());
            user.setRole(Role.REGULAR_USER);
            userService.editUser(user);
            return "redirect:/info";
        }

        @GetMapping("/delete/{userId}")
        public String deleteUser(@PathVariable Long userId) {
            //todo add validation that user cna only delete his own account
            try {
                userService.deleteUser(userId);
            } catch (RuntimeException e) {
                throw new RuntimeException(e);
            }
            return "redirect:/login";
        }
}