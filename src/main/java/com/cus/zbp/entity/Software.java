package com.cus.zbp.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import com.cus.zbp.type.SoftwareType;
import com.cus.zbp.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Entity
@Builder
public class Software extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  private String name;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "email", columnDefinition = "email")
  private User user;

  @Enumerated(EnumType.STRING)
  private SoftwareType type;

  public void updateInfo(String name, SoftwareType type) {
    this.name = name;
    this.type = type;
  }
}
