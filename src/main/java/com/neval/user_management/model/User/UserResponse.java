package com.neval.user_management.model.User;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.UUID;

@Value
@AllArgsConstructor
public class UserResponse {

    UUID id;
    String username;
    String email;
}
