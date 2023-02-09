package com.cus.zbp.dto.software;

import java.time.LocalDateTime;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import com.cus.zbp.type.SoftwareType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class UpdateSoftware {

  @Getter
  @Setter
  @AllArgsConstructor
  public static class Request {
    @NotNull
    private String name;

    @Email
    @NotNull
    private String userEmail;

    @NotNull
    private SoftwareType type;
  }

  @Getter
  @Setter
  @AllArgsConstructor
  @Builder
  public static class Response {
    private String name;
    private SoftwareType type;
    private String userEmail;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    public static UpdateSoftware.Response from(SoftwareDto dto) {
      return UpdateSoftware.Response.builder().name(dto.getName()).userEmail(dto.getUserEmail())
          .type(dto.getType()).createdDate(dto.getCreatedDate()).updatedDate(dto.getUpdatedDate())
          .build();
    }
  }
}
