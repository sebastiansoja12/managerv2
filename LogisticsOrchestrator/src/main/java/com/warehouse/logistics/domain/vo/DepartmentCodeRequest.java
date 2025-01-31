package com.warehouse.logistics.domain.vo;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.ShipmentId;

public class DepartmentCodeRequest {
    private final DepartmentCode departmentCode;
    private final ShipmentId shipmentId;
    private final ProcessType processType;

    public DepartmentCodeRequest(final DepartmentCode departmentCode, final ShipmentId shipmentId, final ProcessType processType) {
        this.departmentCode = departmentCode;
        this.shipmentId = shipmentId;
        this.processType = processType;
    }

    public DepartmentCode getDepartmentCode() {
        return departmentCode;
    }

    public ShipmentId getShipmentId() {
        return shipmentId;
    }

    public ProcessType getProcessType() {
        return processType;
    }
}
