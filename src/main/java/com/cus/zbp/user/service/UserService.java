package com.cus.zbp.user.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.cus.zbp.user.model.PasswordResetInput;
import com.cus.zbp.user.model.UserInput;

public interface UserService extends UserDetailsService {
  /**
   * 회원 가입
   */
  boolean register(UserInput parameter);

  /**
   * 회원 인증 메일 전송
   */
  boolean emailAuth(String uuid);

  UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

  /**
   * 비밀번호 초기화 메일 전송
   */
  boolean sendResetPassword(PasswordResetInput parameter);

  /**
   * 비밀번호 초기화
   */
  boolean resetPassword(String id, String password);

  /**
   * 리셋 id 유효 확인
   */
  boolean checkResetPassword(String id);
}
