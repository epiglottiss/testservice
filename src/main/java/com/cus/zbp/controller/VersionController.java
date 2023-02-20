package com.cus.zbp.controller;

import com.cus.zbp.dto.version.CreateVersion;
import com.cus.zbp.dto.version.DeleteVersion;
import com.cus.zbp.dto.version.VersionDetail;
import com.cus.zbp.dto.version.VersionInfo;
import com.cus.zbp.dto.versionuser.CreateVersionUser;
import com.cus.zbp.dto.versionuser.DeleteVersionUser;
import com.cus.zbp.dto.versionuser.VersionUserDto;
import com.cus.zbp.exception.VersionException;
import com.cus.zbp.service.VersionService;
import com.cus.zbp.type.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/version")
@RestController
@RequiredArgsConstructor
public class VersionController {
    private final VersionService versionService;

    @PostMapping
    public ResponseEntity<CreateVersion.Response> createVersion(
            @Valid CreateVersion.Request request) throws IOException {

        String uri = request.getUri();
        MultipartFile multipartFile = request.getUploadFile();

        if (!multipartFile.isEmpty() && !StringUtils.isEmpty(uri)) {
            // multipart file과 uri 둘 다 입력되어 request 온 경우 어느 것을 저장하려는지 알 수 없으므로 예외처리
            throw new VersionException(ErrorCode.VERSION_CREATION_AMBIGUOUS);
        }

        // version의 software get 방법이 multipartFile과 uri 중 한가지만 가능하고,
        // 요청의 파라미터는 둘 다 포함이기 때문에, 둘 중 비어있지 않은 것을 선택하여 location 컬럼에 저장해야 함.
        // file과 uri 둘 다 파라미터로 전달하고, 저장할 것을 판단하는 것은 service에게 위임.
        return ResponseEntity
                .ok(CreateVersion.Response.from(versionService.createVersion(request.getName(),
                        request.getSoftwareId(), multipartFile, uri, request.getAccessLevel())));

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
            @RequestBody @Valid CreateVersionUser.Request request) {
        return ResponseEntity.ok(CreateVersionUser.Response.from(versionService.createUserAuthToVersion(request.getUserId(), request.getVersionId())));
    }

    @DeleteMapping("/userauth")
    public ResponseEntity<DeleteVersionUser.Response> deleteUserAuthOfVersion(
            @RequestBody @Valid DeleteVersionUser.Request request) {
        return ResponseEntity.ok(DeleteVersionUser.Response.from(versionService
                .deleteUserAuthOfVersion(request.getUserId(), request.getVersionId())));
    }

    @GetMapping("/userauth/versionid/{versionid}")
    public ResponseEntity<List<VersionUserDto>> getAllowedUserOfVersion(
            @PathVariable long versionid) {
        return ResponseEntity.ok(versionService.getAllowedUserOfVersion(versionid));
    }

}
