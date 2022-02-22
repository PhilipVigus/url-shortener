package com.philvigus.urlshortener.repositories;

import com.philvigus.urlshortener.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
class UserRepositoryTestIntegrationTest {
  @Autowired UserRepository userRepository;

  @Test
  void findByUsername() {
    final String USERNAME = "username";
    final String PASSWORD = "password";

    User user = new User();
    user.setUsername(USERNAME);
    user.setPassword(PASSWORD);

    userRepository.save(user);

    User savedUser = userRepository.findByUsername(USERNAME);

    assertNotNull(savedUser.getId());
    assertEquals(USERNAME, savedUser.getUsername());
  }
}
