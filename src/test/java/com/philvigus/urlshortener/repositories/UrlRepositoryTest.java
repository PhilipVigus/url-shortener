package com.philvigus.urlshortener.repositories;

import com.philvigus.urlshortener.model.Url;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@DataJpaTest
@DisplayName("URL repository")
class UrlRepositoryTest {
  @Autowired UrlRepository urlRepository;

  @Test
  @DisplayName("findByFullUrl - finds only URLs with the given full url")
  void findByFullUrl() {
    final String FULL_URL = "full";
    final String DIFFERENT_FULL_URL = "different";

    Url firstUrlWithFullUrl = new Url();
    firstUrlWithFullUrl.setFullUrl(FULL_URL);
    urlRepository.save(firstUrlWithFullUrl);

    Url secondUrlWithFullUrl = new Url();
    secondUrlWithFullUrl.setFullUrl(FULL_URL);
    urlRepository.save(secondUrlWithFullUrl);

    Url urlWithDifferentFullUrl = new Url();
    urlWithDifferentFullUrl.setFullUrl(DIFFERENT_FULL_URL);
    urlRepository.save(urlWithDifferentFullUrl);

    Set<Url> savedUrls = urlRepository.findByFullUrl(FULL_URL);

    assertEquals(2, savedUrls.size());
    assertTrue(savedUrls.contains(firstUrlWithFullUrl));
    assertTrue(savedUrls.contains(secondUrlWithFullUrl));
    assertFalse(savedUrls.contains(urlWithDifferentFullUrl));
  }

  @Test
  @DisplayName("findByFullUrl - finds the URL with the given short url")
  void findByShortUrl() {
    final String SHORT_URL = "short";

    Url url = new Url();
    url.setShortUrl(SHORT_URL);
    urlRepository.save(url);

    Optional<Url> savedUrl = urlRepository.findByShortUrl(SHORT_URL);

    assertTrue(savedUrl.isPresent());
    assertEquals(SHORT_URL, savedUrl.get().getShortUrl());
  }

  @Test
  @Sql("classpath:createUserWithUrl.sql")
  @Sql(scripts = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  void save() {
    final String SHORT_URL = "short";

    Url url = new Url();
    url.setShortUrl(SHORT_URL);
    urlRepository.save(url);
  }
}
