package com.cus.zbp.dto;

import java.time.LocalDateTime;
import com.cus.zbp.entity.Software;
import com.cus.zbp.type.SoftwareType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class SoftwareDto {

  private String name;
  private String userEmail;
  private SoftwareType type;
  private LocalDateTime createdDate;
  private LocalDateTime updatedDate;

  public static SoftwareDto from(Software software) {
    return SoftwareDto.builder().name(software.getName()).type(software.getType())
        .userEmail(software.getUser().getEmail()).createdDate(software.getCreatedDate())
        .updatedDate(software.getUpdatedDate()).build();
  }
}
