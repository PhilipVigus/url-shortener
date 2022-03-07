package com.philvigus.urlshortener.repositories;

import com.philvigus.urlshortener.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@DataJpaTest
@DisplayName("User repository")
class UserRepositoryTest {
  @Autowired UserRepository userRepository;

  @Test
  @DisplayName("findByUsername - finds the user with the given username")
  void findByUsername() {
    final String USERNAME = "username";
    final String PASSWORD = "password";

    final String DIFFERENT_USERNAME = "different";

    User user = new User();
    user.setUsername(USERNAME);
    user.setPassword(PASSWORD);
    userRepository.save(user);

    User differentUser = new User();
    differentUser.setUsername(DIFFERENT_USERNAME);
    differentUser.setPassword(PASSWORD);
    userRepository.save(differentUser);

    User savedUser = userRepository.findByUsername(USERNAME);

    assertEquals(USERNAME, savedUser.getUsername());
  }

  @Test
  @DisplayName("findByUsernameNotFound - if no user is found then returns null")
  void findByUsernameNotFound() {
    final String USERNAME = "username";

    User savedUser = userRepository.findByUsername(USERNAME);

    assertNull(savedUser);
  }
}
