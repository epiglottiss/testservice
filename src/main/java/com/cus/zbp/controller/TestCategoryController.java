package com.cus.zbp.controller;

import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cus.zbp.dto.testcategory.CreateTestCategory;
import com.cus.zbp.dto.testcategory.DeleteTestCategory;
import com.cus.zbp.dto.testcategory.TestCategoryInfo;
import com.cus.zbp.dto.testcategory.UpdateTestCategory;
import com.cus.zbp.service.TestCategoryService;
import lombok.RequiredArgsConstructor;

@RequestMapping("/testcategory")
@RestController
@RequiredArgsConstructor
public class TestCategoryController {
  private final TestCategoryService testCategoryService;

  @PostMapping
  public ResponseEntity<CreateTestCategory.Response> createTestCategory(
      @RequestBody @Valid CreateTestCategory.Request request) {

    return ResponseEntity.ok(
        CreateTestCategory.Response.from(testCategoryService.createTestCategory(request.getName(),
            request.getAbbreviation(), request.getSoftwareId())));
  }

  @GetMapping("/id/{id}")
  public ResponseEntity<TestCategoryInfo> getTestCategoryById(@PathVariable long id) {
    return ResponseEntity.ok(TestCategoryInfo.fromDto(testCategoryService.getTestCategoryById(id)));
  }

  @PutMapping
  public ResponseEntity<UpdateTestCategory.Response> updateTestCategory(
      @RequestBody @Valid UpdateTestCategory.Request request) {
    return ResponseEntity.ok(UpdateTestCategory.Response.fromDto(testCategoryService
        .updateTestCategory(request.getId(), request.getName(), request.getAbbreviation())));
  }

  @DeleteMapping
  public ResponseEntity<DeleteTestCategory.Response> deleteTestCategory(
      @RequestBody @Valid DeleteTestCategory.Request request) {
    return ResponseEntity
        .ok(DeleteTestCategory.Response.from(testCategoryService.deleteVersion(request.getId())));
  }

}
