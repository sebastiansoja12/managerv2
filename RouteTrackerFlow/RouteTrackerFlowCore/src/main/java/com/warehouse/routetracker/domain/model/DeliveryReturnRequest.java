package com.warehouse.routetracker.domain.model;

import java.util.UUID;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.routetracker.domain.enumeration.ProcessType;


public class DeliveryReturnRequest {
    private UUID id;
    private ShipmentId shipmentId;
    private String deliveryStatus;
    private String returnToken;
    private String updateStatus;
    private String depotCode;
    private String username;
    private ProcessType processType;

	public DeliveryReturnRequest(final UUID id, final ShipmentId shipmentId, final String deliveryStatus,
			final String returnToken, final String updateStatus, final String depotCode, final String username,
			final ProcessType processType) {
        this.id = id;
        this.shipmentId = shipmentId;
        this.deliveryStatus = deliveryStatus;
        this.returnToken = returnToken;
        this.updateStatus = updateStatus;
        this.depotCode = depotCode;
        this.username = username;
        this.processType = processType;
    }

    public UUID getId() {
        return id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }

    public ShipmentId getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(final ShipmentId shipmentId) {
        this.shipmentId = shipmentId;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(final String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getReturnToken() {
        return returnToken;
    }

    public void setReturnToken(final String returnToken) {
        this.returnToken = returnToken;
    }

    public String getUpdateStatus() {
        return updateStatus;
    }

    public void setUpdateStatus(final String updateStatus) {
        this.updateStatus = updateStatus;
    }

    public String getDepotCode() {
        return depotCode;
    }

    public void setDepotCode(final String depotCode) {
        this.depotCode = depotCode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public ProcessType getProcessType() {
        return processType;
    }

    public void setProcessType(final ProcessType processType) {
        this.processType = processType;
    }
}
