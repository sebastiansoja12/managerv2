package com.warehouse.platformadministration.application.port.primary;

import com.warehouse.platformadministration.domain.vo.SystemStatus;

public interface SystemStatusPort {

    SystemStatus current();
}
