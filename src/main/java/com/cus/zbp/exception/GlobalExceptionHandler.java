package com.cus.zbp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.cus.zbp.dto.ErrorResponse;
import com.cus.zbp.type.ErrorCode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  // TODO: Exception 상위클래스로 다들어오지 않는지 확인
  // TODO: ErrorCode 마다 적절한 HttpStatus와 상태 파악에 필요한 값 반환하도록 구현
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(Exception e) {
    log.error("Exception occured.", e);
    ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ErrorResponse(errorCode, errorCode.getDiscrption()));
  }

  @ExceptionHandler(SoftwareException.class)
  public ResponseEntity<ErrorResponse> handleException(SoftwareException e) {
    log.error("SoftwareException occured.", e);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new ErrorResponse(e.getErrorCode(), e.getErrorMessege()));
  }

  @ExceptionHandler(MailException.class)
  public ResponseEntity<ErrorResponse> handleException(MailException e) {
    log.error("MailException occured.", e);
    ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ErrorResponse(errorCode, errorCode.getDiscrption()));
  }

  @ExceptionHandler(VersionException.class)
  public ResponseEntity<ErrorResponse> handleException(VersionException e) {
    log.error("VersionException occured.", e);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new ErrorResponse(e.getErrorCode(), e.getErrorMessege()));
  }

  @ExceptionHandler(TestCategoryException.class)
  public ResponseEntity<ErrorResponse> handleException(TestCategoryException e) {
    log.error("TestCategoryException occured.", e);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new ErrorResponse(e.getErrorCode(), e.getErrorMessege()));
  }


}
