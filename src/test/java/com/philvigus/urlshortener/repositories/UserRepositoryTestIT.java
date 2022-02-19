package com.philvigus.urlshortener.repositories;

import com.philvigus.urlshortener.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
class UserRepositoryTestIT {
  @Autowired UserRepository userRepository;

  @Test
  void getUserByUsername() {
    final String USERNAME = "username";

    User user = new User();
    user.setUsername(USERNAME);

    userRepository.save(user);

    List<User> users = userRepository.getUserByUsername(USERNAME);
    User savedUser = users.get(0);

    assertEquals(1, users.size());
    assertNotNull(savedUser.getId());
    assertEquals(USERNAME, savedUser.getUsername());
  }
}
