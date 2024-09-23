package com.warehouse.zebrareturn.domain.port.secondary;


import com.warehouse.commonassets.vo.RouteProcess;

public interface RouteLogServicePort {
    RouteProcess initializeProcess(Long parcelId);
}
