package com.philvigus.urlshortener.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class LoginControllerTest {
  @Autowired private TestRestTemplate testRestTemplate;

  @Test
  public void aUserCanLoadTheLoginPage() throws Exception {
    ResponseEntity<String> response = this.testRestTemplate.getForEntity("/login", String.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  public void anUnregisteredUserCannotLogin() throws Exception {
    MultiValueMap<String, String> form = new LinkedMultiValueMap<>();

    form.set("username", "phil");
    form.set("password", "password");

    ResponseEntity<String> response =
        this.testRestTemplate.postForEntity(
            "/login", new HttpEntity<>(form, new HttpHeaders()), String.class);

    String responseLocation = response.getHeaders().getFirst("Location");

    assertEquals(HttpStatus.FOUND, response.getStatusCode());
    assertThat(responseLocation, containsString("error=true"));
  }

  @Test
  @Sql("classpath:createUserWithUrl.sql")
  @Sql(scripts = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  public void aRegisteredUserCanLogin() throws Exception {
    MultiValueMap<String, String> form = new LinkedMultiValueMap<>();

    form.set("username", "phil");
    form.set("password", "password");

    ResponseEntity<String> response =
        this.testRestTemplate.postForEntity(
            "/login", new HttpEntity<>(form, new HttpHeaders()), String.class);

    String responseLocation = response.getHeaders().getFirst("Location");

    assertEquals(HttpStatus.FOUND, response.getStatusCode());
    assertThat(responseLocation, containsString("dashboard"));
  }
}
