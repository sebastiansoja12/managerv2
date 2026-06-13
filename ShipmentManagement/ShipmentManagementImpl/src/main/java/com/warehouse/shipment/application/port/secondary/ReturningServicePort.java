package com.warehouse.shipment.application.port.secondary;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.ReturnId;
import com.warehouse.shipment.domain.vo.ShipmentReturnDetails;
import com.warehouse.shipment.domain.vo.ShipmentReturnPage;

public interface ReturningServicePort {
    ShipmentReturnDetails getReturn(final ReturnId returnId);
    ShipmentReturnPage getReturns(final DepartmentCode departmentCode, final int page, final int size);
}
