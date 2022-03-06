package com.philvigus.urlshortener.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UrlTest {
  Url url;

  @BeforeEach
  public void setUp() {
    url = new Url();
  }

  @Test
  public void setId() {
    final Long ID = 1L;

    url.setId(ID);

    assertEquals(ID, url.getId());
  }

  @Test
  public void setFullUrl() {
    final String FULL_URL = "www.test.com";

    url.setFullUrl(FULL_URL);

    assertEquals(FULL_URL, url.getFullUrl());
  }

  @Test
  public void setShortUrl() {
    final String SHORT_URL = "abcde";

    url.setShortUrl(SHORT_URL);

    assertEquals(SHORT_URL, url.getShortUrl());
  }

  @Test
  public void setNumberOfClicks() {
    final long NUMBER_OF_CLICKS = 1L;

    url.setNumberOfClicks(NUMBER_OF_CLICKS);

    assertEquals(NUMBER_OF_CLICKS, url.getNumberOfClicks());
  }
}
