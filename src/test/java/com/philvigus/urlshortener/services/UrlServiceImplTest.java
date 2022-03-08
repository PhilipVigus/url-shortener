package com.philvigus.urlshortener.services;

import com.philvigus.urlshortener.model.Url;
import com.philvigus.urlshortener.model.User;
import com.philvigus.urlshortener.repositories.UrlRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

@DisplayName("UrlServiceImpl")
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
  @DisplayName("save - calls save on the URL repository with the given URL model")
  void save() {
    Url url = new Url();
    User user = new User();

    urlService.save(url, user);

    verify(urlRepository, times(1)).save(url);
  }

  @Test
  @DisplayName("findByFullUrl - calls findByFullUrl on the URL repository with the given full URL")
  void findByFullUrl() {
    final String FULL_URL = "long";

    urlService.findByFullUrl(FULL_URL);

    verify(urlRepository, times(1)).findByFullUrl(FULL_URL);
  }

  @Test
  @DisplayName(
      "findByShortUrl - calls findByShortUrl on the URL repository with the given short URL")
  void findByShortUrl() {
    final String SHORT_URL = "short";

    urlService.findByFullUrl(SHORT_URL);

    verify(urlRepository, times(1)).findByFullUrl(SHORT_URL);
  }

  @Test
  @DisplayName("findAll - calls findAll on the URL repository")
  void findAll() {
    urlService.findAll();

    verify(urlRepository, times(1)).findAll();
  }

  @Test
  @DisplayName("deleteById - calls findAll on the URL repository with the given ID")
  void deleteById() {
    final long ID = 1L;

    urlService.deleteById(ID);

    verify(urlRepository, times(1)).deleteById(ID);
  }

  @Test
  @DisplayName("findById - calls findById on the URL repository with the given ID")
  void findById() {
    final long ID = 1L;

    urlService.findById(ID);

    verify(urlRepository, times(1)).findById(ID);
  }

  @Test
  @DisplayName("incrementNumberOfClicks - calls incrementNumberOfClicks on the URL repository")
  void incrementNumberOfClicks() {
    final long INITIAL_NUMBER_OF_CLICKS = 0L;
    when(mockedUrl.getNumberOfClicks()).thenReturn(INITIAL_NUMBER_OF_CLICKS);

    urlService.incrementNumberOfClicks(mockedUrl);

    verify(mockedUrl, times(1)).getNumberOfClicks();
    verify(mockedUrl, times(1)).setNumberOfClicks(INITIAL_NUMBER_OF_CLICKS + 1);
  }
}
