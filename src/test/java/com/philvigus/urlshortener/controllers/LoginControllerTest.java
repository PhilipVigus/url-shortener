package com.philvigus.urlshortener.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class LoginControllerTest {
  LoginController controller;

  @BeforeEach
  void setUp() {
    controller = new LoginController();
  }

  @Test
  void view() throws Exception {
    MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    mockMvc.perform(get("/login")).andExpect(status().isOk());
  }
}
