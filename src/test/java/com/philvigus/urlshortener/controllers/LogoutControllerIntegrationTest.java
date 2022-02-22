package com.philvigus.urlshortener.controllers;

import com.philvigus.urlshortener.model.User;
import com.philvigus.urlshortener.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class LogoutControllerIntegrationTest {
  @Autowired private WebApplicationContext context;

  @Autowired private UserRepository userRepository;

  private MockMvc mvc;

  @BeforeEach
  void setUp() {
    mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
  }

  @Test
  public void anAuthedUserCanLogOut() throws Exception {
    final String USERNAME = "username";
    final String PASSWORD = "password";

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    User user = new User();
    user.setPassword(encoder.encode(PASSWORD));
    user.setUsername(USERNAME);

    userRepository.save(user);

    mvc.perform(formLogin("/login").user(USERNAME).password(PASSWORD))
        .andExpect(status().is3xxRedirection())
        .andExpect(authenticated());
    ;

    mvc.perform(formLogin("/logout"))
        .andExpect(status().is3xxRedirection())
        .andExpect(unauthenticated());
    ;
  }
}
