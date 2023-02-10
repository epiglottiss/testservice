package com.cus.zbp.dto.testcategory;

import java.time.LocalDateTime;
import com.cus.zbp.entity.TestCategory;
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
public class TestCategoryDto {
  private long id;
  private String name;
  private String abbreviation;
  private long softwareId;
  private String softwareName;
  private LocalDateTime createdDate;
  private LocalDateTime updatedDate;

  public static TestCategoryDto from(TestCategory testCategory) {
    return TestCategoryDto.builder().id(testCategory.getId()).name(testCategory.getName())
        .abbreviation(testCategory.getAbbreviation())
        .softwareId(testCategory.getSoftware().getId())
        .softwareName(testCategory.getSoftware().getName())
        .createdDate(testCategory.getCreatedDate())
        .updatedDate(testCategory.getUpdatedDate()).build();
  }
}
