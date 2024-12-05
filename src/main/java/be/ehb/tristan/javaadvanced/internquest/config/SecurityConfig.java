    package be.ehb.tristan.javaadvanced.internquest.config;

    import be.ehb.tristan.javaadvanced.internquest.filter.JwtFilter;
    import be.ehb.tristan.javaadvanced.internquest.services.general.CustomUserDetailsService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.security.authentication.AuthenticationManager;
    import org.springframework.security.authentication.AuthenticationProvider;
    import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
    import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.security.web.SecurityFilterChain;

    @Configuration
    @EnableWebSecurity
    public class SecurityConfig {

        @Autowired
        private JwtFilter jwtFilter;

        private CustomUserDetailsService userDetailsService;

        public SecurityConfig(CustomUserDetailsService userDetailsService) {
            this.userDetailsService = userDetailsService;
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
                    .csrf(customizer -> customizer.disable())
                    .authorizeHttpRequests(request -> request
                            .requestMatchers("create-user", "login").permitAll()
                            .anyRequest().authenticated())
                    .formLogin(form -> form.loginPage("/login"));
                    //.formLogin(Customizer.withDefaults());
                    //.sessionManagement(customizer -> customizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
                    //.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    //                .formLogin(Customizer.withDefaults());
            return http.build();
        }

    //    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    //        http
    //                .csrf(csrf -> csrf.disable())
    //                .authorizeHttpRequests(auth -> auth
    //                        .requestMatchers("/create-user", "/login", "general-error").permitAll()
    //                        .anyRequest().authenticated()
    //                )
    //                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
    //
    //                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    //        return http.build();
    //    }

        @Bean
        public AuthenticationProvider authenticationProvider() {
            DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
            authProvider.setPasswordEncoder(new BCryptPasswordEncoder(12));
            authProvider.setUserDetailsService(userDetailsService);
            return authProvider;
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
            return authenticationConfiguration.getAuthenticationManager();
        }
    }