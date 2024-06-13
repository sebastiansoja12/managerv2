package com.warehouse.deliveryreturn.domain.vo;

public class UpdateStatusParcelRequest {

    private final Long parcelId;
    private final ParcelStatus parcelStatus = ParcelStatus.RETURN;

    public UpdateStatusParcelRequest(Long parcelId) {
        this.parcelId = parcelId;
    }

    public Long getParcelId() {
        return parcelId;
    }

    public ParcelStatus getParcelStatus() {
        return parcelStatus;
    }
}
