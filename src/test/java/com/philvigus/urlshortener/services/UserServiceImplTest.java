package com.philvigus.urlshortener.services;

import com.philvigus.urlshortener.model.User;
import com.philvigus.urlshortener.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DisplayName("UserServiceImpl")
class UserServiceImplTest {
  private UserServiceImpl userService;

  @Mock UserRepository userRepository;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    userService = new UserServiceImpl(userRepository);
  }

  @Test
  @DisplayName("save - calls save on the user repository with the given User model")
  void save() {
    User user = new User();

    userService.save(user);

    verify(userRepository, times(1)).save(user);
  }

  @Test
  @DisplayName(
      "findByUsername - calls findByUsername on the user repository with the given username")
  void findByUsername() {
    final String USERNAME = "username";

    userService.findByUsername(USERNAME);

    verify(userRepository, times(1)).findByUsername(USERNAME);
  }
}
