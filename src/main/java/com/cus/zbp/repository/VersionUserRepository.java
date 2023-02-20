package com.cus.zbp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.cus.zbp.entity.VersionUser;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VersionUserRepository extends JpaRepository<VersionUser, Long> {
    List<VersionUser> findByVersion_Id(long versionId);
    void deleteByVersionIdAndUserId(long versionId, long userId);

    Optional<VersionUser> findByVersionIdAndUserId(long versionId, long userId);
}
