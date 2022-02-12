package com.philvigus.urlshortener.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UrlTest {
  Url url;

  @BeforeEach
  public void setUp() {
    url = new Url();
  }

  @Test
  public void setId() {
    Long id = 1L;

    url.setId(id);

    assertEquals(id, url.getId());
  }

  @Test
  public void setFullUrl() {
    String fullUrl = "www.test.com";

    url.setFullUrl(fullUrl);

    assertEquals(fullUrl, url.getFullUrl());
  }
}
