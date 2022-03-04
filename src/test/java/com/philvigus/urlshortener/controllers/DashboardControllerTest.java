package com.philvigus.urlshortener.controllers;

import com.philvigus.urlshortener.model.Url;
import com.philvigus.urlshortener.repositories.UrlRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
class DashboardControllerTest {
  @Autowired private WebApplicationContext context;

  @Autowired private UrlRepository urlRepository;

  private MockMvc mvc;

  @BeforeEach
  void setUp() {
    mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
  }

  @WithMockUser(username = "phil", password = "password")
  @Sql("classpath:createUserWithUrl.sql")
  @Sql(scripts = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  @Test
  public void anAuthedUserCanAccessTheHomepage() throws Exception {
    mvc.perform(get("/dashboard")).andExpect(view().name("dashboard")).andExpect(status().isOk());
  }

  @Test
  public void aGuestUserCannotAccessTheHomepage() throws Exception {
    mvc.perform(get("/dashboard")).andExpect(status().is3xxRedirection());
  }

  @WithMockUser(username = "phil", password = "password")
  @Sql("classpath:createUserWithUrl.sql")
  @Sql(scripts = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  @Test
  public void AnAuthedUserSeesTheirUrls() throws Exception {
    List<Url> urls = (List<Url>) urlRepository.findAll();

    Url url = urls.stream().findFirst().get();

    mvc.perform(get("/dashboard")).andExpect(model().attributeExists("urls"));
  }
}
