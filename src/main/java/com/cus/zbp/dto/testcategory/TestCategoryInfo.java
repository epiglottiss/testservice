package com.cus.zbp.dto.testcategory;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class TestCategoryInfo {

  private long id;
  private String name;
  private String abbreviation;
  private long softwareId;
  private String softwareName;
  private LocalDateTime createdDate;
  private LocalDateTime updatedDate;

  public static TestCategoryInfo fromDto(TestCategoryDto dto) {
    return TestCategoryInfo.builder().id(dto.getId()).name(dto.getName())
        .abbreviation(dto.getAbbreviation()).softwareId(dto.getSoftwareId())
        .softwareName(dto.getSoftwareName()).createdDate(dto.getCreatedDate())
        .updatedDate(dto.getUpdatedDate()).build();
  }
}
