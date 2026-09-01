package com.warehouse.platformadministration.domain.vo;

import com.warehouse.platformadministration.domain.model.MaintenanceMode;

public record SystemStatus(RuntimeStatus runtime,
                           DatabaseStatus database,
                           MigrationStatus migrations,
                           MaintenanceMode maintenance) {
}
