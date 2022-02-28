package com.philvigus.urlshortener.config;

import com.philvigus.urlshortener.services.UserService;
import com.philvigus.urlshortener.services.UserServiceImpl;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Profile("test-unit")
@Configuration
public class UserServiceTestConfiguration {
  @Bean
  @Primary
  public UserService userService() {
    return Mockito.mock(UserServiceImpl.class);
  }
}
