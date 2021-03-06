package com.philvigus.urlshortener.controllers;

import com.philvigus.urlshortener.model.User;
import com.philvigus.urlshortener.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class RegistrationController {
  private final UserService userService;

  @Autowired
  public RegistrationController(final UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/register")
  public String view(final Model model) {
    model.addAttribute("user", new User());
    return "auth/register";
  }

  @PostMapping("/register")
  public String create(final @Valid User user, final BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return "auth/register";
    }

    final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    final String encodedPassword = encoder.encode(user.getPassword());

    user.setPassword(encodedPassword);

    userService.save(user);

    return "redirect:login";
  }
}
