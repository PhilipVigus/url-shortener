package com.philvigus.urlshortener.controllers;

import com.philvigus.urlshortener.model.Url;
import com.philvigus.urlshortener.model.User;
import com.philvigus.urlshortener.services.UrlService;
import com.philvigus.urlshortener.services.UserService;
import com.philvigus.urlshortener.validation.UniqueShortUrlValidator;
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
import static org.junit.jupiter.api.Assertions.assertNotEquals;
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

  @Autowired private UserService userService;

  private MockMvc mvc;

  @BeforeEach
  void setUp() {
    mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
  }

  @Test
  @DisplayName("A guest user is redirected to the login screen")
  void aGuestUserCannotViewAnyUrls() throws Exception {
    mvc.perform(get("/urls")).andExpect(status().is3xxRedirection());
  }

  @Test
  @WithMockUser(username = "username")
  @Sql("classpath:createUserWithUrl.sql")
  @Sql(scripts = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  @DisplayName("An authed user can see their URLs")
  void anAuthedUserSeesTheirUrls() throws Exception {
    mvc.perform(get("/urls"))
        .andExpect(model().attributeExists("urls"))
        .andExpect(view().name("urls/index"))
        .andExpect(status().isOk());

    Set<Url> urls = urlService.findAll();

    assertEquals(1, urls.size());
  }

  @Test
  @WithMockUser(username = "username")
  @Sql("classpath:createUserWithoutUrl.sql")
  @Sql(scripts = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  @DisplayName("An authed user with no URLs sees no URLs")
  void anAuthedUserWithNoUrlsSeesNoUrls() throws Exception {
    mvc.perform(get("/urls")).andExpect(model().attributeExists("urls"));

    Set<Url> urls = urlService.findAll();

    assertEquals(0, urls.size());
  }

  @Test
  @WithMockUser(username = "username")
  @Sql("classpath:createUserWithUrl.sql")
  @Sql(scripts = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  @DisplayName("An authed user can view the edit URL page")
  void anAuthedUserCanViewTheEditUrlPage() throws Exception {
    Set<Url> urls = urlService.findAll();
    Url url = urls.stream().findFirst().get();

    mvc.perform(get("/urls/" + url.getId()))
        .andExpect(view().name("urls/update"))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("A guest user cannot view a URL")
  void aGuestUserCannotViewAUrl() throws Exception {
    mvc.perform(get("/urls/1")).andExpect(status().is3xxRedirection());
  }

  @Test
  @WithMockUser(username = "su")
  @Sql("classpath:createUserWithUrl.sql")
  @Sql(scripts = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  @DisplayName("An authed user cannot view someone else's URL")
  void anAuthedUserCannotViewSomeoneElsesUrl() throws Exception {
    Set<Url> urls = urlService.findAll();
    Url url = urls.stream().findFirst().get();

    mvc.perform(get("/urls/" + url.getId())).andExpect(status().isForbidden());
  }

  @Test
  @WithMockUser(username = "phil")
  @Sql("classpath:createUserWithoutUrl.sql")
  @Sql(scripts = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  @DisplayName("An authed user cannot view a URL that doesn't exist")
  void anAuthedUserCannotViewAUrlThatDoesntExist() throws Exception {
    mvc.perform(get("/urls/1")).andExpect(status().isNotFound());
  }

  @Test
  @WithMockUser(username = "username")
  @Sql("classpath:createUserWithUrl.sql")
  @Sql(scripts = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  @DisplayName("An authed user can delete a URL")
  void anAuthedUserCanDeleteAUrl() throws Exception {
    Set<Url> urls = urlService.findAll();
    Url url = urls.stream().findFirst().get();

    mvc.perform(
            delete("/urls/" + url.getId())
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/urls"));

    Set<Url> remainingUrls = urlService.findAll();

    assertEquals(0, remainingUrls.size());
  }

  @Test
  @WithMockUser(username = "su")
  @Sql("classpath:createUserWithUrl.sql")
  @Sql(scripts = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  @DisplayName("An authed user cannot delete someone else's URL")
  void anAuthedUserCannotDeleteSomeoneElsesUrl() throws Exception {
    Set<Url> urls = urlService.findAll();
    Url url = urls.stream().findFirst().get();

    mvc.perform(
            delete("/urls/" + url.getId())
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
        .andExpect(status().isForbidden());

    Set<Url> remainingUrls = urlService.findAll();

    assertEquals(1, remainingUrls.size());
  }

  @Test
  @WithMockUser(username = "username")
  @Sql("classpath:createUserWithoutUrl.sql")
  @Sql(scripts = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  @DisplayName("An authed user cannot delete a URL that doesn't exist")
  void anAuthedUserCannotDeleteAUrlThatDoesntExist() throws Exception {
    mvc.perform(delete("/urls/1").with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED))
        .andExpect(status().isNotFound());
  }

  @Test
  @WithMockUser(username = "username")
  @DisplayName("An authed user can view the add URL page")
  void anAuthedUserCanViewTheAddUrlPage() throws Exception {

    mvc.perform(get("/urls/add")).andExpect(view().name("urls/add")).andExpect(status().isOk());
  }

  @Test
  @DisplayName("An guest user cannot view a URL")
  void aGuestUserCannotViewTheAddUrlPage() throws Exception {
    mvc.perform(get("/urls/add")).andExpect(status().is3xxRedirection());
  }

  @Test
  @WithMockUser(username = "username")
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
  @WithMockUser(username = "username")
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
  @WithMockUser(username = "username")
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
        .andExpect(MockMvcResultMatchers.view().name("urls/add"))
        .andExpect(model().attributeHasFieldErrors("url", "shortUrl"));

    Set<Url> urls = urlService.findAll();

    assertEquals(1, urls.size());
  }

  @Test
  @WithMockUser(username = "username")
  @Sql("classpath:createUserWithUrl.sql")
  @Sql(scripts = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  @DisplayName("An authed user can edit their URL")
  void anAuthedUserCanEditTheirUrl() throws Exception {
    final String FULL_URL = "https://www.edited.com";
    final String SHORT_URL = "edited";

    Set<Url> urls = urlService.findAll();
    Url url = urls.stream().findFirst().get();

    mvc.perform(
            put("/urls/" + url.getId())
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("fullUrl", FULL_URL)
                .param("shortUrl", SHORT_URL))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/urls"));

    Url editedUrl = urlService.findById(url.getId()).get();

    assertEquals(FULL_URL, editedUrl.getFullUrl());
    assertEquals(SHORT_URL, editedUrl.getShortUrl());
  }

  @Test
  @WithMockUser(username = "Su")
  @Sql("classpath:createUserWithUrl.sql")
  @Sql(scripts = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  @DisplayName("An authed user cannot edit someone else's URL")
  void anAuthedUserCannotEditSomeoneElsesUrl() throws Exception {
    final String FULL_URL = "https://www.edited.com";
    final String SHORT_URL = "edited";

    Set<Url> urls = urlService.findAll();
    Url url = urls.stream().findFirst().get();

    mvc.perform(
            put("/urls/" + url.getId())
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("fullUrl", FULL_URL)
                .param("shortUrl", SHORT_URL))
        .andExpect(status().isForbidden());

    Url editedUrl = urlService.findById(url.getId()).get();

    assertNotEquals(FULL_URL, editedUrl.getFullUrl());
    assertNotEquals(SHORT_URL, editedUrl.getShortUrl());
  }

  @Test
  @WithMockUser(username = "Su")
  @Sql("classpath:createUserWithoutUrl.sql")
  @Sql(scripts = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  @DisplayName("An authed user can edit their URL")
  void anAuthedUserCannotEditAUrlThatDoesntExist() throws Exception {
    final String FULL_URL = "https://www.edited.com";
    final String SHORT_URL = "edited";

    mvc.perform(
            put("/urls/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("fullUrl", FULL_URL)
                .param("shortUrl", SHORT_URL))
        .andExpect(status().isNotFound());
  }

  @Test
  @WithMockUser(username = "su")
  @Sql("classpath:createUserWithUrl.sql")
  @Sql(scripts = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  @DisplayName("An authed user can edit their URL")
  void anAuthedUserCannotEditTheirUrlToUseAnExistingShortUrl() throws Exception {
    final String FULL_URL = "https://www.edited.com";
    final String SHORT_URL = "new";
    User user = userService.findByUsername("username");

    Url url = new Url();
    url.setFullUrl(FULL_URL);
    url.setShortUrl(SHORT_URL);

    urlService.save(url, user);

    mvc.perform(
            put("/urls/" + url.getId())
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("fullUrl", FULL_URL)
                .param("shortUrl", "short"))
        .andExpect(status().isOk())
        .andExpect(view().name("urls/update"))
        .andExpect(model().hasErrors());
  }

  @Test
  @WithMockUser(username = "username")
  @Sql("classpath:createUserWithUrl.sql")
  @Sql(scripts = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  @DisplayName("An authed user cannot create a URL using a short URL in the forbidden list")
  void anAuthedUserCannotCreateAUrlUsingAShortUrlInTheForbiddenList() throws Exception {
    final String FULL_URL = "https://www.google.com";
    final String SHORT_URL =
        UniqueShortUrlValidator.FORBIDDEN_SHORT_URLS.stream().findFirst().get();

    mvc.perform(
            post("/urls/")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("fullUrl", FULL_URL)
                .param("shortUrl", SHORT_URL))
        .andExpect(MockMvcResultMatchers.view().name("urls/add"))
        .andExpect(model().attributeHasFieldErrors("url", "shortUrl"));

    Set<Url> urls = urlService.findAll();

    assertEquals(1, urls.size());
  }
}
