package com.neval.user_management.controller;

import com.neval.user_management.model.User.UserRequest;
import com.neval.user_management.model.User.UserResponse;
import com.neval.user_management.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody UserRequest userRequest) {
        userService.createUser(userRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Void> updateUser(@PathVariable UUID userId, @RequestBody UserRequest userRequest) {
        userService.updateUser(userId, userRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserResponse> getUserByUsername(@PathVariable String username) {
        UserResponse userResponse = userService.getUserByUsername(username);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @GetMapping("/count")
    public long getUserCount() {
        return userService.getUserCount();
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserResponse>> searchUsers(@RequestParam String username) { //TODO: Future Enhance Paginate
        List<UserResponse> users = userService.searchUsersByUsername(username);
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
        userService.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
