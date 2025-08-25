package com.warehouse.shipment.infrastructure.adapter.secondary.api;


import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.commonassets.identificator.ShipmentId;
import lombok.Builder;

@Builder
public record RouteLogRecord(
        ProcessId processId,
        ShipmentId shipmentId,
        RouteLogRecordDetails routeLogRecordDetails,
        ReturnCode returnCode,
        FaultDescription faultDescription
) {}
