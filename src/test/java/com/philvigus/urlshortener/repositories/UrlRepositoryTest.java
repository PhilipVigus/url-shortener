package com.philvigus.urlshortener.repositories;

import com.philvigus.urlshortener.model.Url;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@DataJpaTest
class UrlRepositoryTest {
  @Autowired UrlRepository urlRepository;

  @Test
  public void save() {
    Url url = new Url();

    urlRepository.save(url);

    Optional<Url> savedUrl = urlRepository.findById(url.getId());

    assertNotNull(url.getId());
    assertTrue(savedUrl.isPresent());
    assertEquals(url, savedUrl.get());
  }

  @Test
  public void findByFullUrl() {
    final String FULL_URL = "www.test.com";

    Url url = new Url();
    url.setFullUrl(FULL_URL);
    urlRepository.save(url);

    Optional<Url> savedUrl = urlRepository.findByFullUrl(FULL_URL);

    assertTrue(savedUrl.isPresent());
    assertEquals(FULL_URL, savedUrl.get().getFullUrl());
  }

  @Test
  public void findByShortUrl() {
    final String SHORT_URL = "abc";

    Url url = new Url();
    url.setShortUrl(SHORT_URL);
    urlRepository.save(url);

    Optional<Url> savedUrl = urlRepository.findByShortUrl(SHORT_URL);

    assertTrue(savedUrl.isPresent());
    assertEquals(SHORT_URL, savedUrl.get().getShortUrl());
  }
}
