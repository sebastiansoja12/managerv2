package com.warehouse.platformadministration.application.port.primary;

import com.warehouse.platformadministration.application.port.secondary.DatabaseStatusServicePort;
import com.warehouse.platformadministration.application.port.secondary.MaintenanceModeRepository;
import com.warehouse.platformadministration.application.port.secondary.RuntimeStatusServicePort;
import com.warehouse.platformadministration.domain.vo.SystemStatus;

public class SystemStatusPortImpl implements SystemStatusPort {

    private final RuntimeStatusServicePort runtimeStatusServicePort;

    private final DatabaseStatusServicePort databaseStatusServicePort;

    private final MaintenanceModeRepository maintenanceModeRepository;

    public SystemStatusPortImpl(final RuntimeStatusServicePort runtimeStatusServicePort,
                                final DatabaseStatusServicePort databaseStatusServicePort,
                                final MaintenanceModeRepository maintenanceModeRepository) {
        this.runtimeStatusServicePort = runtimeStatusServicePort;
        this.databaseStatusServicePort = databaseStatusServicePort;
        this.maintenanceModeRepository = maintenanceModeRepository;
    }

    @Override
    public SystemStatus current() {
        return new SystemStatus(runtimeStatusServicePort.current(), databaseStatusServicePort.database(),
                databaseStatusServicePort.migrations(), maintenanceModeRepository.current());
    }
}
