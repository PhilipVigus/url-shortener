package com.philvigus.urlshortener.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "user")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true, length = 50)
  private String username;

  @Column(nullable = false, length = 64)
  private String password;

  @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
  private Set<Url> urls = new HashSet<>();
}
