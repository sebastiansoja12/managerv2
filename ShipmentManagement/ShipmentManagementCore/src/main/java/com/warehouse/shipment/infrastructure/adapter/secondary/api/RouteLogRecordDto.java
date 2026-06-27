package com.warehouse.shipment.infrastructure.adapter.secondary.api;

import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.commonassets.identificator.ShipmentId;

public record RouteLogRecordDto(
        ProcessId processId,
        ShipmentId shipmentId,
        RouteLogRecordDetailsDto routeLogRecordDetails,
        ReturnCodeDto returnCode,
        FaultDescriptionDto faultDescription
) {
}
