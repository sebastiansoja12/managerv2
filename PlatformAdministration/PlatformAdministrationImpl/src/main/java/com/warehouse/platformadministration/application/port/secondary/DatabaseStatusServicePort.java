package com.warehouse.platformadministration.application.port.secondary;

import com.warehouse.platformadministration.domain.vo.DatabaseStatus;
import com.warehouse.platformadministration.domain.vo.MigrationStatus;

public interface DatabaseStatusServicePort {

    DatabaseStatus database();

    MigrationStatus migrations();
}
