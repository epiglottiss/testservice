package com.cus.zbp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;
import com.cus.zbp.dto.testcategory.TestCategoryDto;
import com.cus.zbp.entity.Software;
import com.cus.zbp.entity.TestCategory;
import com.cus.zbp.repository.SoftwareRepository;
import com.cus.zbp.repository.TestCategoryRepository;
import com.cus.zbp.repository.VersionRepository;
import com.cus.zbp.user.repository.UserRepository;

@Transactional
@ExtendWith(MockitoExtension.class)
class TestCategoryServiceTest {
  @Mock
  private UserRepository userRepository;

  @Mock
  private SoftwareRepository softwareRepository;

  @Mock
  private VersionRepository versionRepository;

  @Mock
  private TestCategoryRepository testCategoryRepository;

  @InjectMocks
  private TestCategoryService testCategoryService;

  @Test
  void successCreateTestCategory() {
    // given
    Software software = Software.builder().id(12).name("soft1").build();
    TestCategory testCategory =
        TestCategory.builder().id(123).name("cat1").abbreviation("CAT").software(software).build();
    given(softwareRepository.findById(anyLong())).willReturn(Optional.of(software));
    given(testCategoryRepository.findByNameAndSoftware_Id(anyString(), anyLong()))
        .willReturn(Optional.empty());
    given(testCategoryRepository.findByAbbreviationAndSoftware_Id(anyString(), anyLong()))
        .willReturn(Optional.empty());
    given(testCategoryRepository.save(any())).willReturn(testCategory);
    // when
    TestCategoryDto dto = testCategoryService.createTestCategory("cat1", "CAT", 12);
    ArgumentCaptor<TestCategory> captor = ArgumentCaptor.forClass(TestCategory.class);
    // then
    verify(testCategoryRepository, times(1)).save(captor.capture());
    assertEquals("cat1", captor.getValue().getName());
    assertEquals("cat1", dto.getName());
  }

  @Test
  void successGetTestCategoryById() {
    // given
    Software software = Software.builder().id(12).name("soft1").build();
    TestCategory testCategory =
        TestCategory.builder().id(123).name("cat1").abbreviation("CAT").software(software).build();
    given(testCategoryRepository.findById(anyLong())).willReturn(Optional.of(testCategory));
    // when
    TestCategoryDto dto = testCategoryService.getTestCategoryById(123);
    // then
    assertEquals("cat1", dto.getName());
    assertEquals(12, dto.getSoftwareId());
  }

  @Test
  void successUpdateTestCategory() {
    // given
    Software software = Software.builder().id(12).name("soft1").build();
    TestCategory testCategory =
        TestCategory.builder().id(123).name("cat1").abbreviation("CAT").software(software).build();
    given(testCategoryRepository.findById(anyLong())).willReturn(Optional.of(testCategory));
    // when
    TestCategoryDto dto = testCategoryService.updateTestCategory(123, "cat2", "CAT2");
    // then
    assertEquals("cat2", dto.getName());
    assertEquals("CAT2", dto.getAbbreviation());
    assertEquals(12, dto.getSoftwareId());
  }

  @Test
  void successDeleteVersion() {
    // given
    Software software = Software.builder().id(12).name("soft1").build();
    TestCategory testCategory =
        TestCategory.builder().id(123).name("cat1").abbreviation("CAT").software(software).build();
    given(testCategoryRepository.findById(anyLong())).willReturn(Optional.of(testCategory));
    // when
    TestCategoryDto dto = testCategoryService.deleteVersion(123);
    // then
    assertEquals("cat1", dto.getName());
    assertEquals("CAT", dto.getAbbreviation());
    assertEquals(12, dto.getSoftwareId());
  }

}
