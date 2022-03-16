package com.philvigus.urlshortener.validation;

import com.philvigus.urlshortener.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {

  @Autowired private UserService userService;

  @Override
  public boolean isValid(String username, ConstraintValidatorContext context) {
    if (username != null && userService.findByUsername(username) == null) {
      return true;
    }

    context.disableDefaultConstraintViolation();
    context.buildConstraintViolationWithTemplate("Username already taken").addConstraintViolation();

    return false;
  }
}
