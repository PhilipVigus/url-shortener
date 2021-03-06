package com.philvigus.urlshortener.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {
  @RequestMapping("/error")
  public String handleError(final HttpServletRequest request) {
    final Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

    String returnView = "error/error";

    if (status != null) {
      final Integer statusCode = Integer.valueOf(status.toString());

      if (statusCode == HttpStatus.NOT_FOUND.value()) {
        returnView = "error/not-found";
      }

      if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
        returnView = "error/server";
      }

      if (statusCode == HttpStatus.FORBIDDEN.value()) {
        returnView = "error/forbidden";
      }
    }

    return returnView;
  }
}
