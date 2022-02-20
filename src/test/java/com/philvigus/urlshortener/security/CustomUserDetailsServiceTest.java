package com.philvigus.urlshortener.security;

import com.philvigus.urlshortener.model.User;
import com.philvigus.urlshortener.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomUserDetailsServiceTest {
  private CustomUserDetailsService customUserDetailsService;

  @Mock UserRepository userRepository;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    customUserDetailsService = new CustomUserDetailsService(userRepository);
  }

  @Test
  void loadUserByUsername() {
    final String USERNAME = "username";
    final String PASSWORD = "password";

    User user = new User();
    user.setUsername(USERNAME);
    user.setPassword(PASSWORD);

    when(userRepository.findByUsername(USERNAME)).thenReturn(user);

    assertInstanceOf(
        CustomUserDetails.class, customUserDetailsService.loadUserByUsername(USERNAME));
    verify(userRepository, times(1)).findByUsername(USERNAME);
  }

  @Test
  void loadUserByUsernameNotFoundThrowsException() {
    final String USERNAME = "username";

    when(userRepository.findByUsername(USERNAME)).thenReturn(null);

    UsernameNotFoundException thrown =
        assertThrows(
            UsernameNotFoundException.class,
            () -> customUserDetailsService.loadUserByUsername(USERNAME));

    assertTrue(thrown.getMessage().equals("User not found"));
  }
}
