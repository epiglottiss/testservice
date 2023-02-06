package com.cus.zbp.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.cus.zbp.dto.software.CreateSoftware;
import com.cus.zbp.dto.software.DeleteSoftware;
import com.cus.zbp.dto.software.SoftwareDto;
import com.cus.zbp.service.SoftwareService;
import com.cus.zbp.type.SoftwareType;
import com.cus.zbp.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest
@AutoConfigureMockMvc(addFilters = false)
class SoftwareControllerTest {
  @MockBean
  private UserService userService;
  @MockBean
  private SoftwareService softwareService;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  final void successCreateSoftware() throws Exception {
    // given
    given(softwareService.createSoftware(anyString(), anyString(), any())).willReturn(
        SoftwareDto.builder().name("soft1").userEmail("test@test.com").type(SoftwareType.EXECUTABLE)
            .createdDate(LocalDateTime.now()).updatedDate(LocalDateTime.now()).build());

    // when
    // then
    mockMvc
        .perform(post("/software").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(
                new CreateSoftware.Request("soft1", "test@test.com", SoftwareType.EXECUTABLE))))
        .andExpect(status().isOk()).andExpect(jsonPath("$.name").value("soft1"))
        .andExpect(jsonPath("$.userEmail").value("test@test.com"))
        .andExpect(jsonPath("$.type").value("EXECUTABLE"));

  }

  @Test
  final void successGetSoftwareListByEmail() throws Exception {
    // given
    List<SoftwareDto> list = new ArrayList();
    SoftwareDto dto1 =
        SoftwareDto.builder().name("soft1").userEmail("test@test.com").type(SoftwareType.EXECUTABLE)
            .createdDate(LocalDateTime.now()).updatedDate(LocalDateTime.now()).build();
    SoftwareDto dto2 =
        SoftwareDto.builder().name("soft2").userEmail("test@test.com").type(SoftwareType.ARCHIVE)
            .createdDate(LocalDateTime.now()).updatedDate(LocalDateTime.now()).build();
    list.add(dto1);
    list.add(dto2);
    given(softwareService.getSoftwareListByEmail(anyString())).willReturn(list);
    // when
    // then
    mockMvc.perform(get("/software/email/test@test.com")).andExpect(status().isOk())
        .andExpect(jsonPath("$[0]['name']").value("soft1"))
        .andExpect(jsonPath("$[0]['userEmail']").value("test@test.com"))
        .andExpect(jsonPath("$[0]['type']").value("EXECUTABLE"))
        .andExpect(jsonPath("$[1]['name']").value("soft2"))
        .andExpect(jsonPath("$[1]['userEmail']").value("test@test.com"))
        .andExpect(jsonPath("$[1]['type']").value("ARCHIVE"));

  }

  @Test
  final void successGetSoftwareListByName() throws Exception {
    // given
    SoftwareDto dto1 =
        SoftwareDto.builder().name("soft1").userEmail("test@test.com").type(SoftwareType.EXECUTABLE)
            .createdDate(LocalDateTime.now()).updatedDate(LocalDateTime.now()).build();
    given(softwareService.getSoftwareByName(anyString())).willReturn(dto1);
    // when
    // then
    mockMvc.perform(get("/software/name/soft1")).andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("soft1"))
        .andExpect(jsonPath("$.userEmail").value("test@test.com"))
        .andExpect(jsonPath("$.type").value("EXECUTABLE"));
  }

  // @Test
  // final void successUpdateSoftware() throws Exception {
  // // given
  // given(softwareService.updateSoftware(anyString(), anyString(), any())).willReturn(
  // SoftwareDto.builder().name("soft3").userEmail("test@test.com").type(SoftwareType.WEBSITE)
  // .createdDate(LocalDateTime.now()).updatedDate(LocalDateTime.now()).build());
  //
  // // when
  // // then
  // mockMvc
  // .perform(put("/software").contentType(MediaType.APPLICATION_JSON)
  // .content(objectMapper.writeValueAsString(
  // new UpdateSoftware.Request("soft3", "test@test.com", SoftwareType.WEBSITE))))
  // .andExpect(status().isOk()).andExpect(jsonPath("$.name").value("soft3"))
  // .andExpect(jsonPath("$.userEmail").value("test@test.com"))
  // .andExpect(jsonPath("$.type").value("WEBSITE"));
  // }

  @Test
  final void testDeleteSoftware() throws Exception {
    // given
    given(softwareService.deleteSoftware(anyString(), anyString())).willReturn(
        SoftwareDto.builder().name("soft3").userEmail("test@test.com").type(SoftwareType.WEBSITE)
            .createdDate(LocalDateTime.now()).updatedDate(LocalDateTime.now()).build());

    // when
    // then
    mockMvc
        .perform(delete("/software").contentType(MediaType.APPLICATION_JSON).content(
            objectMapper.writeValueAsString(new DeleteSoftware.Request("soft3", "test@test.com"))))
        .andExpect(status().isOk()).andExpect(jsonPath("$.name").value("soft3"))
        .andExpect(jsonPath("$.userEmail").value("test@test.com"))
        .andExpect(jsonPath("$.type").value("WEBSITE"));
  }

}
