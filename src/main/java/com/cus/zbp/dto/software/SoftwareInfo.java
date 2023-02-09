package com.cus.zbp.dto.software;

import java.time.LocalDateTime;
import com.cus.zbp.type.SoftwareType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class SoftwareInfo {
  private String name;
  private String userEmail;
  private SoftwareType type;
  private LocalDateTime createdDate;
  private LocalDateTime updatedDate;

  public static SoftwareInfo fromDto(SoftwareDto dto) {
    return SoftwareInfo.builder().name(dto.getName()).userEmail(dto.getUserEmail())
        .type(dto.getType()).createdDate(dto.getCreatedDate()).updatedDate(dto.getUpdatedDate())
        .build();
  }
}
