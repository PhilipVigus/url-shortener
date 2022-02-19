package com.philvigus.urlshortener.controllers;

import com.philvigus.urlshortener.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RegistrationController {
  @GetMapping("/register")
  public String view(Model model) {
    model.addAttribute("user", new User());
    return "auth/register";
  }
}
