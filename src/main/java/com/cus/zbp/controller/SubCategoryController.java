package com.cus.zbp.controller;

import com.cus.zbp.dto.subcategory.CreateSubCategory;
import com.cus.zbp.dto.subcategory.SubCategoryInfo;
import com.cus.zbp.service.SubCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/subcategory")
@RestController
@RequiredArgsConstructor
public class SubCategoryController {
  private final SubCategoryService subCategoryService;

  @PostMapping
  public ResponseEntity<CreateSubCategory.Response> createSubCategory(
      @RequestBody @Valid CreateSubCategory.Request request) {

    return ResponseEntity.ok(
        CreateSubCategory.Response.from(subCategoryService.createSubCategory(request.getName(),
             request.getTestCategoryId())));
  }

  @GetMapping("/categoryid/{categoryid}")
  public ResponseEntity<List<SubCategoryInfo>> getSubCategoriesByTestCategoryId(@PathVariable long categoryid) {
    return ResponseEntity.ok(subCategoryService.getSubCategoriesByTestCategoryId(categoryid).stream()
            .map(SubCategoryInfo::fromDto).collect(Collectors.toList()));
  }

}
