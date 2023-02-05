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
import com.cus.zbp.dto.CreateSoftware;
import com.cus.zbp.dto.DeleteSoftware;
import com.cus.zbp.dto.SoftwareInfo;
import com.cus.zbp.service.SoftwareService;
import lombok.RequiredArgsConstructor;

@RequestMapping("/software")
@RestController
@RequiredArgsConstructor
public class SoftwareController {
  private final SoftwareService softwareService;

  @PostMapping
  public ResponseEntity<CreateSoftware.Response> createSoftware(
      @RequestBody @Valid CreateSoftware.Request request) {
    return ResponseEntity.ok(CreateSoftware.Response.from(softwareService
        .createSoftware(request.getName(), request.getUserEmail(), request.getType())));
  }

  @GetMapping("/email/{email}")
  public ResponseEntity<List<SoftwareInfo>> getSoftwareListByEmail(@PathVariable String email) {
    return ResponseEntity.ok(softwareService.getSoftwareListByEmail(email).stream()
        .map(SoftwareInfo::fromDto).collect(Collectors.toList()));
  }

  @GetMapping("/name/{name}")
  public ResponseEntity<SoftwareInfo> getSoftwareListByName(@PathVariable String name) {
    return ResponseEntity.ok(SoftwareInfo.fromDto(softwareService.getSoftwareByName(name)));
  }

  // @PutMapping
  // public ResponseEntity<UpdateSoftware.Response> updateSoftware(
  // @RequestBody @Valid UpdateSoftware.Request request) {
  // return ResponseEntity.ok(UpdateSoftware.Response.from(softwareService
  // .updateSoftware(request.getName(), request.getUserEmail(), request.getType())));
  // }

  @DeleteMapping
  public ResponseEntity<DeleteSoftware.Response> deleteSoftware(
      @RequestBody @Valid DeleteSoftware.Request request) {
    return ResponseEntity.ok(DeleteSoftware.Response
        .from(softwareService.deleteSoftware(request.getName(), request.getUserEmail())));

  }
}
