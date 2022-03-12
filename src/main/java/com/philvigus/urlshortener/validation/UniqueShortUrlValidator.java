package com.philvigus.urlshortener.validation;

import com.philvigus.urlshortener.services.UrlService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueShortUrlValidator implements ConstraintValidator<UniqueShortUrl, String> {

  @Autowired private UrlService urlService;

  @Override
  public boolean isValid(String shortUrl, ConstraintValidatorContext context) {
    if (shortUrl == "" || !urlService.findByShortUrl(shortUrl).isPresent()) {
      return true;
    }

    context.disableDefaultConstraintViolation();

    context
        .buildConstraintViolationWithTemplate("Short URL already taken")
        .addConstraintViolation();

    return false;
  }
}
