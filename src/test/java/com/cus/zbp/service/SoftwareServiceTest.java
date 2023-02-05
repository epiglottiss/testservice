package com.cus.zbp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.cus.zbp.dto.SoftwareDto;
import com.cus.zbp.entity.Software;
import com.cus.zbp.exception.SoftwareException;
import com.cus.zbp.repository.SoftwareRepository;
import com.cus.zbp.type.ErrorCode;
import com.cus.zbp.type.SoftwareType;
import com.cus.zbp.user.entity.User;
import com.cus.zbp.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class SoftwareServiceTest {
  @Mock
  private UserRepository userRepository;
  @Mock
  private SoftwareRepository softwareRepository;

  @InjectMocks
  private SoftwareService softwareService;

  @Test
  void successCreateSoftware() {
    // given
    User user = User.builder().email("test@test.com").build();
    given(userRepository.findById(anyString())).willReturn(Optional.of(user));
    given(softwareRepository.findByName(anyString())).willReturn(Optional.empty());
    given(softwareRepository.save(any())).willReturn(
        Software.builder().name("soft1").user(user).type(SoftwareType.EXECUTABLE).build());
    // when
    SoftwareDto result =
        softwareService.createSoftware("soft1", "test@test.com", SoftwareType.EXECUTABLE);
    ArgumentCaptor<Software> captor = ArgumentCaptor.forClass(Software.class);

    // then
    verify(softwareRepository, times(1)).save(captor.capture());
    assertEquals("soft1", captor.getValue().getName());
    assertEquals("test@test.com", captor.getValue().getUser().getEmail());
    assertEquals("soft1", result.getName());
    assertEquals("test@test.com", result.getUserEmail());
    assertEquals(SoftwareType.EXECUTABLE, result.getType());
  }

  @Test
  void failCreateSoftware_USER_NOT_FOUND() {
    // given
    given(userRepository.findById(anyString())).willReturn(Optional.empty());
    // when
    SoftwareException e = assertThrows(SoftwareException.class,
        () -> softwareService.createSoftware("soft1", "test@test.com", SoftwareType.EXECUTABLE));

    // then
    assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
  }

  @Test
  void failCreateSoftware_SOFTWARE_NAME_ALREADY_EXIST() {
    // given
    User user = User.builder().email("test@test.com").build();
    Software s1 = Software.builder().name("soft1").user(user).type(SoftwareType.EXECUTABLE).build();
    given(userRepository.findById(anyString())).willReturn(Optional.of(user));
    given(softwareRepository.findByName(anyString())).willReturn(Optional.of(s1));
    // when
    SoftwareException e = assertThrows(SoftwareException.class,
        () -> softwareService.createSoftware("soft1", "test@test.com", SoftwareType.EXECUTABLE));

    // then
    assertEquals(ErrorCode.SOFTWARE_NAME_ALREADY_EXIST, e.getErrorCode());
  }


  @Test
  void successGetSoftwareListByEmail() {
    // given
    User user = User.builder().email("test@test.com").build();

    List<Software> list = new ArrayList();
    Software s1 = Software.builder().name("soft1").user(user).type(SoftwareType.EXECUTABLE).build();
    Software s2 = Software.builder().name("soft2").user(user).type(SoftwareType.ARCHIVE).build();
    list.add(s1);
    list.add(s2);

    given(userRepository.findById(anyString())).willReturn(Optional.of(user));
    given(softwareRepository.findByUser_Email(anyString())).willReturn(list);
    // when
    List<SoftwareDto> result = softwareService.getSoftwareListByEmail("test@test.com");
    // then
    assertEquals("soft1", result.get(0).getName());
    assertEquals("soft2", result.get(1).getName());
    assertEquals(2, result.size());
  }

  @Test
  void failGetSoftwareListByEmail_USER_NOT_FOUND() {
    // given
    given(userRepository.findById(anyString())).willReturn(Optional.empty());
    // when
    SoftwareException e = assertThrows(SoftwareException.class,
        () -> softwareService.getSoftwareListByEmail("test@test.com"));

    // then
    assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
  }

  @Test
  void successGetSoftwareByName() {
    // given
    User user = User.builder().email("test@test.com").build();
    Software s1 = Software.builder().name("soft1").user(user).type(SoftwareType.EXECUTABLE).build();
    given(softwareRepository.findByName(anyString())).willReturn(Optional.of(s1));
    // when
    SoftwareDto result = softwareService.getSoftwareByName("soft1");
    // then
    assertEquals("soft1", result.getName());
  }

  @Test
  void failGetSoftwareByName_SOFTWARE_NOT_EXIST() {
    // given
    given(softwareRepository.findByName(anyString())).willReturn(Optional.empty());
    // when
    SoftwareException e =
        assertThrows(SoftwareException.class, () -> softwareService.getSoftwareByName("soft1"));

    // then
    assertEquals(ErrorCode.SOFTWARE_NOT_EXIST, e.getErrorCode());
  }

  // @Test
  // void successUpdateSoftware() {
  // //given
  // User user = User.builder().email("test@test.com").build();
  // Software s1=
  // Software.builder().name("soft1").user(user).type(SoftwareType.EXECUTABLE)
  // .build();
  // given(softwareRepository.findByName(anyString())).willReturn(Optional.of(s1));
  // //when
  // }

  @Test
  void successDeleteSoftware() {
    // given
    User user = User.builder().email("test@test.com").build();
    Software s1 = Software.builder().name("soft1").user(user).type(SoftwareType.EXECUTABLE).build();
    given(softwareRepository.findByName(anyString())).willReturn(Optional.of(s1));
    // when
    SoftwareDto result = softwareService.deleteSoftware("soft1", "test@test.com");
    // then
    assertEquals("soft1", result.getName());
    assertEquals("test@test.com", result.getUserEmail());
  }

  @Test
  void failDeleteSoftware_SOFTWARE_NOT_EXIST() {
    // given
    given(softwareRepository.findByName(anyString())).willReturn(Optional.empty());
    // when
    SoftwareException e = assertThrows(SoftwareException.class,
        () -> softwareService.deleteSoftware("soft1", "test@test.com"));
    // then
    assertEquals(ErrorCode.SOFTWARE_NOT_EXIST, e.getErrorCode());
  }

  @Test
  void failDeleteSoftware_SOFTWARE_USER_UNMATCH() {
    // given
    User user = User.builder().email("test@test.com").build();
    Software s1 = Software.builder().name("soft1").user(user).type(SoftwareType.EXECUTABLE).build();
    given(softwareRepository.findByName(anyString())).willReturn(Optional.of(s1));
    // when
    SoftwareException e = assertThrows(SoftwareException.class,
        () -> softwareService.deleteSoftware("soft1", "fakeUser@test.com"));
    // then
    assertEquals(ErrorCode.SOFTWARE_USER_UNMATCH, e.getErrorCode());
  }

}
