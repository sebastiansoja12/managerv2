package com.warehouse.deliveryreturn.domain.vo;

import lombok.Builder;

import java.util.UUID;


@Builder
public class DeliveryReturn {
    private final UUID processId;
    private final Long shipmentId;
    private final String departmentCode;
    private final String supplierCode;
    private final String deliveryStatus;
    private final String token;

    public DeliveryReturn(final UUID processId,
                          final Long shipmentId,
                          final String departmentCode,
                          final String supplierCode,
                          final String deliveryStatus,
                          final String token) {
        this.processId = processId;
        this.shipmentId = shipmentId;
        this.departmentCode = departmentCode;
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

    public String getDepartmentCode() {
        return departmentCode;
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
