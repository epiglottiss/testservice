package com.cus.zbp.user.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import com.cus.zbp.entity.Software;
import com.cus.zbp.entity.VersionUser;
import com.cus.zbp.type.UserStatus;
import lombok.Builder;
import lombok.Data;

@Entity
@Data
@Builder
public class User {
  @Id
  String email;
  String password;
  String name;
  LocalDateTime registerDate;
  LocalDateTime updateDate;

  boolean emailAuth;
  LocalDateTime emailAuthDate;
  String emailAuthKey;

  UserStatus userStatus;

  String resetPasswordKey;
  LocalDateTime resetPasswordLimitDate;

  // software relation
  @OneToMany(mappedBy = "user")
  private List<Software> software;

  @OneToMany(mappedBy = "user")
  private List<VersionUser> versionUser;
}
