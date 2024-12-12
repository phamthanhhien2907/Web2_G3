package com.example.controller;
import com.example.model.UserDemo;
import com.example.security.JwtService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class AuthenRestController {
    private final ProviderManager authenticationManager;
    private final JwtService jwtService;
    public AuthenRestController(ProviderManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService  = jwtService;
    }
    @PostMapping("/api/generateToken")
    public String authenticateAndGetToken(@RequestBody UserDemo authRequest) {
        try {
            String user = authRequest.getEmail();
            String authToken = jwtService.generateToken(user);
            System.out.println("authToken" +  authToken);

            Authentication authentication =
                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                            authRequest.getEmail(),
                            authRequest.getPassword()));
            if (authentication.isAuthenticated()) {
                System.out.println("authentication" + authentication);
                return jwtService.generateToken(authRequest.getEmail());
            }
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password.");
        }
        return "Authentication failed";
    }
}
