package com.BankingApplications.controller;

import com.BankingApplications.dto.AuthResponse;
import com.BankingApplications.dto.ChangePasswordRequest;
import com.BankingApplications.dto.LoginRequest;
import com.BankingApplications.dto.RegisterRequest;
import com.BankingApplications.dto.UpdateProfileRequest;
import com.BankingApplications.entity.User;
import com.BankingApplications.repository.UserRepository;
import com.BankingApplications.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * User Login Endpoint
     * POST /api/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtUtil.generateToken(userDetails.getUsername());
            
            // Get user details
            User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
            
            // Prepare user response
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("id", user.getId());
            userMap.put("username", user.getUsername());
            userMap.put("email", user.getEmail());
            userMap.put("fullName", user.getFullName());
            userMap.put("phoneNumber", user.getPhoneNumber());
            userMap.put("address", user.getAddress());
            userMap.put("dateOfBirth", user.getDateOfBirth());
            userMap.put("createdDate", user.getCreatedDate());
            userMap.put("active", user.isActive());
            
            return ResponseEntity.ok(new AuthResponse(token, userMap));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * User Registration Endpoint
     * POST /api/auth/register
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            // Check if username already exists
            if (userRepository.existsByUsername(request.getUsername())) {
                return ResponseEntity.badRequest().body(Map.of("error", "Username already exists"));
            }
            
            // Check if email already exists
            if (userRepository.existsByEmail(request.getEmail())) {
                return ResponseEntity.badRequest().body(Map.of("error", "Email already exists"));
            }
            
            // Create new user
            User user = new User();
            user.setUsername(request.getUsername());
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setFullName(request.getFullName());
            user.setActive(true);
            user.setCreatedDate(LocalDateTime.now());
            user.setUpdatedDate(LocalDateTime.now());
            
            userRepository.save(user);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "User registered successfully");
            response.put("userId", user.getId());
            response.put("username", user.getUsername());
            response.put("email", user.getEmail());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Get Current User Profile
     * GET /api/auth/profile
     */
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            // Validate authorization header
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body(Map.of("error", "Authorization header missing or invalid"));
            }
            
            String token = authHeader.substring(7);
            String username = jwtUtil.extractUsername(token);
            
            // Find user
            User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
            
            // Return user profile
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("id", user.getId());
            userMap.put("username", user.getUsername());
            userMap.put("email", user.getEmail());
            userMap.put("fullName", user.getFullName());
            userMap.put("phoneNumber", user.getPhoneNumber());
            userMap.put("address", user.getAddress());
            userMap.put("dateOfBirth", user.getDateOfBirth());
            userMap.put("createdDate", user.getCreatedDate());
            userMap.put("updatedDate", user.getUpdatedDate());
            userMap.put("active", user.isActive());
            
            return ResponseEntity.ok(userMap);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Update User Profile
     * PUT /api/auth/profile
     */
    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@RequestBody UpdateProfileRequest request,
                                           @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            // Validate authorization header
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body(Map.of("error", "Authorization header missing or invalid"));
            }
            
            String token = authHeader.substring(7);
            String username = jwtUtil.extractUsername(token);
            
            // Find user
            User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
            
            // Update full name
            if (request.getFullName() != null && !request.getFullName().trim().isEmpty()) {
                user.setFullName(request.getFullName());
            }
            
            // Update email with validation
            if (request.getEmail() != null && !request.getEmail().trim().isEmpty()) {
                if (!request.getEmail().equals(user.getEmail()) && 
                    userRepository.existsByEmail(request.getEmail())) {
                    return ResponseEntity.badRequest().body(Map.of("error", "Email already in use by another account"));
                }
                user.setEmail(request.getEmail());
            }
            
            // Update phone number
            if (request.getPhoneNumber() != null) {
                user.setPhoneNumber(request.getPhoneNumber());
            }
            
            // Update address
            if (request.getAddress() != null) {
                user.setAddress(request.getAddress());
            }
            
            // Update date of birth
            if (request.getDateOfBirth() != null) {
                user.setDateOfBirth(request.getDateOfBirth());
            }
            
            user.setUpdatedDate(LocalDateTime.now());
            userRepository.save(user);
            
            // Return updated user info
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("id", user.getId());
            userMap.put("username", user.getUsername());
            userMap.put("email", user.getEmail());
            userMap.put("fullName", user.getFullName());
            userMap.put("phoneNumber", user.getPhoneNumber());
            userMap.put("address", user.getAddress());
            userMap.put("dateOfBirth", user.getDateOfBirth());
            userMap.put("createdDate", user.getCreatedDate());
            userMap.put("updatedDate", user.getUpdatedDate());
            userMap.put("active", user.isActive());
            
            return ResponseEntity.ok(userMap);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Change User Password
     * POST /api/auth/change-password
     */
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request,
                                            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            // Validate authorization header
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body(Map.of("error", "Authorization header missing or invalid"));
            }
            
            String token = authHeader.substring(7);
            String username = jwtUtil.extractUsername(token);
            
            // Find user
            User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
            
            // Validate current password
            if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
                return ResponseEntity.badRequest().body(Map.of("error", "Current password is incorrect"));
            }
            
            // Validate new password length
            if (request.getNewPassword().length() < 6) {
                return ResponseEntity.badRequest().body(Map.of("error", "New password must be at least 6 characters long"));
            }
            
            // Update password
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            user.setUpdatedDate(LocalDateTime.now());
            userRepository.save(user);
            
            return ResponseEntity.ok(Map.of("message", "Password changed successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Delete/Deactivate User Account
     * DELETE /api/auth/account
     */
    @DeleteMapping("/account")
    public ResponseEntity<?> deleteAccount(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            // Validate authorization header
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body(Map.of("error", "Authorization header missing or invalid"));
            }
            
            String token = authHeader.substring(7);
            String username = jwtUtil.extractUsername(token);
            
            // Find user
            User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
            
            // Check if user has active accounts with balance
            boolean hasActiveAccounts = user.getAccounts().stream()
                .anyMatch(account -> account.isActive() && account.getBalance().doubleValue() > 0);
            
            if (hasActiveAccounts) {
                return ResponseEntity.badRequest().body(Map.of("error", 
                    "Cannot delete account. Please close all accounts with zero balance first"));
            }
            
            // Soft delete - deactivate user
            user.setActive(false);
            user.setUpdatedDate(LocalDateTime.now());
            userRepository.save(user);
            
            return ResponseEntity.ok(Map.of("message", "Account deactivated successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Forgot Password - Send Reset Link
     * POST /api/auth/forgot-password
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            
            // Find user by email
            User user = userRepository.findByEmail(email).orElse(null);
            
            // Always return success message for security (don't reveal if email exists)
            // In production, you would:
            // 1. Generate a password reset token
            // 2. Save token to database with expiry
            // 3. Send email with reset link
            
            return ResponseEntity.ok(Map.of("message", "If the email exists, a password reset link will be sent"));
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of("message", "If the email exists, a password reset link will be sent"));
        }
    }

    /**
     * Reset Password
     * POST /api/auth/reset-password
     */
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        try {
            String token = request.get("token");
            String newPassword = request.get("newPassword");
            
            // Validate new password
            if (newPassword.length() < 6) {
                return ResponseEntity.badRequest().body(Map.of("error", "Password must be at least 6 characters long"));
            }
            
            // In production, you would:
            // 1. Validate the reset token from database
            // 2. Find user by token
            // 3. Update password
            // 4. Invalidate the token
            
            return ResponseEntity.ok(Map.of("message", "Password reset successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid or expired token"));
        }
    }

    /**
     * Get User Statistics
     * GET /api/auth/statistics
     */
    @GetMapping("/statistics")
    public ResponseEntity<?> getUserStatistics(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            // Validate authorization header
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body(Map.of("error", "Authorization header missing or invalid"));
            }
            
            String token = authHeader.substring(7);
            String username = jwtUtil.extractUsername(token);
            
            // Find user
            User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
            
            // Calculate statistics
            int totalAccounts = user.getAccounts().size();
            int activeAccounts = (int) user.getAccounts().stream()
                .filter(account -> account.isActive())
                .count();
            double totalBalance = user.getAccounts().stream()
                .filter(account -> account.isActive())
                .mapToDouble(account -> account.getBalance().doubleValue())
                .sum();
            
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalAccounts", totalAccounts);
            stats.put("activeAccounts", activeAccounts);
            stats.put("totalBalance", totalBalance);
            stats.put("memberSince", user.getCreatedDate());
            stats.put("lastUpdated", user.getUpdatedDate());
            stats.put("profileComplete", isProfileComplete(user));
            
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Test Connection Endpoint
     * GET /api/auth/test
     */
    @GetMapping("/test")
    public ResponseEntity<?> testConnection() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Banking Application Backend is running!");
        response.put("port", "3636");
        response.put("database", "siddu");
        response.put("currency", "INR");
        response.put("status", "connected");
        response.put("timestamp", System.currentTimeMillis());
        response.put("version", "1.0.0");
        return ResponseEntity.ok(response);
    }

    /**
     * Check if profile is complete
     */
    private boolean isProfileComplete(User user) {
        return user.getFullName() != null && !user.getFullName().isEmpty() &&
               user.getPhoneNumber() != null && !user.getPhoneNumber().isEmpty() &&
               user.getAddress() != null && !user.getAddress().isEmpty() &&
               user.getDateOfBirth() != null;
    }

    /**
     * Get user by ID (Admin only - for future use)
     */
    @GetMapping("/user/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id,
                                         @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            // Validate authorization header
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body(Map.of("error", "Authorization header missing or invalid"));
            }
            
            // In production, check if user has admin role
            User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
            
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("id", user.getId());
            userMap.put("username", user.getUsername());
            userMap.put("email", user.getEmail());
            userMap.put("fullName", user.getFullName());
            userMap.put("active", user.isActive());
            userMap.put("createdDate", user.getCreatedDate());
            
            return ResponseEntity.ok(userMap);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}