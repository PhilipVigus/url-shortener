package com.philvigus.urlshortener.security;

import com.philvigus.urlshortener.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class CustomUserDetailsServiceIntegrationTest {
  private CustomUserDetailsService customUserDetailsService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    customUserDetailsService = new CustomUserDetailsService();
  }

  @Test
  void loadUserByUsername() {
    User user = new User();
    UserDetails savedUser = customUserDetailsService.loadUserByUsername("test");
  }
}
