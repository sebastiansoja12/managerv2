package com.warehouse.terminal.infrastructure.adapter.secondary;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.terminal.infrastructure.adapter.secondary.entity.MobileEntity;

public interface MobileReadRepository extends JpaRepository<MobileEntity, DeviceId> {
    Optional<MobileEntity> findByIdentityExternalSystemId(final String externalSystemId);
    List<MobileEntity> findByUserId(final UserId userId);
}
