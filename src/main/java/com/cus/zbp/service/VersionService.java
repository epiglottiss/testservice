package com.cus.zbp.service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.cus.zbp.dto.versionuser.VersionUserDto;
import com.cus.zbp.dto.version.VersionDto;
import com.cus.zbp.entity.Software;
import com.cus.zbp.entity.Version;
import com.cus.zbp.entity.VersionUser;
import com.cus.zbp.exception.SoftwareException;
import com.cus.zbp.exception.UserException;
import com.cus.zbp.exception.VersionAuthException;
import com.cus.zbp.exception.VersionException;
import com.cus.zbp.repository.SoftwareRepository;
import com.cus.zbp.repository.VersionRepository;
import com.cus.zbp.repository.VersionUserRepository;
import com.cus.zbp.type.ErrorCode;
import com.cus.zbp.type.VersionAccessLevel;
import com.cus.zbp.user.entity.User;
import com.cus.zbp.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

@Service
@RequiredArgsConstructor
public class VersionService {
  private final UserRepository userRepository;
  private final SoftwareRepository softwareRepository;
  private final VersionRepository versionRepository;
  private final VersionUserRepository versionUserRepository;

  @Value("${cloud.aws.credentials.s3.bucket}")
  private String S3Bucket;

  @Autowired
  AmazonS3Client amazonS3Client;

  @Transactional
  public VersionDto createVersion(String name, long softwareId, MultipartFile multipartFile, String uri,
                                  VersionAccessLevel accessLevel) throws IOException {
    Software software = softwareRepository.findById(softwareId)
            .orElseThrow(() -> new SoftwareException(ErrorCode.SOFTWARE_NOT_EXIST));
    versionRepository.findByNameAndSoftwareId(name, softwareId).ifPresent(version -> {
      throw new VersionException(ErrorCode.VERSION_NAME_ALREADY_EXIST);
    });

    String location = ""; //file과 uri 둘다 비어있을 경우 빈 문자열로 db에 저장
    if(!multipartFile.isEmpty()) {
      String originalName = multipartFile.getOriginalFilename();
      long size = multipartFile.getSize(); // 파일 크기

      ObjectMetadata objectMetaData = new ObjectMetadata();
      objectMetaData.setContentType(multipartFile.getContentType());
      objectMetaData.setContentLength(size);

      // S3에 업로드, S3 제공하는 version 기능 이용
      amazonS3Client.putObject(
              new PutObjectRequest(S3Bucket, originalName, multipartFile.getInputStream(), objectMetaData)
                      .withCannedAcl(CannedAccessControlList.PublicRead)
      ).setVersionId(name);

      location = amazonS3Client.getUrl(S3Bucket, originalName).toString(); // 접근가능한 URL 가져오기
    }
    else if(!StringUtils.isEmpty(uri)){
      location = uri;
    }

    return VersionDto.from(versionRepository.save(Version.builder().name(name).location(location)
            .accessLevel(accessLevel).software(software).build()));
  }

  @Transactional
  public List<VersionDto> getVersionsBySoftwareName(String softwarename) {
    Software software = softwareRepository.findByName(softwarename)
        .orElseThrow(() -> new SoftwareException(ErrorCode.SOFTWARE_NOT_EXIST));

    return versionRepository.findBySoftwareId(software.getId()).stream().map(VersionDto::from)
        .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public VersionDto getVersionDetail(long userId, long softwareId, long versionId) {
    userRepository.findById(userId).orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

    Software software = softwareRepository.findById(softwareId)
        .orElseThrow(() -> new SoftwareException(ErrorCode.SOFTWARE_NOT_EXIST));
    if (software.getUser().getId() != userId) {
      throw new SoftwareException(ErrorCode.SOFTWARE_USER_UNMATCH);
    }
    Version version = versionRepository.findById(versionId)
        .orElseThrow(() -> new VersionException(ErrorCode.VERSION_NOT_EXIST));
    // TODO : version에 대한 user의 권한 체크


    return VersionDto.from(version);
  }


  @Transactional
  public VersionDto deleteVersion(long userId, long softwareId, long versionId) {
    userRepository.findById(userId).orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

    Software software = softwareRepository.findById(softwareId)
        .orElseThrow(() -> new SoftwareException(ErrorCode.SOFTWARE_NOT_EXIST));
    if (software.getUser().getId() != userId) {
      throw new SoftwareException(ErrorCode.SOFTWARE_USER_UNMATCH);
    }

    Version version = versionRepository.findById(versionId)
        .orElseThrow(() -> new VersionException(ErrorCode.VERSION_NOT_EXIST));
    // TODO : version에 대한 user의 권한 체크


    versionRepository.delete(version);
    return VersionDto.from(version);
  }

  @Transactional
  public VersionUserDto createUserAuthToVersion(long userId, long versionId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

    Version version = versionRepository.findById(versionId)
        .orElseThrow(() -> new VersionException(ErrorCode.VERSION_NOT_EXIST));
    if (version.getAccessLevel() != VersionAccessLevel.ALLOWABLE) {
      throw new VersionAuthException(ErrorCode.VERSION_NOT_ALLOWABLE);
    }

    versionUserRepository.findByVersionIdAndUserId(version.getId(), user.getId())
        .ifPresent(auth -> {
          throw new VersionAuthException(ErrorCode.VERSION_USER_HAS_AUTH);
        });

    return VersionUserDto.from(
        versionUserRepository.save(VersionUser.builder().version(version).user(user).build()));
  }

  @Transactional
  public VersionUserDto deleteUserAuthOfVersion(long userId, long versionId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

    Version version = versionRepository.findById(versionId)
        .orElseThrow(() -> new VersionException(ErrorCode.VERSION_NOT_EXIST));
    if (version.getAccessLevel() != VersionAccessLevel.ALLOWABLE) {
      throw new VersionAuthException(ErrorCode.VERSION_NOT_ALLOWABLE);
    }

    versionUserRepository.deleteByVersionIdAndUserId(version.getId(), user.getId());
    return VersionUserDto.from(VersionUser.builder().version(version).user(user).build());
  }

  @Transactional
  public List<VersionUserDto> getAllowedUserOfVersion(long versionId){
    Version version = versionRepository.findById(versionId)
            .orElseThrow(() -> new VersionException(ErrorCode.VERSION_NOT_EXIST));
    if (version.getAccessLevel() != VersionAccessLevel.ALLOWABLE) {
      throw new VersionAuthException(ErrorCode.VERSION_NOT_ALLOWABLE);
    }
    return versionUserRepository.findByVersion_Id(versionId).stream().map(VersionUserDto::from).collect(Collectors.toList());
  }


}
