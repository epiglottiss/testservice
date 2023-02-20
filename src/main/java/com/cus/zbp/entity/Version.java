package com.cus.zbp.entity;

import java.util.List;
import javax.persistence.*;

import com.cus.zbp.type.VersionAccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Entity
@Builder
public class Version extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  private String name;
  private String location; // 파일 저장 위치 기록
  @Enumerated(EnumType.STRING)
  private VersionAccessLevel accessLevel;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "software_id")
  private Software software;

  @OneToMany(mappedBy = "version")
  private List<VersionUser> versionUser;
}
