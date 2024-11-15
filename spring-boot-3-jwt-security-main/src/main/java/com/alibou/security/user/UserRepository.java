package com.alibou.security.user;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Integer> {

  Optional<User> findByEmail(String email);

  void deleteByEmail(String email);

  @Query(value = "SELECT * FROM user", nativeQuery = true)
  List<Object[]> findAllUsersNative();

}
