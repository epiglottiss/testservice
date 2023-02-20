package com.cus.zbp.dto.testcategory;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class DeleteTestCategory {

  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class Request {
    private long Id;
  }

  @Getter
  @Setter
  @AllArgsConstructor
  @Builder
  public static class Response {
    private long id;
    private String name;
    private long softwareId;
    private String softwareName;
    private String abbreviation;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    public static DeleteTestCategory.Response from(TestCategoryDto dto) {
      return DeleteTestCategory.Response.builder().id(dto.getId()).name(dto.getName())
          .softwareId(dto.getSoftwareId()).softwareName(dto.getSoftwareName())
          .abbreviation(dto.getAbbreviation()).createdDate(dto.getCreatedDate())
          .updatedDate(dto.getUpdatedDate()).build();
    }
  }
}
