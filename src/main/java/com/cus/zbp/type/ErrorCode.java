package com.cus.zbp.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
  // Internal Server error (for unspecified exceptions)
  INTERNAL_SERVER_ERROR("서버 내부 오류가 발생했습니다.", 1),

  // user exception(101~199)
  EMAIL_ALREADY_EXIST("사용중인 이메일입니다.", 101),

  AUTH_KEY_NOT_EXIST("이메일 인증 키가 없습니다.", 102),

  AUTH_ALREADY_DONE("인증 완료된 이메일을 다시 인증하려 합니다.", 103),

  USER_NOT_FOUND("없는 사용자 입니다.", 104),

  RESET_PW_REQUEST_KEY_NOT_FOUND("비밀번호 변경 요청 정보가 존재하지 않습니다.", 105),

  RESET_PW_REQUEST_KEY_EXPIRED("비밀번호 변경 요청 후 시간이 만료되었습니다.", 106),

  RESET_PW_LIMIT_DATE_NULL("내부오류 발생. 다시 변경 시도하세요.", 107),

  // software exception(201 ~ 299)
  SOFTWARE_NAME_ALREADY_EXIST("사용중인 Software 이름입니다.", 201),

  SOFTWARE_NOT_EXIST("존재하지 않는 Software입니다.", 202),

  SOFTWARE_USER_UNMATCH("Software의 user가 다릅니다.", 203),

  // version exception(301 ~ 399)
  VERSION_NAME_ALREADY_EXIST("해당 소프트웨어에 동일한 버전명이 존재합니다.", 301),

  VERSION_NOT_EXIST("존재하지 않는 버전입니다.", 302),

  // test category exception(401 ~ 499)
  TEST_CATEGORY_NAME_ALREADY_EXIST("해당 소프트웨어에 동일한 카테고리명이 존재합니다.", 401),

  TEST_CATEGORY_ABBR_ALREADY_EXIST("해당 소프트웨어에 동일한 약칭이 존재합니다.", 402),

  TEST_CATEGORY_ID_NOT_FOUND("해당 카테고리의 키가 없습니다.", 403),



  ;

  private final String discrption;
  private final int codeValue;
}
