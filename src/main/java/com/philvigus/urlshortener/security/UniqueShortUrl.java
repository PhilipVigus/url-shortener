package com.philvigus.urlshortener.security;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UniqueUsernameValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface UniqueShortUrl {

  public String message() default "Short URL already taken";

  public Class<?>[] groups() default {};

  public Class<? extends Payload>[] payload() default {};
}
