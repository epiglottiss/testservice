package com.cus.zbp.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.cus.zbp.entity.Software;

@Repository
public interface SoftwareRepository extends JpaRepository<Software, Long> {
  Optional<Software> findByName(String name);

  List<Software> findByUser_Email(String email);
}
