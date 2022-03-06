package com.philvigus.urlshortener.controllers;

import com.philvigus.urlshortener.model.Url;
import com.philvigus.urlshortener.model.User;
import com.philvigus.urlshortener.services.UrlService;
import com.philvigus.urlshortener.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Controller
public class UrlController {
  private final UrlService urlService;
  private final UserService userService;

  public UrlController(UrlService urlService, UserService userService) {
    this.urlService = urlService;
    this.userService = userService;
  }

  @DeleteMapping("/urls/{id}")
  public String delete(@PathVariable("id") long id) {
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

  @GetMapping("/urls/{id}")
  public String view(
      @AuthenticationPrincipal UserDetails authedUserDetails,
      @PathVariable("id") long id,
      Model model) {
    Optional<Url> url = urlService.findById(id);

    if (!url.isPresent()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    User authedUser = userService.findByUsername(authedUserDetails.getUsername());

    if (url.get().getUser() != authedUser) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }

    model.addAttribute("url", urlService.findById(id).get());

    return "url/show";
  }
}
