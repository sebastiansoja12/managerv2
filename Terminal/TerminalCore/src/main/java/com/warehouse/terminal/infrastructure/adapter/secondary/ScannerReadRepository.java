package com.warehouse.terminal.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.terminal.infrastructure.adapter.secondary.entity.ScannerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScannerReadRepository extends JpaRepository<ScannerEntity, DeviceId> {
    Optional<ScannerEntity> findByIdentityExternalSystemId(final String externalSystemId);
}
