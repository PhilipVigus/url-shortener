package com.philvigus.urlshortener.controllers;

import com.philvigus.urlshortener.model.User;
import com.philvigus.urlshortener.services.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
  private final UserService userService;

  public HomeController(UserService userService) {
    this.userService = userService;
  }

  @RequestMapping("")
  public String view(@AuthenticationPrincipal User activeUser) {
    System.out.println("###################");
    System.out.println(activeUser);
    return "home";
  }
}
