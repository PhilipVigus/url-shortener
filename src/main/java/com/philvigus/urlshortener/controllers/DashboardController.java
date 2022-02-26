package com.philvigus.urlshortener.controllers;

import com.philvigus.urlshortener.model.Url;
import com.philvigus.urlshortener.model.User;
import com.philvigus.urlshortener.services.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Set;

@Controller
public class DashboardController {
  private final UserService userService;

  public DashboardController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/dashboard")
  public String view(@AuthenticationPrincipal UserDetails authedUserDetails, Model model) {
    User authedUser = userService.findByUsername(authedUserDetails.getUsername());

        Set<Url> urls = authedUser.getUrls();

        model.addAttribute("urls", urls);

    return "dashboard";
  }
}
