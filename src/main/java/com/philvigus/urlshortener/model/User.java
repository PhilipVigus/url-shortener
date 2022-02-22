package com.philvigus.urlshortener.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true, length = 50)
  private String username;

  @Column(nullable = false, length = 64)
  private String password;
}
