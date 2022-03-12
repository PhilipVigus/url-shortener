package com.philvigus.urlshortener.model;

import com.philvigus.urlshortener.validation.UniqueShortUrl;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Entity
@Table(name = "urls")
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

  @NotNull @UniqueShortUrl private String shortUrl;

  private long numberOfClicks;

  @PreRemove
  private void removeUrlFromUser() {
    user.getUrls().remove(this);
  }
}
