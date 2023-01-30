package com.cus.zbp.user.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import com.cus.zbp.components.MailComponents;
import com.cus.zbp.user.entity.User;
import com.cus.zbp.user.model.PasswordResetInput;
import com.cus.zbp.user.model.UserInput;
import com.cus.zbp.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;
  private final MailComponents mailComponents;

  @Override
  public boolean register(UserInput parameter) {
    Optional<User> optionalUser = userRepository.findById(parameter.getEmail());
    // 아이디 존재하는 경우
    if (optionalUser.isPresent()) {
      return false;
    }

    String encPassword = BCrypt.hashpw(parameter.getPassword(), BCrypt.gensalt());
    String uuid = UUID.randomUUID().toString();

    User user = User.builder().email(parameter.getEmail()).name(parameter.getName())
        .password(encPassword).registerDate(LocalDateTime.now()).emailAuth(false).emailAuthKey(uuid)
        .userStatus(User.MEMBER_STATUS_REQ).build();
    userRepository.save(user);

    String email = parameter.getEmail();
    String subject = "사이트 가입을 축하드립니다.";
    String text = "<p>사이트 가입을 축하드립니다.<p><p>아래 링크를 클릭하셔서 가입을 완료 하세요.</p>"
        + "<div><a target='_blank' href='http://localhost:8080/user/email-auth?id=" + uuid
        + "'> 가입 완료 </a></div>";
    return mailComponents.sendMail(email, subject, text);
  }

  @Override
  public boolean emailAuth(String uuid) {

    Optional<User> optionalUser = userRepository.findByEmailAuthKey(uuid);
    if (!optionalUser.isPresent()) {
      return false;
    }

    User user = optionalUser.get();

    if (user.isEmailAuth()) {
      return false;
    }

    user.setUserStatus(User.MEMBER_STATUS_ING);
    user.setEmailAuth(true);
    user.setEmailAuthDate(LocalDateTime.now());
    userRepository.save(user);

    return true;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<User> optionalUser = userRepository.findById(username);
    if (!optionalUser.isPresent()) {
      throw new UsernameNotFoundException("회원 정보가 존재하지 않습니다.");
    }

    User user = optionalUser.get();

    if (user.getUserStatus().equals(User.MEMBER_STATUS_REQ)) {
      throw new UsernameNotFoundException("이메일 인증 미완료 상태입니다.");
    }
    if (user.getUserStatus().equals(User.MEMBER_STATUS_STOP)) {
      throw new UsernameNotFoundException("이용 정지된 아이디입니다.");
    }
    if (user.getUserStatus().equals(User.MEMBER_STATUS_WITHDRAW)) {
      throw new UsernameNotFoundException("탈퇴한 아이디입니다.");
    }

    List<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

    return new org.springframework.security.core.userdetails.User(user.getEmail(),
        user.getPassword(), authorities);
  }

  @Override
  public boolean sendResetPassword(PasswordResetInput parameter) {
    Optional<User> optionalUser =
        userRepository.findByEmailAndName(parameter.getEmail(), parameter.getName());
    if (!optionalUser.isPresent()) {
      throw new UsernameNotFoundException("회원 정보가 존재하지 않습니다.");
    }

    User user = optionalUser.get();

    String uuid = UUID.randomUUID().toString();

    user.setResetPasswordKey(uuid);
    user.setResetPasswordLimitDate(LocalDateTime.now().plusDays(1));
    userRepository.save(user);

    String email = parameter.getEmail();
    String subject = "비밀번호 초기화 메일";
    String text = "<p>비밀번호 초기화 메일입니다.<p><p>아래 링크를 클릭하셔서 비밀번호를 초기화 하세요.</p>"
        + "<div><a target='_blank' href='http://localhost:8080/user/reset/password?id=" + uuid
        + "'> 비밀번호 초기화 </a></div>";
    return mailComponents.sendMail(email, subject, text);
  }

  @Override
  public boolean resetPassword(String id, String password) {
    Optional<User> optionalUser = userRepository.findByResetPasswordKey(id);
    if (!optionalUser.isPresent()) {
      throw new UsernameNotFoundException("회원 정보가 존재하지 않습니다.");
    }

    User user = optionalUser.get();

    if (user.getResetPasswordLimitDate() == null) {
      throw new RuntimeException("유효한 날짜가 아닙니다.");
    }

    if (user.getResetPasswordLimitDate().isBefore(LocalDateTime.now())) {
      throw new RuntimeException("유효한 날짜가 아닙니다.");
    }

    String encPassword = BCrypt.hashpw(password, BCrypt.gensalt());
    user.setPassword(encPassword);
    user.setResetPasswordKey("");
    user.setResetPasswordLimitDate(null);
    userRepository.save(user);

    return true;
  }

  @Override
  public boolean checkResetPassword(String id) {
    Optional<User> optionalUser = userRepository.findByResetPasswordKey(id);
    if (!optionalUser.isPresent()) {
      return false;
    }
    User user = optionalUser.get();

    if (user.getResetPasswordLimitDate() == null) {
      throw new RuntimeException("유효한 날짜가 아닙니다.");
    }

    if (user.getResetPasswordLimitDate().isBefore(LocalDateTime.now())) {
      throw new RuntimeException("유효한 날짜가 아닙니다.");
    }
    return true;
  }


}
