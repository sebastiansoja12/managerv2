package com.warehouse.redirect.infrastructure.adapter.secondary;

import com.warehouse.redirect.infrastructure.adapter.secondary.entity.RedirectTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface RedirectTokenReadRepository extends JpaRepository<RedirectTokenEntity, Long> {

    Optional<RedirectTokenEntity> findByToken(String token);

    @Modifying
    @Query("delete from redirect.RedirectTokenEntity t where t.expiryDate <= ?1")
    @Transactional
    void deleteAllExpiredSince(Instant now);
}
