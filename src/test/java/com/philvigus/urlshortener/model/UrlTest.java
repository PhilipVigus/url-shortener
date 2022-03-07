package com.philvigus.urlshortener.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Url model")
class UrlTest {
  Url url;

  @BeforeEach
  public void setUp() {
    url = new Url();
  }

  @Test
  @DisplayName("setId - ID correctly set")
  public void setId() {
    final Long ID = 1L;

    url.setId(ID);

    assertEquals(ID, url.getId());
  }

  @Test
  @DisplayName("setFullUrl - full URL correctly set")
  public void setFullUrl() {
    final String FULL_URL = "long";

    url.setFullUrl(FULL_URL);

    assertEquals(FULL_URL, url.getFullUrl());
  }

  @Test
  @DisplayName("setShortUrl - short URL correctly set")
  public void setShortUrl() {
    final String SHORT_URL = "short";

    url.setShortUrl(SHORT_URL);

    assertEquals(SHORT_URL, url.getShortUrl());
  }

  @Test
  @DisplayName("setNumberOfClicks - number of clicks correctly set")
  public void setNumberOfClicks() {
    final long NUMBER_OF_CLICKS = 1L;

    url.setNumberOfClicks(NUMBER_OF_CLICKS);

    assertEquals(NUMBER_OF_CLICKS, url.getNumberOfClicks());
  }
}
