package com.philvigus.urlshortener.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UniqueShortUrlValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface UniqueShortUrl {

  String message() default "Short URL already taken";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
