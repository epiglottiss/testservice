package com.cus.zbp.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.cus.zbp.dto.software.SoftwareDto;
import com.cus.zbp.entity.Software;
import com.cus.zbp.exception.SoftwareException;
import com.cus.zbp.repository.SoftwareRepository;
import com.cus.zbp.type.ErrorCode;
import com.cus.zbp.type.SoftwareType;
import com.cus.zbp.user.entity.User;
import com.cus.zbp.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SoftwareService {
  private final UserRepository userRepository;
  private final SoftwareRepository softwareRepository;

  @Transactional
  public SoftwareDto createSoftware(String softwareName, String userEmail, SoftwareType type) {
    User user = userRepository.findById(userEmail)
        .orElseThrow(() -> new SoftwareException(ErrorCode.USER_NOT_FOUND));

    softwareRepository.findByName(softwareName).ifPresent(software -> {
      throw new SoftwareException(ErrorCode.SOFTWARE_NAME_ALREADY_EXIST);
    });

    return SoftwareDto.from(softwareRepository
        .save(Software.builder().name(softwareName).user(user).type(type).build()));
  }

  @Transactional(readOnly = true)
  public List<SoftwareDto> getSoftwareListByEmail(String userEmail) {
    User user = userRepository.findById(userEmail)
        .orElseThrow(() -> new SoftwareException(ErrorCode.USER_NOT_FOUND));

    return softwareRepository.findByUser_Email(userEmail).stream().map(SoftwareDto::from)
        .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public SoftwareDto getSoftwareByName(String name) {
    Software software = softwareRepository.findByName(name)
        .orElseThrow(() -> new SoftwareException(ErrorCode.SOFTWARE_NOT_EXIST));
    return SoftwareDto.builder().name(software.getName()).userEmail(software.getUser().getEmail())
        .createdDate(software.getCreatedDate()).type(software.getType())
        .updatedDate(software.getUpdatedDate()).build();
  }

  // @Transactional
  // public SoftwareDto updateSoftware(String name, String userEmail, SoftwareType type) {
  // Software software = softwareRepository.findByName(name)
  // .orElseThrow(() -> new SoftwareException(ErrorCode.SOFTWARE_NOT_EXIST));
  //
  // if (userEmail != software.getUser().getEmail()) {
  // throw new SoftwareException(ErrorCode.SOFTWARE_USER_UNMATCH);
  // }
  //
  // software.updateInfo(name, type);
  // return SoftwareDto.from(softwareRepository.save(software));
  // }

  @Transactional
  public SoftwareDto deleteSoftware(String name, String userEmail) {
    Software software = softwareRepository.findByName(name)
        .orElseThrow(() -> new SoftwareException(ErrorCode.SOFTWARE_NOT_EXIST));

    if (userEmail != software.getUser().getEmail()) {
      throw new SoftwareException(ErrorCode.SOFTWARE_USER_UNMATCH);
    }
    softwareRepository.delete(software);
    return SoftwareDto.from(software);
  }
}
