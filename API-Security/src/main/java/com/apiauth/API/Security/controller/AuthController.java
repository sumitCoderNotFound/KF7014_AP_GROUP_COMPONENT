package com.apiauth.API.Security.controller;

import com.apiauth.API.Security.model.User;
import com.apiauth.API.Security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import java.util.Map;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * REST controller for handling user authentication and account-related operations.
 * Also provides endpoints for login, registration, password change, token validation,
 * token refresh, and logout.
 *
 * <p>Base URI: /api/authenticate</p>
 */
@RestController
@RequestMapping("/api/authenticate")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Authenticates a user using username and password,
     * and returns a JWT access token and refresh token if successful.
     *
     * @param request An AuthRequest object containing username and password.
     * @return A ResponseEntity containing accessToken, refreshToken and success message.
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody AuthRequest request) {
        System.out.println("Login");

        // Authenticate user
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        // Generate token if authentication passed
        UserDetails user = userDetailsService.loadUserByUsername(request.getUsername());
        String accessToken = jwtService.generateToken(user.getUsername());
        String refreshToken = jwtService.generateRefreshToken(user);

        return ResponseEntity.ok(Map.of(
                "message", "Login successful",
                "accessToken", accessToken,
                "refreshToken", refreshToken
        ));
    }


    /**
     * Registers a new user with hashed password.
     *
     * @param request An AuthRequest object containing username and password.
     * @return A ResponseEntity with registration status message.
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AuthRequest request) {
        System.out.println("Register");
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username already exists.");
        }

        // Encode the password
        String hashedPassword = passwordEncoder.encode(request.getPassword());

        // Create and save the new user
        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setPassword(hashedPassword);

        userRepository.save(newUser);

        return ResponseEntity.ok("User registered successfully.");
    }


    /**
     * Allows a logged-in user to change their password by verifying the old one.
     *
     * @param request A ChangePassword object containing old and new passwords.
     * @param authentication The current authenticated user's context.
     * @return A ResponseEntity with password change status message.
     */
    @PutMapping("/changepassword")
    public ResponseEntity<String> changePassword(@RequestBody ChangePassword request,
                                                 Authentication authentication) {
        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body("Incorrect current password.");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        return ResponseEntity.ok("Password changed successfully.");
    }

    /**
     * Simulates a user logout.
     * Note: Actual token deletion should be handled on the client side.
     *
     * @return A ResponseEntity with logout status message.
     */
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout() {

        return ResponseEntity.ok(Map.of("message", "Logged out successfully.")); // delete token on client side react js sumit
    }


    /**
     * Validates the JWT access token from the current authentication context.
     *
     * @param authentication The current authentication token.
     * @return A ResponseEntity with token validation status and details.
     */
    @GetMapping("/validate")
    public ResponseEntity<TokenValidationResponse> validateToken(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return ResponseEntity.ok(new TokenValidationResponse(
                    true,
                    authentication.getName(),
                    "Token is valid",
                    "ok"
            ));
        } else {
            return ResponseEntity.ok(new TokenValidationResponse(
                    false,
                    null,
                    "Token is expired or invalid",
                    "expired"
            ));
        }
    }


    /**
     * Validates the refresh token and issues a new access token if valid.
     *
     * @param request A map containing the refresh token.
     * @return A ResponseEntity with a new access token or an error message.
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");

        if (refreshToken == null) {
            return ResponseEntity.badRequest().body("Missing refresh token");
        }

        try {
            String username = jwtService.extractUsername(refreshToken);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtService.isTokenValid(refreshToken, userDetails)) {
                String newAccessToken = jwtService.generateToken(userDetails.getUsername());
                return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired refresh token");
        }
    }


    /**
     * Deletes a user account based on the provided username.
     *
     * @param username The username of the account to delete.
     * @return A ResponseEntity with deletion status message.
     */
    @DeleteMapping("/delete/{username}")
    public ResponseEntity<String> deleteUser(@PathVariable String username) {
        System.out.println(username);
        return userRepository.findByUsername(username).map(user -> {
            userRepository.delete(user);
            return ResponseEntity.ok("User deleted successfully.");
        }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("User not found."));
    }
}
