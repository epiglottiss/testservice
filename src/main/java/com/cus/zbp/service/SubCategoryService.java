package com.cus.zbp.service;

import com.cus.zbp.dto.subcategory.SubCategoryDto;
import com.cus.zbp.dto.testcategory.TestCategoryDto;
import com.cus.zbp.entity.SubCategory;
import com.cus.zbp.entity.TestCategory;
import com.cus.zbp.exception.TestCategoryException;
import com.cus.zbp.repository.SoftwareRepository;
import com.cus.zbp.repository.SubCategoryRepository;
import com.cus.zbp.repository.TestCategoryRepository;
import com.cus.zbp.type.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubCategoryService {
  private final SoftwareRepository softwareRepository;
  private final TestCategoryRepository testCategoryRepository;
  private final SubCategoryRepository subCategoryRepository;

  @Transactional
  public SubCategoryDto createSubCategory(String name, long testCategoryId) {
    TestCategory category = testCategoryRepository.findById(testCategoryId).orElseThrow(() -> new TestCategoryException(ErrorCode.TEST_CATEGORY_ID_NOT_FOUND));

    Optional<SubCategory> subCategory = subCategoryRepository.findFirstByTestCategoryIdOrderByNumberDesc(testCategoryId);
    int newNumber = subCategory.isEmpty()?1:subCategory.get().getNumber()+1;

    return SubCategoryDto.from(subCategoryRepository.save(
        SubCategory.builder().name(name).number(newNumber).testCategory(category).build()));
  }

  @Transactional(readOnly = true)
  public TestCategoryDto getTestCategoryById(long id) {
    return TestCategoryDto.from(testCategoryRepository.findById(id)
        .orElseThrow(() -> new TestCategoryException(ErrorCode.TEST_CATEGORY_ID_NOT_FOUND)));
  }

  @Transactional
  public TestCategoryDto updateTestCategory(long id, String name, String abbreviation) {
    TestCategory testCategory = testCategoryRepository.findById(id)
        .orElseThrow(() -> new TestCategoryException(ErrorCode.TEST_CATEGORY_ID_NOT_FOUND));

    testCategory.update(name, abbreviation);

    return TestCategoryDto.from(testCategory);
  }

  @Transactional
  public TestCategoryDto deleteVersion(long id) {
    TestCategory testCategory = testCategoryRepository.findById(id)
        .orElseThrow(() -> new TestCategoryException(ErrorCode.TEST_CATEGORY_ID_NOT_FOUND));
    testCategoryRepository.delete(testCategory);
    return TestCategoryDto.from(testCategory);
  }

  @Transactional(readOnly = true)
  public List<SubCategoryDto> getSubCategoriesByTestCategoryId(long categoryid) {
    testCategoryRepository.findById(categoryid).orElseThrow(()->new TestCategoryException(ErrorCode.TEST_CATEGORY_ID_NOT_FOUND));
    return subCategoryRepository.findByTestCategoryId(categoryid).stream().map(SubCategoryDto::from).collect(Collectors.toList());
  }
}
