package com.philvigus.urlshortener.controllers;

import com.philvigus.urlshortener.model.User;
import com.philvigus.urlshortener.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class RegistrationControllerTest {
  RegistrationController controller;

  @Mock UserService userService;
  @Mock BCryptPasswordEncoder passwordEncoder;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    controller = new RegistrationController(userService, passwordEncoder);
  }

  @Test
  void view() throws Exception {
    MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

    mockMvc.perform(get("/register")).andExpect(status().isOk());
  }

  @Test
  void create() throws Exception {
    final String USERNAME = "username";
    final String UNENCODED_PASSWORD = "unencoded password";
    final String ENCODED_PASSWORD = "encoded password";

    MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

    User user = new User();

    ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

    when(passwordEncoder.encode(UNENCODED_PASSWORD)).thenReturn(ENCODED_PASSWORD);
    when(userService.save(any(User.class))).thenReturn(user);

    mockMvc
        .perform(
            post("/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", USERNAME)
                .param("password", UNENCODED_PASSWORD))
        .andExpect(status().is3xxRedirection())
        .andExpect(MockMvcResultMatchers.view().name("redirect:login"));

    verify(passwordEncoder, times(1)).encode(UNENCODED_PASSWORD);
    verify(userService, times(1)).save(captor.capture());

    User userToSave = captor.getValue();

    assertEquals(USERNAME, userToSave.getUsername());
    assertEquals(ENCODED_PASSWORD, userToSave.getPassword());
  }
}
