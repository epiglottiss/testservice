package com.cus.zbp.user.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.cus.zbp.user.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByEmailAuthKey(String emailAuthKey);

  Optional<User> findByEmailAndName(String email, String name);

  Optional<User> findByResetPasswordKey(String resetPasswordKey);

  Optional<User> findByEmail(String email);
}
