package com.philvigus.urlshortener.repositories;

import com.philvigus.urlshortener.model.Url;
import org.springframework.data.repository.CrudRepository;

public interface UrlRepository extends CrudRepository<Url, Long> {}
