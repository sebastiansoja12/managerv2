package com.warehouse.deliveryreturn.domain.vo;

import com.warehouse.commonassets.identificator.ShipmentId;
import lombok.Builder;

@Builder
public class ReturnPackageResponse {
    private final ShipmentId shipmentId;
    private final ReturnToken returnToken;

    public ReturnPackageResponse(final ShipmentId shipmentId, final ReturnToken returnToken) {
        this.shipmentId = shipmentId;
        this.returnToken = returnToken;
    }

    public ShipmentId getShipmentId() {
        return shipmentId;
    }

    public ReturnToken getReturnToken() {
        return returnToken;
    }
}
