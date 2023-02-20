package com.cus.zbp.controller;

import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;

import com.cus.zbp.dto.software.UpdateSoftware;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.cus.zbp.dto.software.CreateSoftware;
import com.cus.zbp.dto.software.DeleteSoftware;
import com.cus.zbp.dto.software.SoftwareInfo;
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
    public ResponseEntity<SoftwareInfo> getSoftwareByName(@PathVariable String name) {
        return ResponseEntity.ok(SoftwareInfo.fromDto(softwareService.getSoftwareByName(name)));
    }

    @PutMapping
    public ResponseEntity<UpdateSoftware.Response> updateSoftware(
            @RequestBody @Valid UpdateSoftware.Request request) {
        return ResponseEntity.ok(UpdateSoftware.Response.from(softwareService
                .updateSoftware(request.getId(), request.getName(), request.getType())));
    }

    @DeleteMapping
    public ResponseEntity<DeleteSoftware.Response> deleteSoftware(
            @RequestBody @Valid DeleteSoftware.Request request) {
        return ResponseEntity.ok(DeleteSoftware.Response
                .from(softwareService.deleteSoftware(request.getName(), request.getUserEmail())));

    }
}
