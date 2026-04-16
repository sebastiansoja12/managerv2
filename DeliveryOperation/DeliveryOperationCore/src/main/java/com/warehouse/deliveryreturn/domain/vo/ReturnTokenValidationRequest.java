package com.warehouse.deliveryreturn.domain.vo;

import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.commonassets.identificator.ShipmentId;

public record ReturnTokenValidationRequest(ProcessId processId, ShipmentId shipmentId, ReturnToken returnToken) {
}
