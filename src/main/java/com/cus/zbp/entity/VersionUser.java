package com.cus.zbp.entity;

import javax.persistence.*;

import com.cus.zbp.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Entity
@Builder
public class VersionUser {
  @Id
  private long id;

  @ManyToOne
  @JoinColumn(name = "version_id", insertable = false, updatable = false)
  private Version version;

  @ManyToOne
  @JoinColumn(name = "user_id", insertable = false, updatable = false)
  private User user;

}
