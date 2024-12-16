package com.example.controller;
import com.example.model.UserDemo;
import com.example.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class AuthenRestController {
    private final ProviderManager authenticationManager;
    private final JwtService jwtService;
    public AuthenRestController(ProviderManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService  = jwtService;
    }
    @PostMapping("/api/generateToken")
    public ResponseEntity<?> authenticateAndGetToken(@RequestBody UserDemo authRequest) {
        try {
            Authentication authentication =
                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                            authRequest.getEmail(),
                            authRequest.getPassword()));

            if (authentication.isAuthenticated()) {
                System.out.println("authentication" +  authentication);
                String token = jwtService.generateToken(authRequest.getEmail());
                return ResponseEntity.ok(token);  
            }else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed.");
            }
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password.");
        }
    }
}
