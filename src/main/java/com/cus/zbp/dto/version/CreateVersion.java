package com.cus.zbp.dto.version;

import com.cus.zbp.type.VersionAccessLevel;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class CreateVersion {

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Request {

    @NotNull
    private String name;

    @NotNull
    private long softwareId; // Client에서 softwareId를 알고 있어야 한다?

    @NotNull
    private VersionAccessLevel accessLevel;

    private MultipartFile uploadFile;

    private String uri;
  }

  @Getter
  @Setter
  @AllArgsConstructor
  @Builder
  public static class Response {
    private long id;
    private String name;
    // private String location; //dto에는 있지만 client에는 안넘겨줌
    private String softwareName;
    private VersionAccessLevel accessLevel;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    public static CreateVersion.Response from(VersionDto dto) {
      return CreateVersion.Response.builder().id(dto.getId()).name(dto.getName())
          .softwareName(dto.getSoftwareName()).accessLevel(dto.getAccessLevel())
          .createdDate(dto.getCreatedDate()).updatedDate(dto.getUpdatedDate()).build();
    }
  }
}
