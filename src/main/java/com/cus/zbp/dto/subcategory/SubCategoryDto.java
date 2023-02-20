package com.cus.zbp.dto.subcategory;

import com.cus.zbp.entity.SubCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class SubCategoryDto {
    private long id;
    private String name;
    private int number;
    private long testCategoryId;
    private String testCategoryName;
    private String abbreviation;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    public static SubCategoryDto from(SubCategory subCategory) {
        return SubCategoryDto.builder().id(subCategory.getId()).name(subCategory.getName())
                .number(subCategory.getNumber())
                .testCategoryId(subCategory.getTestCategory().getId())
                .testCategoryName(subCategory.getTestCategory().getName())
                .abbreviation(subCategory.getTestCategory().getAbbreviation())
                .createdDate(subCategory.getCreatedDate())
                .updatedDate(subCategory.getUpdatedDate()).build();
    }
}
