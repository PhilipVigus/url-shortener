package com.philvigus.urlshortener.controllers;

import com.philvigus.urlshortener.model.Url;
import com.philvigus.urlshortener.services.UrlService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("DashboardController")
class DashboardControllerTest {
  @Autowired private WebApplicationContext context;

  @Autowired private UrlService urlService;

  private MockMvc mvc;

  @BeforeEach
  void setUp() {
    mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
  }

  @Test
  @WithMockUser(username = "username")
  @Sql("classpath:createUserWithUrl.sql")
  @Sql(scripts = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  @DisplayName("An authed user can view their dashboard")
  void anAuthedUserCanAccessTheDashboard() throws Exception {
    mvc.perform(get("/dashboard")).andExpect(view().name("dashboard")).andExpect(status().isOk());
  }

  @Test
  @DisplayName("A guest user is redirected to the login screen")
  void aGuestUserCannotAccessTheDashboard() throws Exception {
    mvc.perform(get("/dashboard")).andExpect(status().is3xxRedirection());
  }

  @Test
  @WithMockUser(username = "username")
  @Sql("classpath:createUserWithUrl.sql")
  @Sql(scripts = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  @DisplayName("An authed user can see their URLs")
  void anAuthedUserSeesTheirUrls() throws Exception {
    mvc.perform(get("/dashboard")).andExpect(model().attributeExists("urls"));

    Set<Url> urls = urlService.findAll();

    assertEquals(1, urls.size());
  }

  @Test
  @WithMockUser(username = "username")
  @Sql("classpath:createUserWithoutUrl.sql")
  @Sql(scripts = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  @DisplayName("An authed user with no URLs sees no URLs")
  void anAuthedUserWithNoUrlsSeesNoUrls() throws Exception {
    mvc.perform(get("/dashboard")).andExpect(model().attributeExists("urls"));

    Set<Url> urls = urlService.findAll();

    assertEquals(0, urls.size());
  }
}
