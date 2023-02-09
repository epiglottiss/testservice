package com.cus.zbp.controller;

import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cus.zbp.dto.version.CreateVersion;
import com.cus.zbp.dto.version.DeleteVersion;
import com.cus.zbp.dto.version.VersionDetail;
import com.cus.zbp.dto.version.VersionInfo;
import com.cus.zbp.service.VersionService;
import lombok.RequiredArgsConstructor;

@RequestMapping("/version")
@RestController
@RequiredArgsConstructor
public class VersionController {
  private final VersionService versionService;

  @PostMapping
  public ResponseEntity<CreateVersion.Response> createVersion(
      @RequestBody @Valid CreateVersion.Request request) {
    // TODO: file S3 저장 구현하고 저장 위치를 service에 넘겨 줌
    String location = "";
    return ResponseEntity
        .ok(CreateVersion.Response.from(versionService.createVersion(request.getName(),
            request.getSoftwareId(), location, request.getAccessLevel())));
  }

  // TODO: api 호출한 사람의 권한에 따라 반환하는 List 달라질 것 구현
  @GetMapping("/softwarename/{softwarename}")
  public ResponseEntity<List<VersionInfo>> getVersionsBySoftwareName(
      @PathVariable String softwarename) {
    return ResponseEntity.ok(versionService.getVersionsBySoftwareName(softwarename).stream()
        .map(VersionInfo::fromDto).collect(Collectors.toList()));
  }

  @GetMapping("/detail")
  public ResponseEntity<VersionDetail.Response> getVersionDetail(
      @RequestBody @Valid VersionDetail.Request request) {
    return ResponseEntity.ok(VersionDetail.Response.from(versionService
        .getVersionDetail(request.getUserId(), request.getSoftwareId(), request.getVersionId())));
  }

  @DeleteMapping
  public ResponseEntity<DeleteVersion.Response> deleteVersion(
      @RequestBody @Valid DeleteVersion.Request request) {
    return ResponseEntity.ok(DeleteVersion.Response.from(versionService
        .deleteVersion(request.getUserId(), request.getSoftwareId(), request.getVersionId())));
  }

}
