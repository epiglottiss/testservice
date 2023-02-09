package com.cus.zbp.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;
import com.cus.zbp.dto.version.CreateVersion;
import com.cus.zbp.dto.version.VersionDetail;
import com.cus.zbp.dto.version.VersionDto;
import com.cus.zbp.service.SoftwareService;
import com.cus.zbp.service.VersionService;
import com.cus.zbp.type.VersionAccessLevel;
import com.cus.zbp.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@WebMvcTest
@AutoConfigureMockMvc(addFilters = false)
class VersionControllerTest {
  @MockBean
  private UserService userService;
  @MockBean
  private SoftwareService softwareService;

  @MockBean
  private VersionService versionService;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void successCreateVersion() throws Exception {
    // given
    VersionDto dto = VersionDto.builder().id(100).accessLevel(VersionAccessLevel.ALL_OPEN)
        .softwareName("soft1").name("1.2.3.4").build();
    byte[] bytes = new byte[10];

    InputStream stream = new ClassPathResource("classpath:logs/log_file.log").getInputStream();

    MultipartFile multipartFile = new MockMultipartFile("uploadFile", stream);
    given(versionService.createVersion(anyString(), anyInt(), anyString(), any())).willReturn(dto);

    // when
    // then
    // TODO:multipartFile 때문에 방법 테스트 방법 찾아야 함.
    mockMvc
        .perform(post("/version").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .writeValueAsString(new CreateVersion.Request("1.2.3.4", 1,
                    VersionAccessLevel.ALL_OPEN, multipartFile))))
        .andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.name").value("1.2.3.4"));
  }

  // TODO : S3 저장 테스트 방법?

  @Test
  void successGetVersionsBySoftwareName() throws Exception {
    // given
    List<VersionDto> dtoList = new ArrayList();
    VersionDto dto1 = VersionDto.builder().softwareName("soft1").name("1.2.3.4").build();
    VersionDto dto2 = VersionDto.builder().softwareName("soft1").name("2.2.3.4").build();
    dtoList.add(dto1);
    dtoList.add(dto2);
    given(versionService.getVersionsBySoftwareName(anyString())).willReturn(dtoList);
    // when
    // then
    mockMvc.perform(get("/version/softwarename/soft1")).andExpect(status().isOk())
        .andExpect(jsonPath("$[0]['name']").value("1.2.3.4"))
        .andExpect(jsonPath("$[1]['name']").value("2.2.3.4"));
  }

  @Test
  void successGetVersionDetail() throws Exception {
    // given
    VersionDto dto = VersionDto.builder().softwareName("soft1").name("1.2.3.4").build();
    given(versionService.getVersionDetail(anyLong(), anyLong(), anyLong())).willReturn(dto);
    // when
    // then
    mockMvc
        .perform(get("/version/detail").contentType(MediaType.APPLICATION_JSON).content(
            objectMapper.writeValueAsString(new VersionDetail.Request(1, 2, "1.2.3.4", 10))))
        .andExpect(status().isOk()).andExpect(jsonPath("$.name").value("1.2.3.4"));
  }

  @Test
  void successDeleteVersion() throws Exception {
    // given
    VersionDto dto = VersionDto.builder().softwareName("soft1").name("1.2.3.4").build();
    given(versionService.deleteVersion(anyLong(), anyLong(), anyLong())).willReturn(dto);
    // when
    // then
    mockMvc
        .perform(delete("/version").contentType(MediaType.APPLICATION_JSON).content(
            objectMapper.writeValueAsString(new VersionDetail.Request(1, 2, "1.2.3.4", 10))))
        .andExpect(status().isOk()).andExpect(jsonPath("$.name").value("1.2.3.4"));
  }

}
