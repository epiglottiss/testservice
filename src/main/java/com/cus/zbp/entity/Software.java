package com.cus.zbp.entity;

import java.util.Collection;
import javax.persistence.*;

import com.cus.zbp.type.SoftwareType;
import com.cus.zbp.user.entity.User;
import lombok.*;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Entity
@Builder
public class Software extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(unique = true)
  private String name;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @Enumerated(EnumType.STRING)
  private SoftwareType type;

  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "version_id")
  private Collection<Version> versions;

  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id")
  private Collection<TestCategory> testCategories;

  public void updateInfo(String name, SoftwareType type) {
    this.name = name;
    this.type = type;
  }
}
