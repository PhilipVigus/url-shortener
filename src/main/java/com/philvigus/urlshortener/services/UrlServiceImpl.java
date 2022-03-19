package com.philvigus.urlshortener.services;

import com.philvigus.urlshortener.model.Url;
import com.philvigus.urlshortener.model.User;
import com.philvigus.urlshortener.repositories.UrlRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.net.URI;
import java.time.Instant;
import java.util.Base64;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UrlServiceImpl implements UrlService {
  private static final Long TIME_NORMALISER = 1_647_688_187L;
  private final UrlRepository urlRepository;

  public UrlServiceImpl(final UrlRepository urlRepository) {
    this.urlRepository = urlRepository;
  }

  @Override
  public Url save(final Url url, final User urlOwner) {
    url.setUser(urlOwner);

    if (url.getShortUrl().equals("")) {
      url.setShortUrl(calculateUniqueShortUrl());
    }

    url.setFullUrl(encodeString(url.getFullUrl()));

    url.setNumberOfClicks(0);

    return urlRepository.save(url);
  }

  @Override
  public Set<Url> findByFullUrl(final String fullUrl) {
    return urlRepository.findByFullUrl(fullUrl);
  }

  @Override
  public Optional<Url> findByShortUrl(final String shortUrl) {
    return urlRepository.findByShortUrl(shortUrl);
  }

  @Override
  public Set<Url> findAll() {
    final Set<Url> urls = new HashSet<>();

    urlRepository.findAll().forEach(urls::add);

    return urls;
  }

  @Override
  public void deleteById(final long id) {
    urlRepository.deleteById(id);
  }

  @Override
  public Optional<Url> findById(final long id) {
    return urlRepository.findById(id);
  }

  @Override
  public Url incrementNumberOfClicks(final Url url) {
    url.setNumberOfClicks(url.getNumberOfClicks() + 1);

    return urlRepository.save(url);
  }

  @Override
  public Url update(final Url url, final User user) {
    final Optional<Url> existingUrl = findById(url.getId());

    if (!existingUrl.isPresent()) {
      return save(url, user);
    }

    final String newShortUrl =
        url.getShortUrl().equals("") ? calculateUniqueShortUrl() : url.getShortUrl();

    existingUrl.get().setShortUrl(newShortUrl);
    existingUrl.get().setFullUrl(encodeString(url.getFullUrl()));

    return urlRepository.save(existingUrl.get());
  }

  private String calculateUniqueShortUrl() {
    String shortUrl = "";
    boolean uniqueShortUrlGenerated = false;

    final Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();

    while (!uniqueShortUrlGenerated) {
      final long time = Instant.now().getEpochSecond() - UrlServiceImpl.TIME_NORMALISER;
      final byte[] bytes = String.valueOf(time).getBytes();

      shortUrl = encoder.encodeToString(bytes);

      if (!findByShortUrl(shortUrl).isPresent()) {
        uniqueShortUrlGenerated = true;
      }
    }

    return shortUrl;
  }

  private String encodeString(final String string) {
    final DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory();
    factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.TEMPLATE_AND_VALUES);
    final URI uri = factory.uriString(string).build();

    return uri.toString();
  }
}
