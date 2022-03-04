package com.philvigus.urlshortener.controllers;

import com.philvigus.urlshortener.model.User;
import com.philvigus.urlshortener.services.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest()
@ActiveProfiles("test-unit")
class RegistrationControllerTest {
  @Autowired private WebApplicationContext context;

  @Autowired UserService userService;

  private MockMvc mvc;

  @BeforeEach
  void setUp() {
    mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
  }

  @AfterEach
  void cleanUp() {
    reset(userService);
  }

  @Test
  void view() throws Exception {
    mvc.perform(get("/register")).andExpect(status().isOk());
  }

  @Test
  void create() throws Exception {
    final String USERNAME = "username";
    final String UNENCODED_PASSWORD = "1Password";

    User user = new User();

    ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

    when(userService.findByUsername(USERNAME)).thenReturn(null);

    when(userService.save(any(User.class))).thenReturn(user);

    mvc.perform(
            post("/register")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", USERNAME)
                .param("password", UNENCODED_PASSWORD))
        .andExpect(status().is3xxRedirection())
        .andExpect(MockMvcResultMatchers.view().name("redirect:login"));

    verify(userService, times(1)).save(captor.capture());

    User userToSave = captor.getValue();

    assertEquals(USERNAME, userToSave.getUsername());
  }

  @Test
  @Sql("classpath:createUserWithUrl.sql")
  @Sql(scripts = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  void createWithDuplicateUsername() throws Exception {
    final String USERNAME = "phil";
    final String UNENCODED_PASSWORD = "1Password";

    User user = new User();
    user.setPassword(UNENCODED_PASSWORD);
    user.setUsername(USERNAME);

    when(userService.findByUsername(USERNAME)).thenReturn(user);

    mvc.perform(
            post("/register")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", USERNAME)
                .param("password", UNENCODED_PASSWORD))
        .andExpect(MockMvcResultMatchers.view().name("auth/register"));

    verify(userService, never()).save(any());
  }

  @Test
  void createWithInvalidPassword() throws Exception {
    final String USERNAME = "phil";
    final String UNENCODED_PASSWORD = "password";

    User user = new User();
    user.setPassword(UNENCODED_PASSWORD);
    user.setUsername(USERNAME);

    when(userService.findByUsername(USERNAME)).thenReturn(null);

    mvc.perform(
            post("/register")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", USERNAME)
                .param("password", UNENCODED_PASSWORD))
        .andExpect(MockMvcResultMatchers.view().name("auth/register"));

    verify(userService, never()).save(any());
  }
}
