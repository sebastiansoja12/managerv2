package com.warehouse.platformadministration.application.port.secondary;

import com.warehouse.platformadministration.domain.vo.RuntimeStatus;

public interface RuntimeStatusServicePort {

    RuntimeStatus current();
}
