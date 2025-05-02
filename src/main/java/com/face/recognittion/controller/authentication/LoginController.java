package com.face.recognittion.controller.authentication;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.face.recognittion.model.User;
import com.face.recognittion.requests.UserRequest;
import com.face.recognittion.service.UserService;

import org.springframework.web.bind.annotation.RequestParam;
import com.face.recognittion.service.TokenService; // Add this import

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    @Autowired
    UserService userService;

    @Autowired
    TokenService tokenService; // Add this field

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserRequest userRequest) {
        String studentId = userRequest.getStudentId();
        String password = userRequest.getPassword();
        if (studentId == null || studentId.isEmpty()) {
            return ResponseEntity.badRequest().body("Student ID is required");
        }
        if (password == null || password.isEmpty()) {
            return ResponseEntity.badRequest().body("Password is required");
        }
        try {
            User user = userService.getUserByStudentId(studentId);
            if (user == null) {
                return ResponseEntity.badRequest().body("User not found");
            }

            boolean isPasswordValid = userService.verifyUserPassword(user.getId(), password);
            if (!isPasswordValid) {
                return ResponseEntity.badRequest().body("Invalid credentials");
            }

            // Generate token
            String token = tokenService.generateToken(user);

            // Return token in response
            return ResponseEntity.ok(Map.of("message", "Login successful", "token", token, "user", user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error during login: " + e.getMessage());
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<Boolean> getMethodName(@RequestParam Map<String, String> param) {
        return ResponseEntity.ok(true);
    }

    @GetMapping("/register")
    public ResponseEntity<Boolean> register() {
        return ResponseEntity.ok(true);
    }
}
