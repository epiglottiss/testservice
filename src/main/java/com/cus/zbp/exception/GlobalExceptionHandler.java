package com.cus.zbp.exception;

import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(Exception.class)
  public void handleException(Exception e) {
    log.error("Exception occured.", e);
  }

  @ExceptionHandler(SoftwareException.class)
  public void handleException(SoftwareException e) {
    log.error("SoftwareException occured.", e);
  }

  @ExceptionHandler(MailException.class)
  public void handleException(MailException e) {
    log.error("MailException occured.", e);
  }

}
