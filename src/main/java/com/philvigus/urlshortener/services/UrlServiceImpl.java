package com.philvigus.urlshortener.services;

import com.philvigus.urlshortener.model.Url;
import com.philvigus.urlshortener.model.User;
import com.philvigus.urlshortener.repositories.UrlRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Base64;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UrlServiceImpl implements UrlService {
  private static final Long TIME_NORMALISER = 1646416961L;
  private final UrlRepository urlRepository;

  public UrlServiceImpl(UrlRepository urlRepository) {
    this.urlRepository = urlRepository;
  }

  @Override
  public Url save(Url url, User urlOwner) {
    url.setUser(urlOwner);

    long time = Instant.now().getEpochSecond() - UrlServiceImpl.TIME_NORMALISER;
    byte[] bytes = String.valueOf(time).getBytes();

    // calculate short url
    String shortUrl = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    url.setShortUrl(shortUrl);

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

  @Override
  public Set<Url> findAll() {
    Set<Url> urls = new HashSet<>();

    urlRepository.findAll().forEach(urls::add);

    return urls;
  }
}
