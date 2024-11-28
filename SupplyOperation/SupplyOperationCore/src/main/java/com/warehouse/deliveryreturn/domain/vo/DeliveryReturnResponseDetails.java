package com.warehouse.deliveryreturn.domain.vo;

import com.warehouse.commonassets.enumeration.DeliveryStatus;
import com.warehouse.commonassets.identificator.ShipmentId;
import lombok.Builder;

import java.util.UUID;


@Builder
public class DeliveryReturnResponseDetails {
    private UUID id;
    private ShipmentId shipmentId;
    private DeliveryStatus deliveryStatus;
    private String returnToken;
    private UpdateStatus updateStatus;

    public DeliveryReturnResponseDetails(final UUID id,
                                         final ShipmentId shipmentId,
                                         final DeliveryStatus deliveryStatus,
                                         final String returnToken,
                                         final UpdateStatus updateStatus) {
        this.id = id;
        this.shipmentId = shipmentId;
        this.deliveryStatus = deliveryStatus;
        this.returnToken = returnToken;
        this.updateStatus = updateStatus;
    }

    public UUID getId() {
        return id;
    }

    public ShipmentId getShipmentId() {
        return shipmentId;
    }

    public DeliveryStatus getDeliveryStatus() {
        return deliveryStatus;
    }

    public String getReturnToken() {
        return returnToken;
    }

    public UpdateStatus getUpdateStatus() {
        return updateStatus;
    }
}
