package com.neval.user_management;

import com.neval.user_management.model.User.UserRequest;
import com.neval.user_management.model.User.UserResponse;
import com.neval.user_management.repository.UserRepository;
import com.neval.user_management.repository.entities.User;
import com.neval.user_management.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private UserRequest userRequest;
    private User user;
    private UUID userId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup
        userRequest = new UserRequest();
        userRequest.setUsername("LewisHamilton");
        userRequest.setEmail("lewham@gmail.com");
        userRequest.setPassword("password123");

        userId  = UUID.randomUUID();

        user = new User();
        user.setId(userId);
        user.setUsername("LewisHamilton");
        user.setEmail("lewham@gmail.com");
        user.setPassword("hashed_password");
    }

    @Test
    void testCreateUser() {
        // Arrange
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        userService.createUser(userRequest);

        // Assert
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertEquals(userRequest.getUsername(), savedUser.getUsername());
        assertEquals(userRequest.getEmail(), savedUser.getEmail());
        assertNotNull(savedUser.getPassword());
        assertNotEquals(userRequest.getPassword(), savedUser.getPassword()); // Password should be hashed
    }

    @Test
    void testUpdateUser_WithPasswordChange() {
        // Arrange
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        userService.updateUser(userId, userRequest);

        // Assert
        verify(userRepository).save(user);
        assertEquals(userRequest.getEmail(), user.getEmail());
        assertNotEquals("hashed_password", user.getPassword());
    }

    @Test
    void testUpdateUser_WithoutPasswordChange() {
        // Arrange
        userRequest.setPassword(null);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        userService.updateUser(userId, userRequest);

        // Assert
        verify(userRepository).save(user);
        assertEquals(userRequest.getEmail(), user.getEmail());
        assertEquals("hashed_password", user.getPassword()); // Password should remain the same
    }

    @Test
    void testGetUserByUsername_UserExists() {
        // Arrange
        when(userRepository.findByUsername("LandoNorris")).thenReturn(Optional.of(user));

        // Act
        UserResponse response = userService.getUserByUsername("LandoNorris");

        // Assert
        assertEquals(user.getId(), response.getId());
        assertEquals(user.getUsername(), response.getUsername());
        assertEquals(user.getEmail(), response.getEmail());
    }

    @Test
    void testGetUserByUsername_UserNotFound() {
        // Arrange
        when(userRepository.findByUsername("ArytonSenna")).thenReturn(Optional.empty());

        // Act
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.getUserByUsername("ArytonSenna");
        });

        // Assert
        assertEquals("User not found", exception.getMessage());
    }
}
