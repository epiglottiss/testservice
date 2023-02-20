package com.cus.zbp.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.cus.zbp.dto.testcategory.TestCategoryDto;
import com.cus.zbp.entity.Software;
import com.cus.zbp.entity.TestCategory;
import com.cus.zbp.exception.SoftwareException;
import com.cus.zbp.exception.TestCategoryException;
import com.cus.zbp.repository.SoftwareRepository;
import com.cus.zbp.repository.TestCategoryRepository;
import com.cus.zbp.type.ErrorCode;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TestCategoryService {
  private final SoftwareRepository softwareRepository;
  private final TestCategoryRepository testCategoryRepository;

  @Transactional
  public TestCategoryDto createTestCategory(String name, String abbreviation, long softwareId) {
    Software software = softwareRepository.findById(softwareId)
        .orElseThrow(() -> new SoftwareException(ErrorCode.SOFTWARE_NOT_EXIST));

    testCategoryRepository.findByNameAndSoftware_Id(name, softwareId).ifPresent(cat -> {
      throw new TestCategoryException(ErrorCode.TEST_CATEGORY_NAME_ALREADY_EXIST);
    });

    testCategoryRepository.findByAbbreviationAndSoftware_Id(abbreviation, softwareId)
        .ifPresent(cat -> {
          throw new TestCategoryException(ErrorCode.TEST_CATEGORY_NAME_ALREADY_EXIST);
        });

    return TestCategoryDto.from(testCategoryRepository.save(
        TestCategory.builder().name(name).abbreviation(abbreviation).software(software).build()));
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
}
