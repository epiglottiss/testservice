package com.cus.zbp.dto.subcategory;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class CreateSubCategory {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {

        @NotNull
        private String name;

        private long testCategoryId;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @Builder
    public static class Response {
        private long id;
        private String name;
        private int number;
        private long testCategoryId;
        private String testCategoryName;
        private String abbreviation;
        private LocalDateTime createdDate;
        private LocalDateTime updatedDate;

        public static CreateSubCategory.Response from(SubCategoryDto dto) {
            return Response.builder().id(dto.getId()).name(dto.getName())
                    .number(dto.getNumber())
                    .testCategoryId(dto.getTestCategoryId())
                    .testCategoryName((dto.getTestCategoryName()))
                    .abbreviation(dto.getAbbreviation())
                    .createdDate(dto.getCreatedDate()).updatedDate(dto.getUpdatedDate()).build();
        }
    }
}
