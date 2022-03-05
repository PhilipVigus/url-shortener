package com.philvigus.urlshortener.controllers;

import com.philvigus.urlshortener.model.Url;
import com.philvigus.urlshortener.services.UrlService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
class DashboardControllerTest {
  @Autowired private WebApplicationContext context;

  @Autowired private UrlService urlService;

  private MockMvc mvc;

  @BeforeEach
  void setUp() {
    mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
  }

  @WithMockUser(username = "phil", password = "password")
  @Sql("classpath:createUserWithUrl.sql")
  @Sql(scripts = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  @Test
  public void anAuthedUserCanAccessTheDashboard() throws Exception {

    mvc.perform(get("/dashboard")).andExpect(view().name("dashboard")).andExpect(status().isOk());
  }

  @Test
  public void aGuestUserCannotAccessTheDashboard() throws Exception {
    mvc.perform(get("/dashboard")).andExpect(status().is3xxRedirection());
  }

  @WithMockUser(username = "phil", password = "password")
  @Sql("classpath:createUserWithUrl.sql")
  @Sql(scripts = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  @Test
  public void AnAuthedUserSeesTheirUrls() throws Exception {
    mvc.perform(get("/dashboard")).andExpect(model().attributeExists("urls"));

    Set<Url> urls = urlService.findAll();

    assertEquals(1, urls.size());
  }

  @WithMockUser(username = "phil", password = "password")
  @Sql("classpath:createUserWithoutUrl.sql")
  @Sql(scripts = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  @Test
  public void AnAuthedUserWithNoUrlsSeesNoUrls() throws Exception {
    mvc.perform(get("/dashboard")).andExpect(model().attributeExists("urls"));

    Set<Url> urls = urlService.findAll();

    assertEquals(0, urls.size());
  }

  @WithMockUser(username = "phil", password = "password")
  @Sql("classpath:createUserWithoutUrl.sql")
  @Sql(scripts = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  @Test
  public void AnAuthedUserCanCreateAUrl() throws Exception {
    final String FULL_URL = "https://www.google.com";
    mvc.perform(
            post("/dashboard")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("fullUrl", FULL_URL))
        .andExpect(status().is3xxRedirection())
        .andExpect(MockMvcResultMatchers.view().name("redirect:/dashboard"));

    Set<Url> urls = urlService.findAll();

    assertEquals(1, urls.size());

    Url url = urls.stream().findFirst().get();

    assertEquals(FULL_URL, url.getFullUrl());
  }
}
