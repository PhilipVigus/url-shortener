package com.philvigus.urlshortener.controllers;

import com.philvigus.urlshortener.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
  private final UserService userService;

  public HomeController(UserService userService) {
    this.userService = userService;
  }

  @RequestMapping("")
  public String view() {
    return "home";
  }
}
