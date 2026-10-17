package com.warehouse.platformadministration.application.port.secondary;

import com.warehouse.platformadministration.domain.model.MaintenanceMode;

public interface MaintenanceModeRepository {

    MaintenanceMode current();

    MaintenanceMode save(final MaintenanceMode maintenanceMode);
}
