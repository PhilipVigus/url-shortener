package com.philvigus.urlshortener.repositories;

import com.philvigus.urlshortener.model.Url;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class UrlRepositoryTestIT {
  @Autowired UrlRepository urlRepository;

  @Test
  public void Save() {
    Url url = new Url();

    urlRepository.save(url);

    Optional<Url> savedUrl = urlRepository.findById(url.getId());

    assertNotNull(url.getId());
    assertTrue(savedUrl.isPresent());
    assertEquals(url, savedUrl.get());
  }
}
