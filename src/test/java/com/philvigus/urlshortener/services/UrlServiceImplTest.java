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

    Url savedUrl = urlRepository.save(url);

    verify(urlRepository, times(1)).save(url);
  }
}
