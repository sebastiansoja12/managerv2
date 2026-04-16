package com.warehouse.routetracker.infrastructure.adapter.secondary;

import com.warehouse.routetracker.infrastructure.adapter.secondary.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserReadRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
}
