package com.philvigus.urlshortener.model;

import com.philvigus.urlshortener.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
@ActiveProfiles("test")
@Sql("classpath:createUserWithUrl.sql")
@Sql(scripts = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class UserIntegrationTest {
  @Autowired UserRepository userRepository;

  @Test
  void getUrls() {
    User user = userRepository.findByUsername("phil");

    Set<Url> urls = user.getUrls();
    Url firstUrl = urls.stream().findFirst().orElse(null);

    assertEquals(1, urls.size());
    assertNotNull(firstUrl);
    assertEquals("short", firstUrl.getShortUrl());
    assertEquals("full", firstUrl.getFullUrl());
  }
}
