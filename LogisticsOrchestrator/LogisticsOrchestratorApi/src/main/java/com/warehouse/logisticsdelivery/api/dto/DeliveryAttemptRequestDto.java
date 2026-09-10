package com.warehouse.logisticsdelivery.api.dto;

import com.warehouse.commonassets.enumeration.DeliveryStatus;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.identificator.VehicleId;

public record DeliveryAttemptRequestDto(DeliveryTargetDto target,
                                        DeliveryType deliveryType,
                                        DeliveryStatus deliveryStatus,
                                        UserId userId,
                                        SupplierId supplierId,
                                        DepartmentId departmentId,
                                        VehicleId vehicleId,
                                        String comment,
                                        String token) {

    public enum DeliveryType {
        OUTBOUND,
        RETURN
    }
}
