package com.neval.user_management.service;

import com.neval.user_management.model.User.UserRequest;
import com.neval.user_management.model.User.UserResponse;

public interface UserService {

    // ADD IN COMMENTS!!!
    void createUser(UserRequest userRequest);

    void updateUser(UserRequest userRequest);

    UserResponse getUserByUsername (String username);
}
