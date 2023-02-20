package com.cus.zbp.dto.software;

import com.cus.zbp.type.SoftwareType;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class UpdateSoftware {

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Request {

    private long id;
    @NotNull
    private String name;

    @NotNull
    private SoftwareType type;
  }

  @Getter
  @Setter
  @AllArgsConstructor
  @Builder
  public static class Response {
    private long id;
    private String name;
    private SoftwareType type;
    private String userEmail;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    public static UpdateSoftware.Response from(SoftwareDto dto) {
      return UpdateSoftware.Response.builder().id(dto.getId()).name(dto.getName()).userEmail(dto.getUserEmail())
          .type(dto.getType()).createdDate(dto.getCreatedDate()).updatedDate(dto.getUpdatedDate())
          .build();
    }
  }
}
