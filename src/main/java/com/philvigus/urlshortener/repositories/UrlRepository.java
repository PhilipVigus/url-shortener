package com.philvigus.urlshortener.repositories;

import com.philvigus.urlshortener.model.Url;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.Set;

public interface UrlRepository extends CrudRepository<Url, Long> {
  Set<Url> findByFullUrl(String fullUrl);

  Optional<Url> findByShortUrl(String shortUrl);
}
