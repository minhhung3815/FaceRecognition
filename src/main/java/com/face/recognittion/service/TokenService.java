package com.face.recognittion.service;

import com.face.recognittion.model.User;
import org.springframework.stereotype.Service;
import java.util.Base64;

@Service
public class TokenService {

    private static final String SECRET_KEY = "your_secret_key"; // Replace with a secure key
    private static final long EXPIRATION_TIME = 86400000; // 1 day in milliseconds

    // Generate a token for the given user
    public String generateToken(User user) {
        String payload = user.getId() + ":" + user.getName();
        return Base64.getUrlEncoder().encodeToString(payload.getBytes()); // Use URL-safe Base64 encoding
    }

    // Decode the token
    public String decodeToken(String token) {
        byte[] decodedBytes = Base64.getUrlDecoder().decode(token); // Use URL-safe Base64 decoding
        return new String(decodedBytes);
    }

    // Validate the token
    public boolean validateToken(String token) {
        try {
            // Decode the token
            String decodedToken = decodeToken(token);

            // Validate the format (e.g., "userId:name")
            if (!decodedToken.contains(":")) {
                return false;
            }

            // Additional validation logic can be added here if needed
            return true;
        } catch (IllegalArgumentException e) {
            // Handle invalid Base64 token
            return false;
        }
    }

    // Extract user ID from the token
    public String getUserIdFromToken(String token) {
        try {
            String decodedToken = decodeToken(token);
            String[] parts = decodedToken.split(":");
            return parts[0]; // Return the userId part
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid token format");
        }
    }
}
