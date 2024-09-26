package com.warehouse.deliverymissed.domain.vo;

import com.warehouse.deliverymissed.domain.enumeration.DeliveryStatus;


public class DeliveryMissedRequest {
    private final Long parcelId;
    private final String depotCode;
    private final String supplierCode;
    private final DeliveryStatus deliveryStatus = DeliveryStatus.UNAVAILABLE;

    public DeliveryMissedRequest(Long parcelId, String depotCode, String supplierCode) {
        this.parcelId = parcelId;
        this.depotCode = depotCode;
        this.supplierCode = supplierCode;
    }

    public DeliveryStatus getDeliveryStatus() {
        return deliveryStatus;
    }

    public Long getParcelId() {
        return parcelId;
    }

    public String getDepotCode() {
        return depotCode;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

}
