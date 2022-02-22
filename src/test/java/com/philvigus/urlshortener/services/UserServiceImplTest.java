package com.philvigus.urlshortener.services;

import com.philvigus.urlshortener.model.User;
import com.philvigus.urlshortener.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class UserServiceImplTest {
  private UserServiceImpl userService;

  @Mock UserRepository userRepository;

  public @BeforeEach void setUp() {
    MockitoAnnotations.openMocks(this);

    userService = new UserServiceImpl(userRepository);
  }

  @Test
  void save() {
    User user = new User();

    userService.save(user);

    verify(userRepository, times(1)).save(user);
  }

  @Test
  void findByUsername() {
    final String USERNAME = "username";

    userService.findByUsername(USERNAME);

    verify(userRepository, times(1)).findByUsername(USERNAME);
  }
}
