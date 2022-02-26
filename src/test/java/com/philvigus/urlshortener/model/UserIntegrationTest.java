package com.philvigus.urlshortener.model;

import com.philvigus.urlshortener.services.UrlService;
import com.philvigus.urlshortener.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
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
  @Sql("classpath:createUserWithUrl.sql")
  @Sql(scripts = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  void getUrls() {
    User user = userService.findByUsername("phil");

    Set<Url> urls = user.getUrls();
    Url firstUrl = urls.stream().findFirst().get();

    assertEquals(1, urls.size());
    assertEquals("short", firstUrl.getShortUrl());
    assertEquals("full", firstUrl.getFullUrl());
  }
}
