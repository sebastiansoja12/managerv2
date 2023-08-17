package com.warehouse.auth.infrastructure.adapter.secondary;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.warehouse.auth.infrastructure.adapter.secondary.entity.UserEntity;

@Repository
public interface AuthenticationReadRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsername(String username);
}
