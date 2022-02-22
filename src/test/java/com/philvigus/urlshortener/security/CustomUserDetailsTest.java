package com.philvigus.urlshortener.security;

import com.philvigus.urlshortener.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomUserDetailsTest {
  private CustomUserDetails customUserDetails;

  @Mock User user;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    customUserDetails = new CustomUserDetails(user);
  }

  @Test
  void getAuthorities() {
    assertNull(customUserDetails.getAuthorities());
  }

  @Test
  void getPassword() {
    final String PASSWORD = "password";

    when(user.getPassword()).thenReturn(PASSWORD);

    assertEquals(PASSWORD, customUserDetails.getPassword());
    verify(user, times(1)).getPassword();
  }

  @Test
  void getUsername() {
    final String USERNAME = "username";

    when(user.getUsername()).thenReturn(USERNAME);

    assertEquals(USERNAME, customUserDetails.getUsername());
    verify(user, times(1)).getUsername();
  }

  @Test
  void isAccountNonExpired() {
    assertTrue(customUserDetails.isAccountNonExpired());
  }

  @Test
  void isAccountNonLocked() {
    assertTrue(customUserDetails.isAccountNonLocked());
  }

  @Test
  void isCredentialsNonExpired() {
    assertTrue(customUserDetails.isCredentialsNonExpired());
  }

  @Test
  void isEnabled() {
    assertTrue(customUserDetails.isEnabled());
  }
}
