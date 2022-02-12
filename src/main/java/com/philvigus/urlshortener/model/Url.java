package com.philvigus.urlshortener.model;

import lombok.Data;

@Data
public class Url {
  private Long id;
  private String fullUrl;
}
