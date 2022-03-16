package com.philvigus.urlshortener.controllers;

import com.philvigus.urlshortener.model.Url;
import com.philvigus.urlshortener.services.UrlService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.Optional;

@RestController
public class UrlRedirectController {
  private final UrlService urlService;

  public UrlRedirectController(final UrlService urlService) {
    this.urlService = urlService;
  }

  @GetMapping("{shortUrl}")
  public ResponseEntity<Void> redirect(final @PathVariable String shortUrl) {
    final Optional<Url> url = urlService.findByShortUrl(shortUrl);

    if (!url.isPresent()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    urlService.incrementNumberOfClicks(url.get());

    return ResponseEntity.status(HttpStatus.FOUND)
        .location(URI.create(url.get().getFullUrl()))
        .build();
  }
}
