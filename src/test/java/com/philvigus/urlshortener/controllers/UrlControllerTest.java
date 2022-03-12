package com.philvigus.urlshortener.controllers;

import com.philvigus.urlshortener.model.Url;
import com.philvigus.urlshortener.services.UrlService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("UrlController")
class UrlControllerTest {
  @Autowired private WebApplicationContext context;

  @Autowired private UrlService urlService;

  private MockMvc mvc;

  @BeforeEach
  void setUp() {
    mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
  }

  @Test
  @WithMockUser(username = "username", password = "password")
  @Sql("classpath:createUserWithUrl.sql")
  @Sql(scripts = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  @DisplayName("An authed user can view their URL")
  void anAuthedUserCanAccessTheirUrl() throws Exception {
    Set<Url> urls = urlService.findAll();

    Url url = urls.stream().findFirst().get();

    mvc.perform(get("/urls/" + url.getId()))
        .andExpect(view().name("url/show"))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("An guest user cannot view a URL")
  void aGuestUserCannotViewAUrl() throws Exception {
    mvc.perform(get("/urls/1")).andExpect(status().is3xxRedirection());
  }

  @Test
  @WithMockUser(username = "su", password = "password")
  @Sql("classpath:createUserWithUrl.sql")
  @Sql(scripts = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  @DisplayName("An authed user cannot view another user's URL")
  void anAuthedUserCannotAccessAnotherUsersUrl() throws Exception {
    Set<Url> urls = urlService.findAll();

    Url url = urls.stream().findFirst().get();

    mvc.perform(get("/urls/" + url.getId())).andExpect(status().isForbidden());
  }

  @Test
  @WithMockUser(username = "phil", password = "password")
  @Sql("classpath:createUserWithoutUrl.sql")
  @Sql(scripts = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  @DisplayName("An authed user cannot view a URL that doesn't exist")
  void anAuthedUserCannotAccessAUrlThatDoesntExist() throws Exception {
    mvc.perform(get("/urls/1")).andExpect(status().isNotFound());
  }

  @Test
  @WithMockUser(username = "phil", password = "password")
  @Sql("classpath:createUserWithUrl.sql")
  @Sql(scripts = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  @DisplayName("An authed user can delete a URL")
  void anAuthedUserCanDeleteAUrl() throws Exception {
    final String FULL_URL = "full";

    Set<Url> urls = urlService.findByFullUrl(FULL_URL);
    Url url = urls.stream().findFirst().get();

    mvc.perform(
            delete("/urls/" + url.getId())
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
        .andExpect(status().is3xxRedirection())
        .andExpect(MockMvcResultMatchers.view().name("redirect:/dashboard"));

    Set<Url> remainingUrls = urlService.findAll();

    assertEquals(0, remainingUrls.size());
  }

  @Test
  @WithMockUser(username = "username", password = "password")
  @DisplayName("An authed user can view the add URL page")
  void anAuthedUserCanViewTheAddUrlPage() throws Exception {

    mvc.perform(get("/urls/add")).andExpect(view().name("url/add")).andExpect(status().isOk());
  }

  @Test
  @DisplayName("An guest user cannot view a URL")
  void aGuestUserCannotViewTheAddUrlPage() throws Exception {
    mvc.perform(get("/urls/add")).andExpect(status().is3xxRedirection());
  }

  @Test
  @WithMockUser(username = "username", password = "password")
  @Sql("classpath:createUserWithoutUrl.sql")
  @Sql(scripts = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  @DisplayName("An authed user can create a URL with no short URL specified")
  void anAuthedUserCanCreateAUrlWIthNoShortUrlSpecified() throws Exception {
    final String FULL_URL = "https://www.google.com";

    mvc.perform(
        post("/urls/")
            .with(csrf())
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("fullUrl", FULL_URL)
            .param("shortUrl", ""));

    Set<Url> urls = urlService.findAll();

    assertEquals(1, urls.size());

    Url url = urls.stream().findFirst().get();

    assertEquals(FULL_URL, url.getFullUrl());
  }

  @Test
  @WithMockUser(username = "username", password = "password")
  @Sql("classpath:createUserWithoutUrl.sql")
  @Sql(scripts = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  @DisplayName("An authed user can create a URL with a custom short URL")
  void anAuthedUserCanCreateAUrlWIthACustomShortUrl() throws Exception {
    final String FULL_URL = "https://www.google.com";
    final String SHORT_URL = "custom";

    mvc.perform(
        post("/urls/")
            .with(csrf())
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("fullUrl", FULL_URL)
            .param("shortUrl", SHORT_URL));

    Set<Url> urls = urlService.findAll();

    assertEquals(1, urls.size());

    Url url = urls.stream().findFirst().get();

    assertEquals(FULL_URL, url.getFullUrl());
    assertEquals(SHORT_URL, url.getShortUrl());
  }

  @Test
  @WithMockUser(username = "username", password = "password")
  @Sql("classpath:createUserWithUrl.sql")
  @Sql(scripts = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  @DisplayName("An authed user cannot create a URL with a duplicate custom short URL")
  void anAuthedUserCannotCreateAUrlWIthADuplicateCustomShortUrl() throws Exception {
    final String FULL_URL = "https://www.google.com";
    final String SHORT_URL = "short";

    mvc.perform(
            post("/urls/")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("fullUrl", FULL_URL)
                .param("shortUrl", SHORT_URL))
        .andExpect(MockMvcResultMatchers.view().name("url/add"))
        .andExpect(model().attributeHasFieldErrors("url", "shortUrl"));
    ;

    Set<Url> urls = urlService.findAll();

    assertEquals(1, urls.size());
  }
}
