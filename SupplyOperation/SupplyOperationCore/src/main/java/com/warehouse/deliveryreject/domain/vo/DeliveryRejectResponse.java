package com.warehouse.deliveryreject.domain.vo;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.deliveryreject.domain.model.RejectReason;

public record DeliveryRejectResponse(SupplierCode supplierCode, ShipmentId shipmentId, ShipmentId newShipmentId,
                                     RejectReason rejectReason, DeviceInformation deviceInformation) {
}
