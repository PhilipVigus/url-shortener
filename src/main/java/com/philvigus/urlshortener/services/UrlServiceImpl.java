package com.philvigus.urlshortener.services;

import com.philvigus.urlshortener.model.Url;
import com.philvigus.urlshortener.repositories.UrlRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UrlServiceImpl implements UrlService {
  private final UrlRepository urlRepository;

  public UrlServiceImpl(UrlRepository urlRepository) {
    this.urlRepository = urlRepository;
  }

  @Override
  public Url save(Url url) {
    return urlRepository.save(url);
  }

  @Override
  public Optional<Url> findByFullUrl(String fullUrl) {
    return urlRepository.findByFullUrl(fullUrl);
  }

  @Override
  public Optional<Url> findByShortUrl(String shortUrl) {
    return urlRepository.findByShortUrl(shortUrl);
  }
}
