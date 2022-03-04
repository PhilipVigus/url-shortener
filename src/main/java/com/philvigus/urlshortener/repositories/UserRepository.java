package com.philvigus.urlshortener.repositories;

import com.philvigus.urlshortener.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
  @Query("SELECT u FROM User u WHERE u.username = ?1")
  User findByUsername(String username);
}
