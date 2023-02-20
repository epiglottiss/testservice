package com.cus.zbp.exception;

import com.cus.zbp.type.ErrorCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TestCategoryException extends RuntimeException {

  private String errorMessege;
  private ErrorCode errorCode;

  public TestCategoryException(ErrorCode errorCode) {
    this.errorMessege = errorCode.getDiscrption();
    this.errorCode = errorCode;
  }
}
