package com.philvigus.urlshortener.repositories;

import com.philvigus.urlshortener.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {
  public List<User> getUserByUsername(String username);
}
