package com.cus.zbp.dto.version;

import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import com.cus.zbp.type.VersionAccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class DeleteVersion {

  @Getter
  @Setter
  @AllArgsConstructor
  public static class Request {
    private long versionId;

    private long userId;

    @NotNull
    private String name;

    private long softwareId; // Client에서 softwareId를 알고 있어야 한다?
  }

  @Getter
  @Setter
  @AllArgsConstructor
  @Builder
  public static class Response {
    private long id;
    private String name;
    private String location;
    private String softwareName;
    private VersionAccessLevel accessLevel;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    public static DeleteVersion.Response from(VersionDto dto) {
      return DeleteVersion.Response.builder().id(dto.getId()).name(dto.getName())
          .location(dto.getLocation()).softwareName(dto.getSoftwareName())
          .accessLevel(dto.getAccessLevel()).createdDate(dto.getCreatedDate())
          .updatedDate(dto.getUpdatedDate()).build();
    }
  }
}
