package com.philvigus.urlshortener.controllers;

import com.philvigus.urlshortener.model.Url;
import com.philvigus.urlshortener.model.User;
import com.philvigus.urlshortener.services.UrlService;
import com.philvigus.urlshortener.services.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Set;

@Controller
public class DashboardController {
  private final UserService userService;
  private final UrlService urlService;

  public DashboardController(UserService userService, UrlService urlService) {
    this.userService = userService;
    this.urlService = urlService;
  }

  @PostMapping("/dashboard")
  public String create(
      @AuthenticationPrincipal UserDetails authedUserDetails, @ModelAttribute Url url) {
    User authedUser = userService.findByUsername(authedUserDetails.getUsername());

    Url savedUrl = urlService.save(url, authedUser);

    return "redirect:/dashboard";
  }

  @GetMapping("/dashboard")
  public String view(
      @AuthenticationPrincipal UserDetails authedUserDetails,
      @ModelAttribute Url url,
      Model model) {
    User authedUser = userService.findByUsername(authedUserDetails.getUsername());

    Set<Url> urls = authedUser.getUrls();

    model.addAttribute("urls", urls);
    model.addAttribute("url", url);

    return "dashboard";
  }
}
