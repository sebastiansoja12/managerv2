package com.warehouse.platformadministration.application.port.primary;

import com.warehouse.platformadministration.application.port.primary.command.ChangeMaintenanceModeCommand;
import com.warehouse.platformadministration.domain.model.MaintenanceMode;

public interface MaintenanceModePort {

    MaintenanceMode current();

    MaintenanceMode change(final ChangeMaintenanceModeCommand command);

    boolean isBlocked(final String path, final String httpMethod);
}
