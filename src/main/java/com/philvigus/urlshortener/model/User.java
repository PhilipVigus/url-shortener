package com.philvigus.urlshortener.model;

import com.philvigus.urlshortener.validation.UniqueUsername;
import com.philvigus.urlshortener.validation.ValidPassword;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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

  @NotNull
  @Size(min = 3, max = 30)
  @Column(nullable = false, unique = true, length = 50)
  @UniqueUsername
  private String username;

  @Column(nullable = false, length = 64)
  @ValidPassword
  private String password;

  @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  private Set<Url> urls = new HashSet<>();
}
