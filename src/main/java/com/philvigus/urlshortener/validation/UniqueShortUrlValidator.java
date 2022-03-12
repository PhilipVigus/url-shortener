package com.philvigus.urlshortener.validation;

import com.philvigus.urlshortener.model.Url;
import com.philvigus.urlshortener.services.UrlService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class UniqueShortUrlValidator implements ConstraintValidator<UniqueShortUrl, Url> {

  @Autowired private UrlService urlService;

  @Override
  public boolean isValid(Url newUrl, ConstraintValidatorContext context) {
    // We are going to generate a random short url - we don't need to check anything
    if (newUrl.getShortUrl().equals("")) {
      return true;
    }

    // It's a new URL, because the id isn't set
    if (newUrl.getId() == null) {
      Optional<Url> optionalExistingUrlWithSameShortUrl =
          urlService.findByShortUrl(newUrl.getShortUrl());

      return !optionalExistingUrlWithSameShortUrl.isPresent();
    }

    Optional<Url> optionalExistingUrlWithSameId = urlService.findById(newUrl.getId());

    // There's no existing Url with this id
    // So make sure the short URL isn't already in use
    if (!optionalExistingUrlWithSameId.isPresent()) {
      Optional<Url> optionalExistingUrlWithSameShortUrl =
          urlService.findByShortUrl(newUrl.getShortUrl());

      return !optionalExistingUrlWithSameShortUrl.isPresent();
    }

    Optional<Url> optionalExistingUrlWithSameShortUrl =
        urlService.findByShortUrl(newUrl.getShortUrl());

    // There's no existing Url with this short Url
    if (!optionalExistingUrlWithSameShortUrl.isPresent()) {
      return true;
    }

    Url existingUrlWithSameShortUrl = optionalExistingUrlWithSameShortUrl.get();

    // There's an existing URL with the new URL's short URL, but it represents
    // the same one (ie it's being updated), so everything is good
    if (existingUrlWithSameShortUrl.getId() == newUrl.getId()) {
      return true;
    }

    // Phew - if we get this far it means we're trying to create or update an
    // existing URL while setting it's short URL to the short URL of a different
    // URL's short URL
    return false;
  }
}
