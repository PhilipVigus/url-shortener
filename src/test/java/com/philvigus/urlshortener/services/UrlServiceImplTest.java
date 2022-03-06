package com.philvigus.urlshortener.services;

import com.philvigus.urlshortener.model.Url;
import com.philvigus.urlshortener.model.User;
import com.philvigus.urlshortener.repositories.UrlRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

class UrlServiceImplTest {
  private UrlService urlService;

  @Mock UrlRepository urlRepository;

  @Mock Url mockedUrl;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    urlService = new UrlServiceImpl(urlRepository);
  }

  @Test
  public void save() {
    Url url = new Url();
    User user = new User();

    urlService.save(url, user);

    verify(urlRepository, times(1)).save(url);
  }

  @Test
  public void findByFullUrl() {
    final String FULL_URL = "www.test.com";

    urlService.findByFullUrl(FULL_URL);

    verify(urlRepository, times(1)).findByFullUrl(FULL_URL);
  }

  @Test
  public void findByShortUrl() {
    final String SHORT_URL = "short";

    urlService.findByFullUrl(SHORT_URL);

    verify(urlRepository, times(1)).findByFullUrl(SHORT_URL);
  }

  @Test
  public void findAll() {
    urlService.findAll();

    verify(urlRepository, times(1)).findAll();
  }

  @Test
  public void deleteById() {
    final long ID = 1L;

    urlService.deleteById(ID);

    verify(urlRepository, times(1)).deleteById(ID);
  }

  @Test
  public void findById() {
    final long ID = 1L;

    urlService.findById(ID);

    verify(urlRepository, times(1)).findById(ID);
  }

  @Test
  public void incrementNumberOfClicks() {
    final long INITIAL_NUMBER_OF_CLICKS = 0L;
    when(mockedUrl.getNumberOfClicks()).thenReturn(INITIAL_NUMBER_OF_CLICKS);

    urlService.incrementNumberOfClicks(mockedUrl);

    verify(mockedUrl, times(1)).getNumberOfClicks();
    verify(mockedUrl, times(1)).setNumberOfClicks(INITIAL_NUMBER_OF_CLICKS + 1);
  }
}
