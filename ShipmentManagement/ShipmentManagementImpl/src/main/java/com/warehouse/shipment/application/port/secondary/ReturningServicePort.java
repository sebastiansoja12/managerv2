package com.warehouse.shipment.application.port.secondary;

import java.util.Optional;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.ReturnId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.vo.ShipmentReturnDetails;
import com.warehouse.shipment.domain.vo.ShipmentReturnPage;

public interface ReturningServicePort {
    ShipmentReturnDetails getReturn(final ReturnId returnId);
    Optional<ShipmentReturnDetails> findReturnByShipmentId(final ShipmentId shipmentId);
    ShipmentReturnPage getReturns(final DepartmentCode departmentCode, final int page, final int size);
    void startProcessing(final ShipmentId shipmentId);
    void complete(final ShipmentId shipmentId);
}
