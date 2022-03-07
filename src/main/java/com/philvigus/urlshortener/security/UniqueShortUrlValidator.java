package com.philvigus.urlshortener.security;

import com.philvigus.urlshortener.services.UrlService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueShortUrlValidator implements ConstraintValidator<UniqueShortUrl, String> {

  @Autowired private UrlService urlService;

  @Override
  public boolean isValid(String shortUrl, ConstraintValidatorContext context) {
    if (shortUrl != null && (urlService.findByShortUrl(shortUrl) == null)) {
      return true;
    }

    context.disableDefaultConstraintViolation();
    context
        .buildConstraintViolationWithTemplate("Short URL already taken")
        .addConstraintViolation();

    return false;
  }
}
