package com.warehouse.platformadministration.infrastructure.adapter.secondary;

import com.warehouse.platformadministration.infrastructure.adapter.secondary.entity.PlatformMaintenanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlatformMaintenanceReadRepository extends JpaRepository<PlatformMaintenanceEntity, Long> {
}
