package com.cus.zbp.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
  // user exception
  EMAIL_ALREADY_EXIST("사용중인 이메일입니다."),

  AUTH_KEY_NOT_EXIST("이메일 인증 키가 없습니다."),

  AUTH_ALREADY_DONE("인증 완료된 이메일을 다시 인증하려 합니다."),

  USER_NOT_FOUND("없는 사용자 입니다."),

  RESET_PW_REQUEST_KEY_NOT_FOUND("비밀번호 변경 요청 정보가 존재하지 않습니다."),

  RESET_PW_REQUEST_KEY_EXPIRED("비밀번호 변경 요청 후 시간이 만료되었습니다."),

  RESET_PW_LIMIT_DATE_NULL("내부오류 발생. 다시 변경 시도하세요."),

  // software exception
  SOFTWARE_NAME_ALREADY_EXIST("사용중인 Software 이름입니다."),

  SOFTWARE_NOT_EXIST("존재하지 않는 Software입니다."),

  SOFTWARE_USER_UNMATCH("Software의 user가 다릅니다."),


  ;

  private final String discrption;
}
