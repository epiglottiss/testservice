package com.cus.zbp.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.cus.zbp.entity.Version;

@Repository
public interface VersionRepository extends JpaRepository<Version, Long> {
  Optional<Version> findByNameAndSoftwareId(String name, Long softwareId);

  List<Version> findBySoftwareId(Long softwareId);
}
