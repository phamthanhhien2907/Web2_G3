package com.example.jwtAuthFilter;

import com.example.security.JwtService;
import com.example.security.UserDetailsServiceIml;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsServiceIml userDetailsService;

    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, IOException {

        String authHeader = request.getHeader("Authorization");
        System.out.println("authHeader: " + authHeader);
        String token = null;
        String username = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7); // Extract token
            try {
                // Lấy username từ token
                username = jwtService.extractUsername(token);
                System.out.println("username: " + username);
                System.out.println("token: " + token);
                System.out.println("Bearer: " + authHeader.startsWith("Bearer "));
            } catch (Exception e) {
                // Nếu có lỗi trong việc lấy username từ token
                System.out.println("Invalid token or failed to extract username.");
            }
        }


        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//            if (request.getRequestURI().startsWith("/api/generateToken")) {
//                filterChain.doFilter(request, response);  // Cho phép tiếp tục mà không cần xác thực
//                return;
//            }
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            System.out.println("userDetails: " + userDetails);
            if (jwtService.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                System.out.println("Authentication set: " + SecurityContextHolder.getContext().getAuthentication());
            }
        }
//        if (request.getRequestURI().equals("/api/generateToken")) {
//            filterChain.doFilter(request, response);  // Cho phép tiếp tục mà không cần xác thực
//            return;
//        }
        // Continue the filter chain
        filterChain.doFilter(request, response);
    }
}
