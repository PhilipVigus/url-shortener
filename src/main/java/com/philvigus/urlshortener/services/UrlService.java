package com.philvigus.urlshortener.services;

import com.philvigus.urlshortener.model.Url;

public interface UrlService {
  Url saveUrl(Url url);
}
