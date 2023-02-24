package com.gdsc.bakku.auth.domain.repo;

import com.gdsc.bakku.auth.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}