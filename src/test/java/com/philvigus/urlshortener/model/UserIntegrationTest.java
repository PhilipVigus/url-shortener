package com.philvigus.urlshortener.model;

import com.philvigus.urlshortener.services.UrlService;
import com.philvigus.urlshortener.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class UserIntegrationTest {
  @Autowired UserService userService;
  @Autowired UrlService urlService;

  @Test
  void getUrls() {
    final String USERNAME = "username";
    final String PASSWORD = "password";
    final String SHORT_URL = "test";
    final String LONG_URL = "www.google.com";

    User user = new User();
    user.setUsername(USERNAME);
    user.setPassword(PASSWORD);

    Url url = new Url();
    url.setShortUrl(SHORT_URL);
    url.setFullUrl(LONG_URL);
    url.setUser(userService.save(user));

    urlService.save(url);

    User savedUser = userService.findByUsername(USERNAME);

    Set<Url> userUrls = savedUser.getUrls();
    Url userUrl = userUrls.stream().findFirst().get();

    assertEquals(1, userUrls.size());
    assertEquals(SHORT_URL, userUrl.getShortUrl());
    assertEquals(LONG_URL, userUrl.getFullUrl());
  }
}
