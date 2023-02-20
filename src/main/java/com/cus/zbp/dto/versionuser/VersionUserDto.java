package com.cus.zbp.dto.versionuser;

import com.cus.zbp.entity.Version;
import com.cus.zbp.entity.VersionUser;
import com.cus.zbp.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class VersionUserDto {
  private long versionId;
  private String versionName;
  private String softwareName;

  private long userId;
  private String userEamil;

  public static VersionUserDto from(VersionUser versionUser) {
    Version version = versionUser.getVersion();
    User user = versionUser.getUser();
    return VersionUserDto.builder().versionId(version.getId()).versionName(version.getName())
        .softwareName(version.getSoftware().getName()).userId(user.getId())
        .userEamil(user.getEmail()).build();
  }
}
