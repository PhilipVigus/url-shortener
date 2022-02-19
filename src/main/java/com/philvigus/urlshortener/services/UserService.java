package com.philvigus.urlshortener.services;

import com.philvigus.urlshortener.model.User;

import java.util.List;

public interface UserService {
  User save(User user);

  List<User> getUserByUsername(String username);
}
