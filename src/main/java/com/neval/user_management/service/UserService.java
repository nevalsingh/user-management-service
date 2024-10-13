package com.neval.user_management.service;

import com.neval.user_management.model.User.UserRequest;
import com.neval.user_management.model.User.UserResponse;
import java.util.List;
import java.util.UUID;

public interface UserService {

    /**
     * Creates a new user
     */
    void createUser(UserRequest userRequest);

    /**
     * Updates an existing user
     */
    void updateUser(UUID userId, UserRequest userRequest);

    /**
     * Finds a user by their username.
     */
    UserResponse getUserByUsername (String username);

    /**
     * Gets count of all users
     */
    long getUserCount();

    /**
     * Looks for users containing wildcard pattern %% ignoring case
     */
    List<UserResponse> searchUsersByUsername(String username);

    /**
     * Removes user by userId
     */
    void deleteUser(UUID userId);
}
