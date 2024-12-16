package com.example.config;
import com.example.jwtAuthFilter.JwtAuthFilter;
import com.example.security.CustomAuthenticationFailureHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Autowired
    private JwtAuthFilter authFilter;
    private CustomAuthenticationFailureHandler failureHandler;
    private final UserDetailsService userDetailsService;
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOriginPattern("*"); // Allow all origins; adjust for production
        config.addAllowedMethod("*"); // Allow all HTTP methods (GET, POST, etc.)
        config.addAllowedHeader("*"); // Allow all headers
        config.setAllowCredentials(true); // Allow credentials (if needed)
        source.registerCorsConfiguration("/**", config); // Apply to all endpoints
        return new CorsFilter(source);
    }
    public WebSecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
    @Bean
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
//    @Bean
//    public ProviderManager authenticationManager(
//            UserDetailsService userDetailsService,
//            PasswordEncoder passwordEncoder) {
//        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
//        authenticationProvider.setUserDetailsService(userDetailsService);
//        authenticationProvider.setPasswordEncoder(passwordEncoder);
//
//        return new ProviderManager(authenticationProvider);
//    }
    @Bean
    public ProviderManager authenticationManager() throws Exception {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());

        return new ProviderManager(authenticationProvider);
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(List.of("http://localhost:5173")); // Cho phép origin frontend
                    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Cho phép các phương thức
                    config.setAllowedHeaders(List.of("*")); // Cho phép tất cả headers
                    config.setAllowCredentials(true); // Cho phép gửi credentials như cookies
                    return config;
                }))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        request -> request
                                .requestMatchers("/api/**").permitAll()
//                                .requestMatchers("/api/addNewUser").permitAll()
                                .requestMatchers("/api/register").permitAll()
//                                .requestMatchers("/api/updateUser/{id}").permitAll()
//                                .requestMatchers("/api/deleteUser/{id}").permitAll()
//                                .requestMatchers("/api/users").permitAll()
//                                .requestMatchers("/api/addNewCompany").permitAll()
//                                .requestMatchers("/api/updateCompany/{id}").permitAll()
//                                .requestMatchers("/api/deleteCompany/{id}").permitAll()
//                                .requestMatchers("/api/company").permitAll()
//                                .requestMatchers("/").permitAll()
//                                .requestMatchers("/h2-console/**").permitAll()
//                                .requestMatchers("/login").permitAll()
                                .requestMatchers("/api/generateToken").permitAll()
//                                .requestMatchers("/register").permitAll()
//                                .requestMatchers("/addUser").hasAnyAuthority("USER", "ADMIN")
                                .anyRequest().authenticated()
                ).sessionManagement(sess -> sess
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
//                .formLogin(form -> form
//                        .loginPage("/login")
//                        .failureHandler(failureHandler)
//                        .permitAll())
//                .logout(config -> config
//                        .logoutSuccessUrl("/login"))
//                .headers(headers -> headers
//                        .frameOptions().sameOrigin()  // Cho phép iframe từ cùng một origin
//                )
                .build();
    }
}