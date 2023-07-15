package com.melihdumanli.dms.repository;

import com.melihdumanli.dms.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByEmailAndPassword(String email, String password);
    Optional<User> findByEmail(String email);
    List<User> getUsersByDeleteFlagFalse();
}
