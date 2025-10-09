package com.warehouse.returning.domain.vo;

import com.warehouse.returning.domain.model.ReturnPackage;
import com.warehouse.returning.domain.model.ReturnStatus;

public record ProcessReturn(ShipmentId shipmentId, ReturnId returnId, ReturnStatus processStatus) {
    public static ProcessReturn from(final ReturnPackage returnPackage) {
        return new ProcessReturn(returnPackage.getShipmentId(), new ReturnId(returnPackage.getReturnPackageId().value()),
                returnPackage.getReturnStatus());
    }
}
