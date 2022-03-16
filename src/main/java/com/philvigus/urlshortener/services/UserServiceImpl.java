package com.philvigus.urlshortener.services;

import com.philvigus.urlshortener.model.User;
import com.philvigus.urlshortener.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
  private final UserRepository repository;

  @Autowired
  public UserServiceImpl(final UserRepository repository) {
    this.repository = repository;
  }

  @Override
  public User save(final User user) {
    return repository.save(user);
  }

  @Override
  public User findByUsername(final String username) {
    return repository.findByUsername(username);
  }
}
