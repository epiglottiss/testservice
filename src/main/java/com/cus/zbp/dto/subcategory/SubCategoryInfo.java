package com.cus.zbp.dto.subcategory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class SubCategoryInfo {

  private long id;
  private String name;
  private int number;
  private long testCategoryId;
  private String testCategoryName;
  private LocalDateTime createdDate;
  private LocalDateTime updatedDate;

  public static SubCategoryInfo fromDto(SubCategoryDto dto) {
    return SubCategoryInfo.builder().id(dto.getId()).name(dto.getName())
        .number(dto.getNumber()).testCategoryId(dto.getTestCategoryId())
        .testCategoryName(dto.getTestCategoryName()).createdDate(dto.getCreatedDate())
        .updatedDate(dto.getUpdatedDate()).build();
  }
}
