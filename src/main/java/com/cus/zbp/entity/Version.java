package com.cus.zbp.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import com.cus.zbp.type.VersionAccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Entity
@Builder
public class Version extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  private String name;
  private String location; // 파일 저장 위치 기록
  private VersionAccessLevel accessLevel;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "software_id")
  private Software software;
}
