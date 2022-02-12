package com.philvigus.urlshortener.services;

import com.philvigus.urlshortener.model.Url;

import java.util.Optional;

public interface UrlService {
  Url saveUrl(Url url);

  Optional<Url> findByFullUrl(String fullUrl);
}
