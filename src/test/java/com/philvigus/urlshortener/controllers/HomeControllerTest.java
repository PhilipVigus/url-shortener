package com.philvigus.urlshortener.controllers;

import com.philvigus.urlshortener.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class HomeControllerTest {
  HomeController controller;

  @Mock UserService userService;

  @BeforeEach
  void setUp() {
    controller = new HomeController(userService);
  }

  @Test
  void view() throws Exception {
    MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

    mockMvc.perform(get("/")).andExpect(status().isOk());
  }
}
