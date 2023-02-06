package com.cus.zbp.user.entity;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Builder;
import lombok.Data;

@Entity
@Data
@Builder
public class User implements UserStatusCode {
  @Id
  String email;
  String password;
  String name;
  LocalDateTime registerDate;
  LocalDateTime updateDate;

  boolean emailAuth;
  LocalDateTime emailAuthDate;
  String emailAuthKey;

  String userStatus;

  String resetPasswordKey;
  LocalDateTime resetPasswordLimitDate;
}
