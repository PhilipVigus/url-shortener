package com.philvigus.urlshortener.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
  User user;

  @BeforeEach
  void setUp() {
    user = new User();
  }

  @Test
  void setId() {
    final Long ID = 1L;

    user.setId(ID);

    assertEquals(ID, user.getId());
  }

  @Test
  void setUsername() {
    final String USERNAME = "username";

    user.setUsername(USERNAME);

    assertEquals(USERNAME, user.getUsername());
  }

  @Test
  void setPassword() {
    final String PASSWORD = "password";

    user.setPassword(PASSWORD);

    assertEquals(PASSWORD, user.getPassword());
  }

  @Test
  void setRole() {
    final String ROLE = "role";

    user.setRole(ROLE);

    assertEquals(ROLE, user.getRole());
  }

  @Test
  void setEnabled() {
    user.setEnabled(true);

    assertTrue(user.isEnabled());
  }
}
