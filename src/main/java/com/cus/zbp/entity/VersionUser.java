package com.cus.zbp.entity;

import javax.persistence.*;

import com.cus.zbp.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Entity
@Builder
public class VersionUser {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @ManyToOne
  @JoinColumn(name = "version_id")
  private Version version;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

}
