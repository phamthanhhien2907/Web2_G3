package com.example.security;
import com.example.model.Role;
import com.example.model.UserDemo;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class UserDetailsServiceIml implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserDemo user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + email));
        System.out.println("user" + user);

        boolean enabled = true;
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(), enabled, accountNonExpired,
                credentialsNonExpired, accountNonLocked, getAuthorities(user.getRole()));
    }

    private static List<GrantedAuthority> getAuthorities (Set<Role> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (roles == null || roles.isEmpty()) {
            authorities.add(new SimpleGrantedAuthority("USER")); // Default role if no roles are found
        } else {
            for (Role role : roles) {
                if (role != null && role.getRole() != null) {
                    authorities.add(new SimpleGrantedAuthority(role.getRole()));
                }
            }
        }
        return authorities;
    }
}