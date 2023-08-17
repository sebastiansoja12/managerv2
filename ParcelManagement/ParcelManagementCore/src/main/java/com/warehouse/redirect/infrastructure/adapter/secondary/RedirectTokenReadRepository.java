package com.warehouse.redirect.infrastructure.adapter.secondary;

import com.warehouse.redirect.infrastructure.adapter.secondary.entity.RedirectTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public interface RedirectTokenReadRepository extends JpaRepository<RedirectTokenEntity, Long> {
    void deleteAllExpiredSince(Instant now);
}
