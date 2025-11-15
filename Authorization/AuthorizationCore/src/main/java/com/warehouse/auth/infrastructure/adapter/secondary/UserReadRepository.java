package com.warehouse.auth.infrastructure.adapter.secondary;

import com.warehouse.auth.infrastructure.adapter.secondary.entity.UserEntity;
import com.warehouse.commonassets.identificator.UserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserReadRepository extends JpaRepository<UserEntity, UserId> {

    @Query("SELECT u FROM UserEntity u WHERE u.username = :username AND u.deleted = false")
    Optional<UserEntity> findByUsername(@Param("username") final String username);

    Optional<UserEntity> findByApiKey(final String apiKey);
}
