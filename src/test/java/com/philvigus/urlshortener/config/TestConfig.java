package com.philvigus.urlshortener.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@TestConfiguration
@Order(1)
class TestConfig extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf()
        .disable()
        .authorizeRequests()
        .antMatchers("/urls")
        .authenticated()
        .anyRequest()
        .permitAll()
        .and()
        .formLogin()
        .loginPage("/login")
        .failureUrl("/login?error=true")
        .usernameParameter("username")
        .defaultSuccessUrl("/urls")
        .permitAll()
        .and()
        .logout()
        .logoutUrl("/logout")
        .logoutSuccessUrl("/")
        .permitAll();
  }
}
