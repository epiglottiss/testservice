package com.cus.zbp.user.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.cus.zbp.components.MailComponents;
import com.cus.zbp.exception.UserException;
import com.cus.zbp.type.ErrorCode;
import com.cus.zbp.type.UserStatus;
import com.cus.zbp.user.entity.User;
import com.cus.zbp.user.model.PasswordResetInput;
import com.cus.zbp.user.model.UserInput;
import com.cus.zbp.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {
  private final UserRepository userRepository;
  private final MailComponents mailComponents;

  @Value("${app.server}")
  String server;

  @Value("${app.port}")
  String port;

  private String getEmailAuthHTMLBody(String uuid) {
    return "<p>링크를 클릭하셔서 가입을 완료 하세요.</p>" + "<div><a target='_blank' href='http://" + server + ":"
        + port + "/user/email-auth?id=" + uuid + "'> 가입 완료 </a></div>";
  }

  private String getResetPasswordHTMLBody(String uuid) {
    return "<p>비밀번호 초기화 메일입니다.<p><p>아래 링크를 클릭하셔서 비밀번호를 초기화 하세요.</p>"
        + "<div><a target='_blank' href='http://" + server + ":" + port + "/user/reset/password?id="
        + uuid + "'> 비밀번호 초기화 </a></div>";
  }

  @Transactional
  public void register(UserInput parameter) throws Exception {
    userRepository.findById(parameter.getEmail()).ifPresent(user -> {
      throw new UserException(ErrorCode.EMAIL_ALREADY_EXIST);
    });

    String encPassword = BCrypt.hashpw(parameter.getPassword(), BCrypt.gensalt());
    String uuid = UUID.randomUUID().toString();

    User user = User.builder().email(parameter.getEmail()).name(parameter.getName())
        .password(encPassword).registerDate(LocalDateTime.now()).emailAuth(false).emailAuthKey(uuid)
        .userStatus(UserStatus.MEMBER_STATUS_REQ).build();
    userRepository.save(user);

    String email = parameter.getEmail();
    String subject = "사이트 가입을 축하드립니다.";

    mailComponents.sendMail(email, subject, getEmailAuthHTMLBody(uuid));
  }

  @Transactional
  public void emailAuth(String uuid) throws Exception {

    User user = userRepository.findByEmailAuthKey(uuid)
        .orElseThrow(() -> new UserException(ErrorCode.AUTH_KEY_NOT_EXIST));

    if (user.isEmailAuth()) {
      throw new UserException(ErrorCode.AUTH_ALREADY_DONE);
    }

    user.setUserStatus(UserStatus.MEMBER_STATUS_ING);
    user.setEmailAuth(true);
    user.setEmailAuthDate(LocalDateTime.now());
    userRepository.save(user);
  }

  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<User> optionalUser = userRepository.findById(username);
    if (!optionalUser.isPresent()) {
      throw new UsernameNotFoundException("회원 정보가 존재하지 않습니다.");
    }

    User user = optionalUser.get();

    if (user.getUserStatus().equals(UserStatus.MEMBER_STATUS_REQ)) {
      throw new UsernameNotFoundException("이메일 인증 미완료 상태입니다.");
    }
    if (user.getUserStatus().equals(UserStatus.MEMBER_STATUS_STOP)) {
      throw new UsernameNotFoundException("이용 정지된 아이디입니다.");
    }
    if (user.getUserStatus().equals(UserStatus.MEMBER_STATUS_WITHDRAW)) {
      throw new UsernameNotFoundException("탈퇴한 아이디입니다.");
    }

    List<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

    return new org.springframework.security.core.userdetails.User(user.getEmail(),
        user.getPassword(), authorities);
  }

  @Transactional
  public void sendResetPassword(PasswordResetInput parameter) throws Exception {
    User user = userRepository.findByEmailAndName(parameter.getEmail(), parameter.getName())
        .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

    String uuid = UUID.randomUUID().toString();

    user.setResetPasswordKey(uuid);
    user.setResetPasswordLimitDate(LocalDateTime.now().plusDays(1));
    userRepository.save(user);

    String email = parameter.getEmail();
    String subject = "비밀번호 초기화 메일";
    mailComponents.sendMail(email, subject, getResetPasswordHTMLBody(uuid));
  }

  public void resetPassword(String id, String password) throws Exception {
    User user = userRepository.findByResetPasswordKey(id)
        .orElseThrow(() -> new UserException(ErrorCode.RESET_PW_REQUEST_KEY_NOT_FOUND));

    if (user.getResetPasswordLimitDate() == null) {
      throw new UserException(ErrorCode.RESET_PW_LIMIT_DATE_NULL);
    }

    if (user.getResetPasswordLimitDate().isBefore(LocalDateTime.now())) {
      throw new UserException(ErrorCode.RESET_PW_REQUEST_KEY_EXPIRED);
    }

    String encPassword = BCrypt.hashpw(password, BCrypt.gensalt());
    user.setPassword(encPassword);
    user.setResetPasswordKey("");
    user.setResetPasswordLimitDate(null);
    userRepository.save(user);
  }

}
