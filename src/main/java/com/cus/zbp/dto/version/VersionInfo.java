package com.cus.zbp.dto.version;

import java.time.LocalDateTime;
import com.cus.zbp.type.VersionAccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class VersionInfo {

  private String name;
  private String location; // EXECUTABLE, ARCHIVE인 경우 다운로드 받을 수 있는 url // WEBSITE는 url
  private VersionAccessLevel accessLevel;
  private String softwareName;
  private LocalDateTime createdDate;
  private LocalDateTime updatedDate;

  public static VersionInfo fromDto(VersionDto dto) {
    return VersionInfo.builder().name(dto.getName()).location(dto.getLocation())
        .accessLevel(dto.getAccessLevel()).softwareName(dto.getSoftwareName())
        .createdDate(dto.getCreatedDate()).updatedDate(dto.getUpdatedDate()).build();
  }
}
