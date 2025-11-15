package com.warehouse.auth.infrastructure.adapter.secondary;

import com.warehouse.auth.infrastructure.adapter.secondary.entity.UserEntity;
import com.warehouse.commonassets.identificator.UserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserReadRepository extends JpaRepository<UserEntity, UserId> {

    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByApiKey(final String apiKey);
}
