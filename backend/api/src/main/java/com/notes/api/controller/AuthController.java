package com.notes.api.controller;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.bind.annotation.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {
        "https://6931e4c0a3ca9a000849f8d9--mynoteui.netlify.app",
        "https://mynoteui.netlify.app",
        "http://localhost:3000"
})
public class AuthController {
    // Simple in-memory user storage
    private final InMemoryUserDetailsManager userDetailsManager;

    public AuthController() {
        // Create in-memory user manager with default users
        this.userDetailsManager = new InMemoryUserDetailsManager();
        // Create password encoder
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // Add default users
        userDetailsManager.createUser(
                User.builder()
                        .username("user")
                        .password(encoder.encode("password123"))
                        .roles("USER")
                        .build()
        );

        userDetailsManager.createUser(
                User.builder()
                        .username("admin")
                        .password(encoder.encode("admin123"))
                        .roles("USER", "ADMIN")
                        .build()
        );
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {
        try {
            String username = loginData.get("username");
            String password = loginData.get("password");

            if (username == null || password == null) {
                return ResponseEntity.badRequest().body("Username and password are required");
            }

            // Check if user exists
            if (!userDetailsManager.userExists(username)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("User not found");
            }

            // Load user details
            User userDetails = (User) userDetailsManager.loadUserByUsername(username);
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

            // Verify password
            if (!encoder.matches(password, userDetails.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid password");
            }

            // Create Basic Auth token
            String credentials = username + ":" + password;
            String basicAuthToken = Base64.getEncoder().encodeToString(credentials.getBytes());

            // Return success
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Login successful");
            response.put("username", username);
            response.put("token", basicAuthToken);
            response.put("authType", "Basic");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Authentication failed: " + e.getMessage());
        }
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verifyToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Basic ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("No valid authentication token");
        }

        try {
            String token = authHeader.substring(6);
            String credentials = new String(Base64.getDecoder().decode(token));
            String[] parts = credentials.split(":", 2);

            if (parts.length != 2) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid token format");
            }

            String username = parts[0];
            String password = parts[1];

            if (!userDetailsManager.userExists(username)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("User not found");
            }

            User userDetails = (User) userDetailsManager.loadUserByUsername(username);
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

            if (!encoder.matches(password, userDetails.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid credentials");
            }

            Map<String, Object> response = new HashMap<>();
            response.put("authenticated", true);
            response.put("username", username);
            response.put("roles", userDetails.getAuthorities().toString());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token verification failed: " + e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> registerData) {
        try {
            String username = registerData.get("username");
            String password = registerData.get("password");

            if (username == null || password == null) {
                return ResponseEntity.badRequest().body("Username and password are required");
            }

            if (userDetailsManager.userExists(username)) {
                return ResponseEntity.badRequest().body("Username already exists");
            }

            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

            userDetailsManager.createUser(
                    User.builder()
                            .username(username)
                            .password(encoder.encode(password))
                            .roles("USER")
                            .build()
            );

            return ResponseEntity.ok("User registered successfully");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
        }
    }
}
