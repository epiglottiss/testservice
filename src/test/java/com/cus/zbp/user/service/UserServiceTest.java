package com.cus.zbp.user.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCrypt;
import com.cus.zbp.components.MailComponents;
import com.cus.zbp.exception.UserException;
import com.cus.zbp.type.ErrorCode;
import com.cus.zbp.type.UserStatus;
import com.cus.zbp.user.entity.User;
import com.cus.zbp.user.model.PasswordResetInput;
import com.cus.zbp.user.model.UserInput;
import com.cus.zbp.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
  @Mock
  private UserRepository userRepository;

  @Mock
  private MailComponents mailComponents;

  @InjectMocks
  private UserService userService;

  @Test
  void successRegister() throws Exception {
    // given
    String password = "password";
    String encPassword = BCrypt.hashpw(password, BCrypt.gensalt());

    UserInput userInput = UserInput.builder().email("not-exist-email@naver1.com").name("noname")
        .password(password).build();
    given(userRepository.findByEmail(anyString())).willReturn(Optional.empty());

    given(userRepository.save(any())).willReturn(User.builder().email("not-exist-email@naver1.com")
        .name("noname").password(encPassword).registerDate(LocalDateTime.now()).emailAuth(false)
        .emailAuthKey("authkeyuuid").userStatus(UserStatus.MEMBER_STATUS_REQ).build());
    ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

    // when
    userService.register(userInput);
    // then
    verify(userRepository, times(1)).save(captor.capture());
    assertEquals("not-exist-email@naver1.com", captor.getValue().getEmail());
    assertEquals("noname", captor.getValue().getName());
    assertNotEquals("password", captor.getValue().getPassword());
    assertFalse(captor.getValue().isEmailAuth());
    assertEquals(UserStatus.MEMBER_STATUS_REQ, captor.getValue().getUserStatus());
  }

  @Test
  void failRegister_EMAIL_ALREADY_EXIST() {
    // given
    UserInput userInput = UserInput.builder().email("not-exist-email@naver1.com").name("noname")
        .password("password").build();
    User user = User.builder().email("not-exist-email@naver1.com").name("noname")
        .password("password").build();
    given(userRepository.findByEmail(anyString())).willReturn(Optional.of(user));
    // when
    UserException e = assertThrows(UserException.class, () -> userService.register(userInput));
    // then
    assertEquals(ErrorCode.EMAIL_ALREADY_EXIST, e.getErrorCode());
  }

  @Test
  void successEmailAuth() throws Exception {
    // given
    String uuid = "auth-key";
    User user = User.builder().email("not-exist-email@naver1.com").name("noname")
        .password("password").emailAuthKey(uuid).emailAuth(false).build();
    given(userRepository.findByEmailAuthKey(anyString())).willReturn(Optional.of(user));
    given(userRepository.save(any())).willReturn(User.builder().email("not-exist-email@naver1.com")
        .name("noname").password("password").emailAuthKey("auth-key").emailAuth(true)
        .userStatus(UserStatus.MEMBER_STATUS_ING).emailAuthDate(LocalDateTime.now()).build());
    ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

    // when
    userService.emailAuth(uuid);
    // then
    verify(userRepository, times(1)).save(captor.capture());
    assertEquals(UserStatus.MEMBER_STATUS_ING, captor.getValue().getUserStatus());
    assertTrue(captor.getValue().isEmailAuth());
    assertNotNull(captor.getValue().getEmailAuthDate());
  }

  @Test
  void failEmailAuth_AUTH_KEY_NOT_EXIST() {
    // given
    String uuid = "auth-key";
    given(userRepository.findByEmailAuthKey(anyString())).willReturn(Optional.empty());
    // when
    UserException e = assertThrows(UserException.class, () -> userService.emailAuth(uuid));
    // then
    assertEquals(ErrorCode.AUTH_KEY_NOT_EXIST, e.getErrorCode());
  }

  @Test
  void failEmailAuth_AUTH_ALREADY_DONE() {
    // given
    String uuid = "auth-key";
    User user = User.builder().email("not-exist-email@naver1.com").name("noname")
        .password("password").emailAuthKey(uuid).emailAuth(true).build();
    given(userRepository.findByEmailAuthKey(anyString())).willReturn(Optional.of(user));

    // when
    UserException e = assertThrows(UserException.class, () -> userService.emailAuth(uuid));
    // then
    assertEquals(ErrorCode.AUTH_ALREADY_DONE, e.getErrorCode());
  }

  @Test
  void failLoadUserByUsername_usernameNotFound() {
    // given
    given(userRepository.findByEmail(anyString())).willReturn(Optional.empty());
    // when
    Exception e = assertThrows(Exception.class, () -> userService.loadUserByUsername("any"));
    // then
    assertEquals("회원 정보가 존재하지 않습니다.", e.getMessage());
  }

  @Test
  void failLoadUserByUsername_MEMBER_STATUS_REQ() {
    // given
    User user =
        User.builder().email("not-exist-email@naver1.com").name("noname").password("password")
            .emailAuthKey("uuid").emailAuth(true).userStatus(UserStatus.MEMBER_STATUS_REQ).build();

    given(userRepository.findByEmail(anyString())).willReturn(Optional.of(user));
    // when
    Exception e = assertThrows(Exception.class, () -> userService.loadUserByUsername("any"));
    // then
    assertEquals("이메일 인증 미완료 상태입니다.", e.getMessage());
  }

  @Test
  void failLoadUserByUsername_MEMBER_STATUS_STOP() {
    // given
    User user =
        User.builder().email("not-exist-email@naver1.com").name("noname").password("password")
            .emailAuthKey("uuid").emailAuth(true).userStatus(UserStatus.MEMBER_STATUS_STOP).build();

    given(userRepository.findByEmail(anyString())).willReturn(Optional.of(user));
    // when
    Exception e = assertThrows(Exception.class, () -> userService.loadUserByUsername("any"));
    // then
    assertEquals("이용 정지된 아이디입니다.", e.getMessage());
  }

  @Test
  void failLoadUserByUsername_MEMBER_STATUS_WITHDRAW() {
    // given
    User user = User.builder().email("not-exist-email@naver1.com").name("noname")
        .password("password").emailAuthKey("uuid").emailAuth(true)
        .userStatus(UserStatus.MEMBER_STATUS_WITHDRAW).build();

    given(userRepository.findByEmail(anyString())).willReturn(Optional.of(user));
    // when
    Exception e = assertThrows(Exception.class, () -> userService.loadUserByUsername("any"));
    // then
    assertEquals("탈퇴한 아이디입니다.", e.getMessage());
  }

  @Test
  void successSendResetPassword() throws Exception {
    // given
    User user = User.builder().email("not-exist-email@naver1.com").name("noname")
        .password("password").emailAuth(true).build();

    PasswordResetInput input = PasswordResetInput.builder().email("not-exist-email@naver1.com")
        .name("noname").id("uuid").password("password").build();
    given(userRepository.findByEmailAndName(anyString(), anyString()))
        .willReturn(Optional.of(user));

    given(userRepository.save(any()))
        .willReturn(User.builder().email("not-exist-email@naver1.com").build());
    ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

    // when
    userService.sendResetPassword(input);
    // then
    verify(userRepository, times(1)).save(captor.capture());
    assertNotNull(captor.getValue().getResetPasswordKey());
    assertNotNull(captor.getValue().getResetPasswordLimitDate());
  }

  @Test
  void failSendResetPassword() {
    // given
    PasswordResetInput input = PasswordResetInput.builder().email("not-exist-email@naver1.com")
        .name("noname").id("uuid").password("password").build();
    given(userRepository.findByEmailAndName(anyString(), anyString())).willReturn(Optional.empty());
    // when
    UserException e = assertThrows(UserException.class, () -> userService.sendResetPassword(input));
    // then
    assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
  }

  @Test
  void successResetPassword() throws Exception {
    // given
    User user = User.builder().email("not-exist-email@naver1.com").name("noname")
        .password("password").emailAuth(true).resetPasswordKey("resetKey")
        .resetPasswordLimitDate(LocalDateTime.now().plusHours(2)).build();

    given(userRepository.findByResetPasswordKey(anyString())).willReturn(Optional.of(user));
    given(userRepository.save(any()))
        .willReturn(User.builder().password(BCrypt.hashpw("password", BCrypt.gensalt()))
            .resetPasswordKey("").resetPasswordLimitDate(null).build());
    ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

    // when
    userService.resetPassword("resetKey", "password");
    // then
    verify(userRepository, times(1)).save(captor.capture());
    assertNotEquals("password", captor.getValue().getPassword());
    assertEquals("", captor.getValue().getResetPasswordKey());
    assertNull(captor.getValue().getResetPasswordLimitDate());
  }

  @Test
  void failResetPassword_userNotFound() {
    // given
    given(userRepository.findByResetPasswordKey(anyString())).willReturn(Optional.empty());

    // when
    UserException e =
        assertThrows(UserException.class, () -> userService.resetPassword("any", "any"));
    // then
    assertEquals(ErrorCode.RESET_PW_REQUEST_KEY_NOT_FOUND, e.getErrorCode());
  }

  @Test
  void failResetPassword_limitDateNull() {
    // given
    User user =
        User.builder().email("not-exist-email@naver1.com").name("noname").password("password")
            .emailAuth(true).resetPasswordKey("resetKey").resetPasswordLimitDate(null).build();

    given(userRepository.findByResetPasswordKey(anyString())).willReturn(Optional.of(user));

    // when
    UserException e =
        assertThrows(UserException.class, () -> userService.resetPassword("any", "any"));
    // then
    assertEquals(ErrorCode.RESET_PW_LIMIT_DATE_NULL, e.getErrorCode());
  }

  @Test
  void failResetPassword_limitDateExpired() {
    // given
    User user = User.builder().email("not-exist-email@naver1.com").name("noname")
        .password("password").emailAuth(true).resetPasswordKey("resetKey")
        .resetPasswordLimitDate(LocalDateTime.now().minusDays(1)).build();

    given(userRepository.findByResetPasswordKey(anyString())).willReturn(Optional.of(user));

    // when
    UserException e =
        assertThrows(UserException.class, () -> userService.resetPassword("any", "any"));
    // then
    assertEquals(ErrorCode.RESET_PW_REQUEST_KEY_EXPIRED, e.getErrorCode());
  }

  // @Test
  // void successCheckResetPassword() {
  // // given
  // User user = User.builder().email("not-exist-email@naver1.com").name("noname")
  // .password("password").emailAuth(true).resetPasswordKey("resetKey")
  // .resetPasswordLimitDate(LocalDateTime.now().plusHours(2)).build();
  //
  // given(userRepository.findByResetPasswordKey(anyString())).willReturn(Optional.of(user));
  //
  // // when
  // boolean result = userService.checkResetPassword("resetKey");
  // // then
  // assertEquals(true, result);
  // }
  //
  // @Test
  // void failCheckResetPassword_keyNotFound() {
  // // given
  // given(userRepository.findByResetPasswordKey(anyString())).willReturn(Optional.empty());
  //
  // // when
  // boolean result = userService.checkResetPassword("resetKey");
  // // then
  // assertEquals(false, result);
  // }
  //
  // @Test
  // void failCheckResetPassword_limitDateNull() {
  // // given
  // User user =
  // User.builder().email("not-exist-email@naver1.com").name("noname").password("password")
  // .emailAuth(true).resetPasswordKey("resetKey").resetPasswordLimitDate(null).build();
  //
  // given(userRepository.findByResetPasswordKey(anyString())).willReturn(Optional.of(user));
  //
  // // when
  // Exception e = assertThrows(Exception.class, () -> userService.checkResetPassword("resetKey"));
  // // then
  // assertEquals("유효한 날짜가 아닙니다.", e.getMessage());
  // }
  //
  // @Test
  // void failCheckResetPassword_limitDateExpired() {
  // // given
  // User user = User.builder().email("not-exist-email@naver1.com").name("noname")
  // .password("password").emailAuth(true).resetPasswordKey("resetKey")
  // .resetPasswordLimitDate(LocalDateTime.now().minusHours(2)).build();
  //
  // given(userRepository.findByResetPasswordKey(anyString())).willReturn(Optional.of(user));
  //
  // // when
  // Exception e = assertThrows(Exception.class, () -> userService.checkResetPassword("resetKey"));
  // // then
  // assertEquals("유효한 날짜가 아닙니다.", e.getMessage());
  // }
}
