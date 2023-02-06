package com.cus.zbp.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserStatus {
  /**
   * 현재 가입 요청중
   */
  MEMBER_STATUS_REQ("REQ"),

  /**
   * 현재 이용중인 상태
   */
  MEMBER_STATUS_ING("ING"),

  /**
   * 현재 정지된 상태
   */
  MEMBER_STATUS_STOP("STOP"),

  /**
   * 현재 탈퇴된 회원
   */
  MEMBER_STATUS_WITHDRAW("WITHDRAW")

  ;

  private final String statusCode;
}
