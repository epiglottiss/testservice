package com.cus.zbp.user.entity;

import com.cus.zbp.entity.Software;
import com.cus.zbp.entity.VersionUser;
import com.cus.zbp.type.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Data
@Builder
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  private String email;
  private String password;
  private String name;
  private LocalDateTime registerDate;
  private LocalDateTime updateDate;

  private boolean emailAuth;
  private LocalDateTime emailAuthDate;
  private String emailAuthKey;

  private UserStatus userStatus;

  private String resetPasswordKey;
  private LocalDateTime resetPasswordLimitDate;

  // software relation
  @OneToMany(mappedBy = "user")
  private List<Software> software;

  @OneToMany(mappedBy = "user")
  private List<VersionUser> versionUser;
}
