package com.philvigus.urlshortener.repositories;

import com.philvigus.urlshortener.model.User;
import com.philvigus.urlshortener.services.UserServiceImpl;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class UserRepositoryTest {
  @Autowired UserRepository userRepository;

  @InjectMocks UserServiceImpl userService;

  @Disabled
  @Test
  void findByUsername() {
    final String USERNAME = "Steve";
    final String PASSWORD = "1Password";

    when(userService.findByUsername(USERNAME)).thenReturn(null);

    User user = new User();
    user.setUsername(USERNAME);
    user.setPassword(PASSWORD);

    userRepository.save(user);

    User savedUser = userRepository.findByUsername(USERNAME);

    assertNotNull(savedUser.getId());
    assertEquals(USERNAME, savedUser.getUsername());
  }

  @Test
  void findByUsernameNotFound() {
    final String USERNAME = "username";

    User savedUser = userRepository.findByUsername(USERNAME);

    assertNull(savedUser);
  }
}
