package com.warehouse.routelogger.event;

import com.warehouse.routelogger.RouteLogEvent;
import com.warehouse.routelogger.dto.SupplierCodeRequestDto;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class SupplierCodeLogEvent extends RouteLogBaseEvent implements RouteLogEvent {
    private final SupplierCodeRequestDto supplierCodeRequest;

    @Builder
    public SupplierCodeLogEvent(@NonNull SupplierCodeRequestDto supplierCodeRequest) {
        super();
        this.supplierCodeRequest = supplierCodeRequest;
    }
}
