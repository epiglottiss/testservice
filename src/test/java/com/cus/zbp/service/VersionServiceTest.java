package com.cus.zbp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
import org.springframework.transaction.annotation.Transactional;
import com.cus.zbp.dto.version.VersionDto;
import com.cus.zbp.entity.Software;
import com.cus.zbp.entity.Version;
import com.cus.zbp.repository.SoftwareRepository;
import com.cus.zbp.repository.VersionRepository;
import com.cus.zbp.type.VersionAccessLevel;
import com.cus.zbp.user.entity.User;
import com.cus.zbp.user.repository.UserRepository;

@Transactional
@ExtendWith(MockitoExtension.class)
class VersionServiceTest {
  @Mock
  private UserRepository userRepository;

  @Mock
  private SoftwareRepository softwareRepository;

  @Mock
  private VersionRepository versionRepository;

  @InjectMocks
  private VersionService versionService;

  @Test
  void successCreateVersion() {
    // given
    Software software = Software.builder().id(12).name("soft1").build();
    Version version = Version.builder().id(1023).name("1.2.3.4").software(software).build();
    given(softwareRepository.findById(anyLong())).willReturn(Optional.of(software));
    given(versionRepository.save(any())).willReturn(version);
    // when
    VersionDto dto =
        versionService.createVersion("1.2.3.4", 12, "Storage", VersionAccessLevel.ALLOWABLE);
    ArgumentCaptor<Version> captor = ArgumentCaptor.forClass(Version.class);
    // then
    verify(versionRepository, times(1)).save(captor.capture());
    assertEquals("1.2.3.4", captor.getValue().getName());
    assertEquals("1.2.3.4", dto.getName());
  }

  @Test
  void failCreateVersion_SOFTWARE_NOT_EXIST() {
    // 예외처리 테스트 해야하는데
  }

  @Test
  void successGetVersionsBySoftwareName() {
    // given
    Software software = Software.builder().id(12).name("soft1").build();
    List<Version> dtoList = new ArrayList();
    Version dto1 = Version.builder().software(software).id(100).name("1.2.3.4").build();
    Version dto2 = Version.builder().software(software).id(101).name("2.2.3.4").build();
    dtoList.add(dto1);
    dtoList.add(dto2);
    given(softwareRepository.findByName(anyString())).willReturn(Optional.of(software));
    given(versionRepository.findBySoftwareId(anyLong())).willReturn(dtoList);
    // when
    List<VersionDto> result = versionService.getVersionsBySoftwareName("soft1");
    // then
    assertEquals("1.2.3.4", result.get(0).getName());
    assertEquals("2.2.3.4", result.get(1).getName());
    assertEquals(2, result.size());
  }

  @Test
  void successGetVersionDetail() {
    // given
    User user = User.builder().id(99).email("test@test.com").build();
    Software software = Software.builder().id(12).user(user).name("soft1").build();
    Version version = Version.builder().id(1023).name("1.2.3.4").software(software).build();
    given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
    given(softwareRepository.findById(anyLong())).willReturn(Optional.of(software));
    given(versionRepository.findById(anyLong())).willReturn(Optional.of(version));
    // when
    VersionDto result = versionService.getVersionDetail(99, 12, 1023);
    // then
    assertEquals("1.2.3.4", result.getName());
  }

  @Test
  void successDeleteVersion() {
    // given
    User user = User.builder().id(99).email("test@test.com").build();
    Software software = Software.builder().id(12).user(user).name("soft1").build();
    Version version = Version.builder().id(1023).name("1.2.3.4").software(software).build();
    given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
    given(softwareRepository.findById(anyLong())).willReturn(Optional.of(software));
    given(versionRepository.findById(anyLong())).willReturn(Optional.of(version));
    // when
    VersionDto result = versionService.getVersionDetail(99, 12, 1023);
    // then
    assertEquals("1.2.3.4", result.getName());
  }

}
