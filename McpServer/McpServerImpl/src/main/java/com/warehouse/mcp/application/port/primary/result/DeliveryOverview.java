package com.warehouse.mcp.application.port.primary.result;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.mcp.domain.vo.DeliveryDetails;

public record DeliveryOverview(DeliveryDetails delivery,
                               DepartmentCode originDepartmentCode,
                               DepartmentCode destinationDepartmentCode,
                               SupplierCode courierCode) {
}
