package be.ehb.tristan.javaadvanced.internquest.config;

import be.ehb.tristan.javaadvanced.internquest.services.general.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final CustomUserDetailsService customUserDetailsService;


    public SecurityConfiguration(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder())  // Use the PasswordEncoder bean
                .and().build();
    }

    @Bean
    public org.springframework.security.web.SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/login", "/register", "/create-user", "/home", "/css/**", "/js/**", "/new/**")
                                .permitAll() // Allow these pages without authentication
                                .anyRequest().authenticated() // Require authentication for all other pages
                )
                .formLogin(formLogin ->
                        formLogin
                                .loginPage("/login")
                                .loginProcessingUrl("/login")
                                .defaultSuccessUrl("/loggedin", true)
                                .permitAll() // Allow everyone to access the login page
                )
                .logout(logout ->
                        logout
                                .permitAll() // Allow everyone to access the logout page
                                .logoutUrl("/logout") // URL for logout
                                .logoutSuccessUrl("/login?logout") // Redirect to login page after logout
                );

        return http.build();
    }
}
