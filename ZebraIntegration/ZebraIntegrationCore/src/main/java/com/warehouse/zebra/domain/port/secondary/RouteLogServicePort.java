package com.warehouse.zebra.domain.port.secondary;

import com.warehouse.zebra.domain.vo.RouteProcess;

public interface RouteLogServicePort {
    RouteProcess initializeProcess(Long parcelId);
}
