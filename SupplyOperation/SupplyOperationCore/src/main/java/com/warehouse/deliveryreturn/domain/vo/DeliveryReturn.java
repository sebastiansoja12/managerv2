package com.warehouse.deliveryreturn.domain.vo;

import java.util.UUID;


public class DeliveryReturn {
    private final UUID processId;
    private final Long shipmentId;
    private final String depotCode;
    private final String supplierCode;
    private final String deliveryStatus;
    private final String token;

    public DeliveryReturn(final UUID processId,
                          final Long shipmentId,
                          final String depotCode,
                          final String supplierCode,
                          final String deliveryStatus,
                          final String token) {
        this.processId = processId;
        this.shipmentId = shipmentId;
        this.depotCode = depotCode;
        this.supplierCode = supplierCode;
        this.deliveryStatus = deliveryStatus;
        this.token = token;
    }

    public UUID getProcessId() {
        return processId;
    }

    public Long getShipmentId() {
        return shipmentId;
    }

    public String getDepotCode() {
        return depotCode;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public String getToken() {
        return token;
    }
}
