package com.warehouse.shipment.domain.vo;

import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.commonassets.identificator.ShipmentId;

public record RouteLogRecord(
        ProcessId processId,
        ShipmentId shipmentId,
        RouteLogRecordDetails routeLogRecordDetails,
        ReturnCode returnCode,
        FaultDescription faultDescription
) {
}
