package com.warehouse.logistics.domain.vo;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.commonassets.identificator.UserId;
import lombok.Builder;

@Builder
public class CompleteDeliveryCommand {
    private ShipmentId shipmentId;
    private SupplierId supplierId;
    private UserId userId;
    private DepartmentId departmentId;


    public DepartmentId getDepartmentId() {
        return departmentId;
    }

    public ShipmentId getShipmentId() {
        return shipmentId;
    }

    public SupplierId getSupplierId() {
        return supplierId;
    }

    public UserId getUserId() {
        return userId;
    }
}
