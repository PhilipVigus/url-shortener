package com.philvigus.urlshortener.services;

import com.philvigus.urlshortener.model.Url;
import com.philvigus.urlshortener.repositories.UrlRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class UrlServiceImplTest {
  public UrlServiceImpl urlService;

  @Mock UrlRepository urlRepository;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    urlService = new UrlServiceImpl(urlRepository);
  }

  @Test
  public void saveUrl() {
    Url url = new Url();

    urlRepository.save(url);

    verify(urlRepository, times(1)).save(url);
  }

  @Test
  public void findByFullUrl() {
    final String FULL_URL = "www.test.com";

    Url url = new Url();
    urlRepository.save(url);

    urlRepository.findByFullUrl(FULL_URL);

    verify(urlRepository, times(1)).findByFullUrl(FULL_URL);
  }
}
