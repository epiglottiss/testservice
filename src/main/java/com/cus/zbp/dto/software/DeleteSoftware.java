package com.cus.zbp.dto.software;

import java.time.LocalDateTime;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import com.cus.zbp.type.SoftwareType;
import lombok.*;

public class DeleteSoftware {

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Request {
    @NotNull
    private String name;

    @Email
    @NotNull
    private String userEmail;
  }

  @Getter
  @Setter
  @AllArgsConstructor
  @Builder
  public static class Response {
    private String name;
    private SoftwareType type;
    private String userEmail;
    private LocalDateTime deletedDate;

    public static DeleteSoftware.Response from(SoftwareDto dto) {
      return DeleteSoftware.Response.builder().name(dto.getName()).userEmail(dto.getUserEmail())
          .type(dto.getType()).deletedDate(LocalDateTime.now()).build();
    }
  }
}
