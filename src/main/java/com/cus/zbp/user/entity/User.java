package com.cus.zbp.user.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import com.cus.zbp.entity.Software;
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

  // software relation
  @OneToMany(mappedBy = "user")
  private List<Software> softwares = new ArrayList<>();
}
