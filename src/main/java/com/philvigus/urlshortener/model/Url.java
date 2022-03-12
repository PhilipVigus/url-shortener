package com.philvigus.urlshortener.model;

import com.philvigus.urlshortener.validation.UniqueShortUrl;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@Entity
@Table(name = "urls")
@UniqueShortUrl
public class Url {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id")
  private User user;

  @Column(columnDefinition = "text")
  @NotNull
  private String fullUrl;

  @NotNull
  @Pattern(
      regexp = "^[a-zA-z0-9_-]*$",
      message = "The short URL most only contain letters, numbers, _ and -")
  private String shortUrl;

  private long numberOfClicks;

  @PreRemove
  private void removeUrlFromUser() {
    user.getUrls().remove(this);
  }
}
