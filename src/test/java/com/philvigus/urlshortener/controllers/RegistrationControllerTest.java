package com.philvigus.urlshortener.controllers;

import com.philvigus.urlshortener.model.User;
import com.philvigus.urlshortener.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest()
@ActiveProfiles("test")
@DisplayName("RegistrationController")
class RegistrationControllerTest {
  @Autowired private WebApplicationContext context;

  @Autowired private UserService userService;

  private MockMvc mvc;

  @BeforeEach
  void setUp() {
    mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
  }

  @Test
  @DisplayName("Can view the registration page")
  void view() throws Exception {
    mvc.perform(get("/register")).andExpect(status().isOk());
  }

  @Test
  @DisplayName("Can register a new user")
  @Sql(scripts = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  void create() throws Exception {
    final String USERNAME = "username";
    final String UNENCODED_PASSWORD = "1Password";

    mvc.perform(
            post("/register")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", USERNAME)
                .param("password", UNENCODED_PASSWORD))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("login"));

    User createdUser = userService.findByUsername(USERNAME);

    assertNotNull(createdUser);
  }

  @Test
  @Sql("classpath:createUserWithoutUrl.sql")
  @Sql(scripts = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  @DisplayName("Cannot register a new user with a duplicate username")
  void createWithDuplicateUsername() throws Exception {
    final String USERNAME = "username";
    final String UNENCODED_PASSWORD = "1Password";

    mvc.perform(
            post("/register")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", USERNAME)
                .param("password", UNENCODED_PASSWORD))
        .andExpect(MockMvcResultMatchers.view().name("auth/register"))
        .andExpect(model().attributeHasFieldErrors("user", "username"));
  }

  @Test
  @DisplayName("Cannot register a new user with an invalid password")
  void createWithInvalidPassword() throws Exception {
    final String USERNAME = "username";
    final String UNENCODED_PASSWORD = "invalidpassword";

    mvc.perform(
            post("/register")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", USERNAME)
                .param("password", UNENCODED_PASSWORD))
        .andExpect(MockMvcResultMatchers.view().name("auth/register"))
        .andExpect(model().attributeHasFieldErrors("user", "password"));
  }
}
