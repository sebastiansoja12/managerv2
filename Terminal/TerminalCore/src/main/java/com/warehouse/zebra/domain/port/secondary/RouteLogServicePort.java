package com.warehouse.zebra.domain.port.secondary;


import com.warehouse.commonassets.vo.RouteProcess;

public interface RouteLogServicePort {
    RouteProcess initializeProcess(final Long parcelId);
}
