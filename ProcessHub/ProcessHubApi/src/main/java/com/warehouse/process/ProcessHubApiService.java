package com.warehouse.process;

import com.warehouse.process.infrastructure.dto.ProcessIdDto;
import com.warehouse.process.infrastructure.dto.ShipmentIdDto;

public interface ProcessHubApiService {
    ProcessIdDto initialize(final ShipmentIdDto shipmentId);
}
