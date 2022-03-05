package com.philvigus.urlshortener.controllers;

import com.philvigus.urlshortener.model.Url;
import com.philvigus.urlshortener.services.UrlService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UrlRedirectControllerTest {
  UrlRedirectController controller;

  @Mock UrlService urlService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    controller = new UrlRedirectController(urlService);
  }

  @Test
  void redirectToNonExistentShortUrlReturnsNotFound() throws Exception {
    MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

    when(urlService.findByFullUrl("wibble")).thenReturn(Optional.empty());

    mockMvc.perform(get("/wibble")).andExpect(status().isNotFound());
  }

  @Test
  void redirectToExistingShortUrlRedirects() throws Exception {
    MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

    Url url = new Url();
    url.setShortUrl("short");
    url.setFullUrl("long");

    when(urlService.findByShortUrl("short")).thenReturn(Optional.of(url));

    mockMvc.perform(get("/short")).andExpect(status().is3xxRedirection());
  }
}
