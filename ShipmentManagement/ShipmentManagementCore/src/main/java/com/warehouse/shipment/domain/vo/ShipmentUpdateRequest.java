package com.warehouse.shipment.domain.vo;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.enumeration.UpdateType;
import com.warehouse.shipment.domain.model.ShipmentUpdate;

import java.util.Objects;

public class ShipmentUpdateRequest {
    private final ShipmentId shipmentId;
    private final ShipmentUpdate shipmentUpdate;
    private final UpdateType updateType;
    private final ProcessType processType;

	public ShipmentUpdateRequest(final ShipmentId shipmentId,
                                 final ShipmentUpdate shipmentUpdate,
                                 final UpdateType updateType,
                                 final ProcessType processType) {
        this.shipmentId = shipmentId;
        this.shipmentUpdate = shipmentUpdate;
        this.updateType = updateType;
        this.processType = processType;
    }
    public ShipmentUpdate getShipmentUpdate() {
        return shipmentUpdate;
    }

    public ShipmentId getShipmentId() {
        return shipmentId;
    }

    public UpdateType getUpdateType() {
        return updateType;
    }

    public ProcessType getProcessType() {
        return processType;
    }

    public Recipient getRecipient() {
        return shipmentUpdate.getRecipient();
    }

    public void updateDestination(final City city) {
        if (Objects.nonNull(city) && Objects.nonNull(city.getValue())) {
            this.shipmentUpdate.updateDestination(city.getValue());
        }
    }
}