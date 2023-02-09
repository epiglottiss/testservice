package com.cus.zbp.dto.version;

import java.time.LocalDateTime;
import com.cus.zbp.entity.Version;
import com.cus.zbp.type.VersionAccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class VersionDto {
  private long id;
  private String name;
  private String location;
  // private long softwareId; Join된 software name만 넘겨주고 id는 dto에서 안다뤄도 될듯
  private String softwareName;
  private VersionAccessLevel accessLevel;
  private LocalDateTime createdDate;
  private LocalDateTime updatedDate;

  public static VersionDto from(Version version) {
    return VersionDto.builder().id(version.getId()).name(version.getName())
        .location(version.getLocation()).softwareName(version.getSoftware().getName())
        .accessLevel(version.getAccessLevel()).createdDate(version.getCreatedDate())
        .updatedDate(version.getUpdatedDate()).build();
  }
}
