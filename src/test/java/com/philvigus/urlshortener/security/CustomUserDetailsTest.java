package com.philvigus.urlshortener.security;

import com.philvigus.urlshortener.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("CustomUserDetails")
class CustomUserDetailsTest {
  private CustomUserDetails customUserDetails;

  @Mock User user;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    customUserDetails = new CustomUserDetails(user);
  }

  @Test
  @DisplayName("getAuthorities - returns null")
  void getAuthorities() {
    assertNull(customUserDetails.getAuthorities());
  }

  @Test
  @DisplayName("getPassword - calls getPassword on the wrapped User")
  void getPassword() {
    final String PASSWORD = "password";

    when(user.getPassword()).thenReturn(PASSWORD);

    assertEquals(PASSWORD, customUserDetails.getPassword());
    verify(user, times(1)).getPassword();
  }

  @Test
  @DisplayName("getUsername - calls getUsername on the wrapped User")
  void getUsername() {
    final String USERNAME = "username";

    when(user.getUsername()).thenReturn(USERNAME);

    assertEquals(USERNAME, customUserDetails.getUsername());
    verify(user, times(1)).getUsername();
  }

  @Test
  @DisplayName("isAccountNonExpired - returns true")
  void isAccountNonExpired() {
    assertTrue(customUserDetails.isAccountNonExpired());
  }

  @Test
  @DisplayName("isAccountNonLocked - returns true")
  void isAccountNonLocked() {
    assertTrue(customUserDetails.isAccountNonLocked());
  }

  @Test
  @DisplayName("isCredentialsNonExpired - returns true")
  void isCredentialsNonExpired() {
    assertTrue(customUserDetails.isCredentialsNonExpired());
  }

  @Test
  @DisplayName("isEnabled - returns true")
  void isEnabled() {
    assertTrue(customUserDetails.isEnabled());
  }
}
