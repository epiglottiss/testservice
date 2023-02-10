package com.cus.zbp.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.cus.zbp.entity.TestCategory;

@Repository
public interface TestCategoryRepository extends JpaRepository<TestCategory, Long> {
  Optional<TestCategory> findByNameAndSoftware_Id(String name, long softwareId);

  Optional<TestCategory> findByAbbreviationAndSoftware_Id(String abbreviation, long softwareId);

}
