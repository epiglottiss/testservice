package com.cus.zbp.controller;

import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;

import com.cus.zbp.dto.versionuser.CreateVersionUser;
import com.cus.zbp.dto.versionuser.DeleteVersionUser;
import com.cus.zbp.dto.versionuser.VersionUserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

  @PostMapping("/userauth")
  public ResponseEntity<CreateVersionUser.Response> createUserAuthToVersion(
          @RequestBody @Valid CreateVersionUser.Request request){
    return ResponseEntity.ok(CreateVersionUser.Response.from(versionService.createUserAuthToVersion(request.getUserId(),request.getVersionId())));
  }

  @DeleteMapping("/userauth")
  public ResponseEntity<DeleteVersionUser.Response> deleteUserAuthOfVersion(
          @RequestBody @Valid DeleteVersionUser.Request request) {
    return ResponseEntity.ok(DeleteVersionUser.Response.from(versionService
            .deleteUserAuthOfVersion(request.getUserId(),request.getVersionId())));
  }

  @GetMapping("/userauth/versionId/{versionId}")
  public ResponseEntity<List<VersionUserDto>> getAllowedUserOfVersion(
          @PathVariable long versionId) {
    return ResponseEntity.ok(versionService.getAllowedUserOfVersion(versionId));
  }

}
