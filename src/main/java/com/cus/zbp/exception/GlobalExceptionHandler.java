package com.cus.zbp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mail.MailException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.cus.zbp.dto.ErrorResponse;
import com.cus.zbp.type.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  // TODO: ErrorCode 마다 적절한 HttpStatus와 상태 파악에 필요한 값 반환하도록 구현
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(Exception e) {
    log.error("Exception occurred.", e);
    ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ErrorResponse(errorCode, errorCode.getDiscrption()));
  }
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleException(MethodArgumentNotValidException e) {
    log.error("MethodArgumentNotValidException occurred.", e);
    ErrorCode errorCode = ErrorCode.ARGUMENT_NOT_VALID;
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse(errorCode, errorCode.getDiscrption()));
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorResponse> handleException(HttpMessageNotReadableException e) {
    log.error("HttpMessageNotReadableException occurred.", e);
    ErrorCode errorCode = ErrorCode.REQUEST_ERROR;
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse(errorCode, errorCode.getDiscrption()));
  }

  @ExceptionHandler(MaxUploadSizeExceededException.class)
  public ResponseEntity<ErrorResponse> handleException(MaxUploadSizeExceededException e) {
    log.error("MaxUploadSizeExceededException occurred.", e);
    ErrorCode errorCode = ErrorCode.VERSION_FILE_SIZE_LIMIT;
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse(errorCode, errorCode.getDiscrption()));
  }

  @ExceptionHandler(UserException.class)
  public ResponseEntity<ErrorResponse> handleException(UserException e) {
    log.error("UserException occurred.", e);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse(e.getErrorCode(), e.getErrorMessege()));
  }

  @ExceptionHandler(SoftwareException.class)
  public ResponseEntity<ErrorResponse> handleException(SoftwareException e) {
    log.error("SoftwareException occurred.", e);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new ErrorResponse(e.getErrorCode(), e.getErrorMessege()));
  }

  @ExceptionHandler(MailException.class)
  public ResponseEntity<ErrorResponse> handleException(MailException e) {
    log.error("MailException occurred.", e);
    ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ErrorResponse(errorCode, errorCode.getDiscrption()));
  }

  @ExceptionHandler(VersionException.class)
  public ResponseEntity<ErrorResponse> handleException(VersionException e) {
    log.error("VersionException occurred.", e);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new ErrorResponse(e.getErrorCode(), e.getErrorMessege()));
  }

  @ExceptionHandler(VersionAuthException.class)
  public ResponseEntity<ErrorResponse> handleException(VersionAuthException e) {
    log.error("VersionAuthException occurred.", e);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse(e.getErrorCode(), e.getErrorMessege()));
  }

  @ExceptionHandler(TestCategoryException.class)
  public ResponseEntity<ErrorResponse> handleException(TestCategoryException e) {
    log.error("TestCategoryException occurred.", e);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new ErrorResponse(e.getErrorCode(), e.getErrorMessege()));
  }

  @ExceptionHandler(SubCategoryException.class)
  public ResponseEntity<ErrorResponse> handleException(SubCategoryException e) {
    log.error("SubCategoryException occurred.", e);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse(e.getErrorCode(), e.getErrorMessege()));
  }

}
