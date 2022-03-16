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
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Optional;
import java.util.Set;

@Controller
public class UrlController {
  private final UrlService urlService;
  private final UserService userService;

  public UrlController(final UrlService urlService, final UserService userService) {
    this.urlService = urlService;
    this.userService = userService;
  }

  @GetMapping("/urls")
  public String index(
      final @AuthenticationPrincipal UserDetails authedUserDetails, final Model model) {
    final User authedUser = userService.findByUsername(authedUserDetails.getUsername());

    final Set<Url> urls = authedUser.getUrls();

    model.addAttribute("urls", urls);

    return "urls/index";
  }

  @GetMapping("/urls/{id}")
  public String update(
      final @AuthenticationPrincipal UserDetails authedUserDetails,
      final @PathVariable("id") long id,
      final Model model) {
    final Optional<Url> url = urlService.findById(id);

    if (!url.isPresent()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    final User authedUser = userService.findByUsername(authedUserDetails.getUsername());

    if (!url.get().getUser().equals(authedUser)) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }

    model.addAttribute("url", urlService.findById(id).get());

    return "urls/update";
  }

  @GetMapping("/urls/add")
  public String add(final @ModelAttribute Url url, final Model model) {

    model.addAttribute("url", url);

    return "urls/add";
  }

  @DeleteMapping("/urls/{id}")
  public String delete(
      final @AuthenticationPrincipal UserDetails authedUserDetails,
      final @PathVariable("id") long id) {
    final Optional<Url> urlToDelete = urlService.findById(id);

    if (!urlToDelete.isPresent()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    final User authedUser = userService.findByUsername(authedUserDetails.getUsername());

    if (!urlToDelete.get().getUser().equals(authedUser)) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }

    urlService.deleteById(id);

    return "redirect:/urls";
  }

  @PostMapping("/urls")
  public String create(
      final @AuthenticationPrincipal UserDetails authedUserDetails,
      final @Valid @ModelAttribute Url url,
      BindingResult bindingResult) {

    bindingResult = convertGlobalErrors(bindingResult);

    if (bindingResult.hasFieldErrors()) {
      return "urls/add";
    }

    final User authedUser = userService.findByUsername(authedUserDetails.getUsername());

    urlService.save(url, authedUser);

    return "redirect:/urls";
  }

  @PutMapping("/urls/{id}")
  public String update(
      final @AuthenticationPrincipal UserDetails authedUserDetails,
      final @PathVariable("id") long id,
      final @Valid @ModelAttribute Url url,
      BindingResult bindingResult) {
    bindingResult = convertGlobalErrors(bindingResult);

    if (bindingResult.hasFieldErrors()) {
      return "urls/update";
    }

    final Optional<Url> urlToUpdate = urlService.findById(id);

    if (!urlToUpdate.isPresent()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    final User authedUser = userService.findByUsername(authedUserDetails.getUsername());

    if (!urlToUpdate.get().getUser().equals(authedUser)) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }

    urlService.update(url, authedUser);

    return "redirect:/urls";
  }

  private BindingResult convertGlobalErrors(final BindingResult bindingResult) {
    if (bindingResult.getGlobalErrors().size() > 0) {
      bindingResult.addError(new FieldError("url", "shortUrl", "Short URL already in use"));
    }

    return bindingResult;
  }
}
