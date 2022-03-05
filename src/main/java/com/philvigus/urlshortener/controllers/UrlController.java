package com.philvigus.urlshortener.controllers;

import com.philvigus.urlshortener.model.Url;
import com.philvigus.urlshortener.model.User;
import com.philvigus.urlshortener.services.UrlService;
import com.philvigus.urlshortener.services.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UrlController {
  private final UrlService urlService;
  private final UserService userService;

  public UrlController(UrlService urlService, UserService userService) {
    this.urlService = urlService;
    this.userService = userService;
  }

  @DeleteMapping("/urls/{id}")
  public String delete(@PathVariable("id") long id, Model model) {
    urlService.deleteById(id);

    return "redirect:/dashboard";
  }

  @PostMapping("/urls")
  public String create(
      @AuthenticationPrincipal UserDetails authedUserDetails, @ModelAttribute Url url) {
    User authedUser = userService.findByUsername(authedUserDetails.getUsername());

    urlService.save(url, authedUser);

    return "redirect:/dashboard";
  }
}
