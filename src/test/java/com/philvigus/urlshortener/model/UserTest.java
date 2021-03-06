package com.philvigus.urlshortener.model;

import com.philvigus.urlshortener.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
@ActiveProfiles("test")
@DisplayName("User model")
class UserTest {
  @Autowired UserRepository userRepository;

  User user;

  @BeforeEach
  void setUp() {
    user = new User();
  }

  @Test
  @DisplayName("setId - ID correctly set")
  void setId() {
    final Long ID = 1L;

    user.setId(ID);

    assertEquals(ID, user.getId());
  }

  @Test
  @DisplayName("setUsername - username correctly set")
  void setUsername() {
    final String USERNAME = "username";

    user.setUsername(USERNAME);

    assertEquals(USERNAME, user.getUsername());
  }

  @Test
  @DisplayName("setPassword - password correctly set")
  void setPassword() {
    final String PASSWORD = "password";

    user.setPassword(PASSWORD);

    assertEquals(PASSWORD, user.getPassword());
  }

  @Test
  @DisplayName("getUrls - urls correctly retrieved")
  @Sql("classpath:createUserWithUrl.sql")
  @Sql(scripts = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  void getUrls() {
    User user = userRepository.findByUsername("username");

    Set<Url> urls = user.getUrls();
    Url firstUrl = urls.stream().findFirst().orElse(null);

    assertEquals(1, urls.size());
    assertNotNull(firstUrl);
    assertEquals("short", firstUrl.getShortUrl());
    assertEquals("full", firstUrl.getFullUrl());
  }
}
