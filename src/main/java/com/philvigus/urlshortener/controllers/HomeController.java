package com.philvigus.urlshortener.controllers;

import com.philvigus.urlshortener.model.User;
import com.philvigus.urlshortener.security.CustomUserDetails;
import com.philvigus.urlshortener.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
  private final UserService userService;

  public HomeController(UserService userService) {
    this.userService = userService;
  }

  @RequestMapping("")
  public String view(Authentication authentication) {
    CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
    User user = userService.findByUsername(userDetails.getUsername());
    System.out.println("###################");
    System.out.println(user);
    return "home";
  }
}
