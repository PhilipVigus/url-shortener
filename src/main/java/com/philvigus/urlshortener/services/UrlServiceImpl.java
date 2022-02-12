package com.philvigus.urlshortener.services;

import com.philvigus.urlshortener.model.Url;
import com.philvigus.urlshortener.repositories.UrlRepository;

import java.util.Optional;

public class UrlServiceImpl implements UrlService {
  private final UrlRepository urlRepository;

  public UrlServiceImpl(UrlRepository urlRepository) {
    this.urlRepository = urlRepository;
  }

  @Override
  public Url saveUrl(Url url) {
    return urlRepository.save(url);
  }

  @Override
  public Optional<Url> findByFullUrl(String fullUrl) {
    return urlRepository.findByFullUrl(fullUrl);
  }
}
