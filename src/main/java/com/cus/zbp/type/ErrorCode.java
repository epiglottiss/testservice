package com.cus.zbp.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
  USER_NOT_FOUND("없는 사용자 입니다."),

  SOFTWARE_NAME_ALREADY_EXIST("사용중인 Software 이름입니다."),

  SOFTWARE_NOT_EXIST("존재하지 않는 Software입니다."),

  SOFTWARE_USER_UNMATCH("Software의 user가 다릅니다."),


  ;

  private final String discrption;
}
