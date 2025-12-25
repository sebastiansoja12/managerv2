package com.warehouse.process.infrastructure.adapter.primary;

import com.warehouse.process.ProcessHubApiService;
import com.warehouse.process.infrastructure.dto.ProcessIdDto;
import com.warehouse.process.infrastructure.dto.ShipmentIdDto;

public class ProcessResourceAdapter implements ProcessHubApiService {

    @Override
    public ProcessIdDto initialize(final ShipmentIdDto shipmentId) {
        return null;
    }
}
