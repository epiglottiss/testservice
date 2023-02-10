package com.cus.zbp.dto.testcategory;

import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class CreateTestCategory {

  @Getter
  @Setter
  @AllArgsConstructor
  public static class Request {

    @NotNull
    private String name;

    @NotNull
    private String abbreviation;

    private long softwareId;
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

    public static CreateTestCategory.Response from(TestCategoryDto dto) {
      return CreateTestCategory.Response.builder().id(dto.getId()).name(dto.getName())
          .softwareId(dto.getSoftwareId()).softwareName(dto.getSoftwareName())
          .abbreviation(dto.getAbbreviation()).softwareName(dto.getSoftwareName())
          .createdDate(dto.getCreatedDate()).updatedDate(dto.getUpdatedDate()).build();
    }
  }
}
