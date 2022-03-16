package com.philvigus.urlshortener.validation;

import com.philvigus.urlshortener.model.Url;
import com.philvigus.urlshortener.services.UrlService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class UniqueShortUrlValidator implements ConstraintValidator<UniqueShortUrl, Url> {
  public static final List<String> FORBIDDEN_SHORT_URLS =
      Collections.unmodifiableList(Arrays.asList("urls"));

  @Autowired private UrlService urlService;

  @Override
  public boolean isValid(Url urlBeingValidated, ConstraintValidatorContext context) {
    // We are going to generate a random short url - we don't need to check anything
    if (urlBeingValidated.getShortUrl().equals("")) {
      return true;
    }

    if (UniqueShortUrlValidator.FORBIDDEN_SHORT_URLS.contains(urlBeingValidated.getShortUrl())) {
      return false;
    }

    // It's a new URL, because the id isn't set
    if (urlBeingValidated.getId() == null) {
      return isValidNewUrl(urlBeingValidated);
    }

    Optional<Url> optionalExistingUrlWithSameId = urlService.findById(urlBeingValidated.getId());

    // There's no existing Url with this id, so it's new URL
    if (!optionalExistingUrlWithSameId.isPresent()) {
      return isValidNewUrl(urlBeingValidated);
    }

    return isValidExistingUrl(urlBeingValidated);
  }

  private boolean isValidNewUrl(Url urlBeingValidated) {
    Optional<Url> optionalExistingUrlWithSameShortUrl =
        urlService.findByShortUrl(urlBeingValidated.getShortUrl());

    return !optionalExistingUrlWithSameShortUrl.isPresent();
  }

  private boolean isValidExistingUrl(Url urlBeingValidated) {
    Optional<Url> optionalExistingUrlWithSameShortUrl =
        urlService.findByShortUrl(urlBeingValidated.getShortUrl());

    // There's no existing Url with this short Url
    if (!optionalExistingUrlWithSameShortUrl.isPresent()) {
      return true;
    }

    Url existingUrlWithSameShortUrl = optionalExistingUrlWithSameShortUrl.get();

    // There's an existing URL with the new URL's short URL, but it represents
    // the same one (ie it's being updated), so everything is good
    if (existingUrlWithSameShortUrl.getId().equals(urlBeingValidated.getId())) {
      return true;
    }

    // We're updating an existing URL and setting it's short URL to a value
    // that's in use by another URL, so the update isn't valid
    return false;
  }
}
