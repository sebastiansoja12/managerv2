package com.warehouse.routelogger.domain.model;


import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.routelogger.domain.enumeration.ProcessType;

import lombok.Data;

@Data
public class AnyDeliveryRequest {
    private DepartmentCode departmentCode;
    private SupplierCode supplierCode;
    private ShipmentId shipmentId;
    private ProcessType processType;
}
