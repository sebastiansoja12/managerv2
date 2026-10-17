package com.warehouse.platformadministration.infrastructure.adapter.secondary;

import com.warehouse.platformadministration.infrastructure.adapter.secondary.entity.PlatformAuditEntryEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlatformAuditReadRepository extends JpaRepository<PlatformAuditEntryEntity, Long> {

    List<PlatformAuditEntryEntity> findAllByOrderByCreatedAtDesc(final Pageable pageable);

    List<PlatformAuditEntryEntity> findAllByOperatorIdOrderByCreatedAtDesc(final Long operatorId, final Pageable pageable);
}
