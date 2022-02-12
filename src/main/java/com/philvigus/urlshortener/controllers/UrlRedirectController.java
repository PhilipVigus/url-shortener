package com.philvigus.urlshortener.controllers;

import com.philvigus.urlshortener.model.Url;
import com.philvigus.urlshortener.services.UrlService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Optional;

@RestController
public class UrlRedirectController {
  private final UrlService urlService;

  public UrlRedirectController(UrlService urlService) {
    this.urlService = urlService;
  }

  @GetMapping("{shortUrl}")
  public ResponseEntity<Void> redirect(@PathVariable String shortUrl) {
    Optional<Url> url = urlService.findByFullUrl(shortUrl);

    if (!url.isPresent()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    return ResponseEntity.status(HttpStatus.FOUND)
        .location(URI.create("https://www.google.com"))
        .build();
  }
}
