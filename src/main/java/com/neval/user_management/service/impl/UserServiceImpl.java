package com.neval.user_management.service.impl;

import com.neval.user_management.model.User.UserRequest;
import com.neval.user_management.model.User.UserResponse;
import com.neval.user_management.repository.UserRepository;
import com.neval.user_management.repository.entities.User;
import com.neval.user_management.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void createUser(UserRequest userRequest) {
        try {
            logger.info("Creating user with username: {}", userRequest.getUsername());

            User user = new User();
            user.setUsername(userRequest.getUsername());
            user.setEmail(userRequest.getEmail());
            user.setPassword(hashPassword(userRequest.getPassword()));

            userRepository.save(user);
            logger.info("User created successfully: {}", user.getId());
        } catch (Exception ex) {
            logger.error("Failed to create user: {}", userRequest.getUsername(), ex);
            throw new RuntimeException("Failed to create user", ex);
        }
    }

    @Override
    public void updateUser(UUID userId, UserRequest userRequest) {
        try {
            logger.info("Updating user with ID: {}", userId);
            User existingUser = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            existingUser.setUsername(userRequest.getUsername());
            existingUser.setEmail(userRequest.getEmail());

            if (userRequest.getPassword() != null && !userRequest.getPassword().isEmpty()) {
                existingUser.setPassword(hashPassword(userRequest.getPassword()));
            }

            userRepository.save(existingUser);
            logger.info("User updated successfully: {}", existingUser.getId());
        } catch (Exception ex) {
            logger.error("Failed to update user with ID: {}", userId, ex);
            throw new RuntimeException("Failed to update user", ex);
        }
    }

    @Override
    public UserResponse getUserByUsername(String username) {
        try {
            logger.info("Fetching user with username: {}", username);
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            logger.info("User fetched successfully: {}", user.getId());
            return new UserResponse(user.getId(), user.getUsername(), user.getEmail());
        } catch (Exception ex) {
            logger.error("Failed to fetch user by username: {}", username, ex);
            throw new RuntimeException("Failed to fetch user by username", ex);
        }
    }

    @Override
    public long getUserCount() {
        try {
            logger.info("Fetching user count");
            return userRepository.count();
        } catch (Exception ex) {
            logger.error("Failed to fetch user count", ex);
            throw new RuntimeException("Failed to get user count", ex);
        }
    }

    @Override
    public List<UserResponse> searchUsersByUsername(String username) {
        try {
            logger.info("Searching users with username containing: {}", username);

            List<User> users = userRepository.findByUsernameContainingIgnoreCase(username);

            return users.stream()
                    .map(this::mapToUserResponse)
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            logger.error("Failed to search users by username: {}", username, ex);
            throw new RuntimeException("Failed to search users by username", ex);
        }
    }

    @Override
    public void deleteUser(UUID userId) {
        try {
            logger.info("Deleting user with ID: {}", userId);

            userRepository.deleteById(userId);

            logger.info("User deleted successfully: {}", userId);
        } catch (Exception ex) {
            logger.error("Failed to delete user with ID: {}", userId, ex);
            throw new RuntimeException("Failed to delete user", ex);
        }
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(password.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            return bytesToHex(encodedhash);
        } catch (NoSuchAlgorithmException ex) {
            logger.error("Error hashing password", ex);
            throw new RuntimeException("Error hashing password", ex);
        }
    }

    private String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1)
                hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    private UserResponse mapToUserResponse(User user) {
        return new UserResponse(user.getId(), user.getUsername(), user.getEmail());
    }
}
