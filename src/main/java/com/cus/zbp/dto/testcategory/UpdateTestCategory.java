package com.cus.zbp.dto.testcategory;

import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class UpdateTestCategory {

  @Getter
  @Setter
  @AllArgsConstructor
  public static class Request {

    private long id;

    @NotNull
    private String name;

    @NotNull
    private String abbreviation;

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

    public static UpdateTestCategory.Response fromDto(TestCategoryDto dto) {
      return UpdateTestCategory.Response.builder().id(dto.getId()).name(dto.getName())
          .softwareId(dto.getSoftwareId()).softwareName(dto.getSoftwareName())
          .abbreviation(dto.getAbbreviation()).createdDate(dto.getCreatedDate())
          .updatedDate(dto.getUpdatedDate()).build();
    }
  }
}
