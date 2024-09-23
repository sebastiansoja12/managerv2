package com.warehouse.commonassets.request;

public class ParcelCreatedRequest {
    private final Long parcelId;

    private final Long parcelRelatedId;

    public ParcelCreatedRequest(Long parcelId, Long parcelRelatedId) {
        this.parcelId = parcelId;
        this.parcelRelatedId = parcelRelatedId;
    }

    public Long getParcelId() {
        return parcelId;
    }

    public Long getParcelRelatedId() {
        return parcelRelatedId;
    }
}
