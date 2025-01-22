package com.warehouse.returntoken.domain.vo;

import com.warehouse.commonassets.identificator.ShipmentId;

import lombok.Builder;

@Builder
public class ReturnPackageResponse {
    private final ShipmentId shipmentId;
    private final ReturnToken returnToken;
    private CrossCourierDelivery crossCourierDelivery;

    public ReturnPackageResponse(final ShipmentId shipmentId, final ReturnToken returnToken,
                                 final CrossCourierDelivery crossCourierDelivery) {
        this.shipmentId = shipmentId;
        this.returnToken = returnToken;
        this.crossCourierDelivery = crossCourierDelivery;
    }

    public ShipmentId getShipmentId() {
        return shipmentId;
    }

    public ReturnToken getReturnToken() {
        return returnToken;
    }

    public CrossCourierDelivery getCrossCourierDelivery() {
        return crossCourierDelivery;
    }

    public void updateCrossCourierDelivery(final CrossCourierDelivery crossCourierDelivery) {
        this.crossCourierDelivery = crossCourierDelivery;
    }
}
