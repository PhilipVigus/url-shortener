package com.philvigus.urlshortener.security;

import org.passay.*;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {

  @Override
  public void initialize(ValidPassword constraintAnnotation) {}

  @Override
  public boolean isValid(String password, ConstraintValidatorContext context) {
    PasswordValidator validator =
        new PasswordValidator(
            Arrays.asList(
                new LengthRule(8, 30),
                new CharacterRule(EnglishCharacterData.UpperCase, 1),
                new CharacterRule(EnglishCharacterData.LowerCase, 1),
                new CharacterRule(EnglishCharacterData.Digit, 1),
                new WhitespaceRule()));

    RuleResult result = validator.validate(new PasswordData(password));

    if (result.isValid()) {
      return true;
    }

    context.disableDefaultConstraintViolation();
    context
        .buildConstraintViolationWithTemplate(
            "Must be 8 or more characters and contain at least one number, uppercase and lowercase letter")
        .addConstraintViolation();

    return false;
  }
}
