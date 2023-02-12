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

import com.cus.zbp.dto.versionuser.VersionUserDto;
import com.cus.zbp.entity.VersionUser;
import com.cus.zbp.repository.VersionUserRepository;
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

  @Mock
  private VersionUserRepository versionUserRepository;

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
  void successGetVersionsBySoftwareName() {
    // given
    Software software = Software.builder().id(12).name("soft1").build();
    List<Version> dtoList = new ArrayList<>();
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
    VersionDto result = versionService.deleteVersion(99, 12, 1023);
    // then
    assertEquals("1.2.3.4", result.getName());
  }

  @Test
  void successCreateUserAuthToVersion() {
    // given
    User user = User.builder().id(99).email("test@test.com").build();
    Software software = Software.builder().id(12).user(user).name("soft1").build();
    Version version = Version.builder().id(1023).name("1.2.3.4").software(software).accessLevel(VersionAccessLevel.ALLOWABLE).build();
    given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
    given(versionRepository.findById(anyLong())).willReturn(Optional.of(version));
    given(versionUserRepository.findByVersionIdAndUserId(anyLong(), anyLong())).willReturn(Optional.empty());
    given(versionUserRepository.save(any())).willReturn(
            VersionUser.builder().version(version).user(user).build()
    );
    // when
    VersionUserDto result = versionService.createUserAuthToVersion(99, 1023);
    // then
    assertEquals(99, result.getUserId());
    assertEquals(1023, result.getVersionId());
  }

  @Test
  void successDeleteUserAuthOfVersion() {
    // given
    User user = User.builder().id(99).email("test@test.com").build();
    Software software = Software.builder().id(12).user(user).name("soft1").build();
    Version version = Version.builder().id(1023).name("1.2.3.4").software(software).accessLevel(VersionAccessLevel.ALLOWABLE).build();
    given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
    given(versionRepository.findById(anyLong())).willReturn(Optional.of(version));
    given(versionUserRepository.deleteByVersionIdAndUserId(anyLong(),anyLong())).willReturn(
            VersionUser.builder().version(version).user(user).build()
    );
    // when
    VersionUserDto result = versionService.deleteUserAuthOfVersion(99, 1023);
    // then
    assertEquals(99, result.getUserId());
    assertEquals(1023, result.getVersionId());
  }

  @Test
  void successGetAllowedUserOfVersion() {
    // given
    User user1 = User.builder().id(99).email("test@test.com").build();
    User user2 = User.builder().id(100).email("test2@test.com").build();

    Software software = Software.builder().id(12).user(user1).name("soft1").build();
    Version version = Version.builder().id(1023).name("1.2.3.4").software(software).accessLevel(VersionAccessLevel.ALLOWABLE).build();

    VersionUser authInfo1 = VersionUser.builder().user(user1).version(version).build();
    VersionUser authInfo2 = VersionUser.builder().user(user2).version(version).build();

    List<VersionUser> list = new ArrayList<>();
    list.add(authInfo1);
    list.add(authInfo2);

    given(versionRepository.findById(anyLong())).willReturn(Optional.of(version));
    given(versionUserRepository.findByVersion_Id(anyLong())).willReturn(list);
    // when
    List<VersionUserDto> result = versionService.getAllowedUserOfVersion(1023);
    // then
    assertEquals(99, result.get(0).getUserId());
    assertEquals(100, result.get(1).getUserId());
  }
}
