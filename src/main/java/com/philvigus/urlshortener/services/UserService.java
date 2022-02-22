package com.philvigus.urlshortener.services;

import com.philvigus.urlshortener.model.User;

public interface UserService {
  User save(User user);

  User findByUsername(String username);
}
