package com.warehouse.auth.infrastructure.adapter.secondary;

import com.warehouse.auth.infrastructure.adapter.secondary.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthenticationReadRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> getByUsername(String username);

    Optional<UserEntity> findByUsername(String username);
}
