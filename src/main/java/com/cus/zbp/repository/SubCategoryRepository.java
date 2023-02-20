package com.cus.zbp.repository;

import com.cus.zbp.entity.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {
    Optional<SubCategory> findByNameAndTestCategoryId(String name, long categoryId);

    Optional<SubCategory> findByNumberAndTestCategoryId(int number, long categoryId);

    Optional<SubCategory> findFirstByTestCategoryIdOrderByNumberDesc(long categoryId);

    List<SubCategory> findByTestCategoryId(long categoryId);
}
