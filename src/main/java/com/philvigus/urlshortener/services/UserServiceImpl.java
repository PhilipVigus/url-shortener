package com.philvigus.urlshortener.services;

import com.philvigus.urlshortener.model.User;
import com.philvigus.urlshortener.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
  private final UserRepository repository;

  public UserServiceImpl(UserRepository repository) {
    this.repository = repository;
  }

  @Override
  public User save(User user) {
    return repository.save(user);
  }

  @Override
  public List<User> getUserByUsername(String username) {
    return repository.getUserByUsername(username);
  }
}
