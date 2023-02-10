package com.cus.zbp.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.cus.zbp.dto.testcategory.CreateTestCategory;
import com.cus.zbp.dto.testcategory.DeleteTestCategory;
import com.cus.zbp.dto.testcategory.TestCategoryDto;
import com.cus.zbp.dto.testcategory.UpdateTestCategory;
import com.cus.zbp.service.SoftwareService;
import com.cus.zbp.service.TestCategoryService;
import com.cus.zbp.service.VersionService;
import com.cus.zbp.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest
@AutoConfigureMockMvc(addFilters = false)
class TestCategoryControllerTest {
  @MockBean
  private UserService userService;
  @MockBean
  private SoftwareService softwareService;

  @MockBean
  private VersionService versionService;

  @MockBean
  private TestCategoryService testCategoryService;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void successCreateVersion() throws Exception {
    // given
    TestCategoryDto dto = TestCategoryDto.builder().id(11).name("cat1").abbreviation("CAT")
        .softwareId(1010).softwareName("soft1").build();
    given(testCategoryService.createTestCategory(anyString(), anyString(), anyLong()))
        .willReturn(dto);
    // when
    // then
    mockMvc
        .perform(post("/testcategory").contentType(MediaType.APPLICATION_JSON).content(
            objectMapper.writeValueAsString(new CreateTestCategory.Request("cat1", "CAT", 1010))))
        .andExpect(status().isOk()).andExpect(jsonPath("$.name").value("cat1"))
        .andExpect(jsonPath("$.softwareName").value("soft1"));
  }

  @Test
  void successGetTestCategoryById() throws Exception {
    // given
    TestCategoryDto dto = TestCategoryDto.builder().id(11).name("cat1").abbreviation("CAT")
        .softwareId(1010).softwareName("soft1").build();
    given(testCategoryService.getTestCategoryById(anyLong())).willReturn(dto);
    // when
    // then
    mockMvc.perform(get("/testcategory/id/11")).andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(11)).andExpect(jsonPath("$.name").value("cat1"))
        .andExpect(jsonPath("$.softwareName").value("soft1"));
  }

  @Test
  void successUpdateTestCategory() throws Exception {
    // given
    TestCategoryDto dto = TestCategoryDto.builder().id(11).name("cat2").abbreviation("CAT")
        .softwareId(1010).softwareName("soft1").build();
    given(testCategoryService.updateTestCategory(anyLong(), anyString(), anyString()))
        .willReturn(dto);
    // when
    // then
    mockMvc
        .perform(put("/testcategory").contentType(MediaType.APPLICATION_JSON).content(
            objectMapper.writeValueAsString(new UpdateTestCategory.Request(1010, "cat2", "CAT"))))
        .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(11))
        .andExpect(jsonPath("$.name").value("cat2"))
        .andExpect(jsonPath("$.abbreviation").value("CAT"))
        .andExpect(jsonPath("$.softwareName").value("soft1"));
  }


  @Test
  void testDeleteVersion() throws Exception {
    // given
    TestCategoryDto dto = TestCategoryDto.builder().id(11).name("cat1").abbreviation("CAT")
        .softwareId(1010).softwareName("soft1").build();
    given(testCategoryService.deleteVersion(anyLong())).willReturn(dto);
    // when
    // then
    mockMvc
        .perform(delete("/testcategory").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(new DeleteTestCategory.Request(11))))
        .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(11))
        .andExpect(jsonPath("$.name").value("cat1"))
        .andExpect(jsonPath("$.abbreviation").value("CAT"))
        .andExpect(jsonPath("$.softwareName").value("soft1"));
  }

}
