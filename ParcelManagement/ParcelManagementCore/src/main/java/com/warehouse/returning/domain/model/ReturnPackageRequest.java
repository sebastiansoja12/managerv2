package com.warehouse.returning.domain.model;

import lombok.Builder;
import lombok.Data;

import static com.warehouse.returning.domain.model.ReturnStatus.CANCELLED;

@Data
@Builder
public class ReturnPackageRequest {
    private Parcel parcel;
    private String reason;
    private ReturnStatus returnStatus;
    private String returnToken;
    private String supplierCode;
    private String depotCode;

    public void processReturn(String reason) {
        this.reason = reason;
        this.returnStatus = ReturnStatus.PROCESSING;
    }

    public void completeReturn() {
        this.returnStatus = ReturnStatus.COMPLETED;
    }

    public ReturnPackageRequest revertStatus(ReturnStatus returnStatus) {
        return ReturnPackageRequest.builder()
                .parcel(parcel)
                .reason(reason)
                .returnStatus(returnStatus)
                .returnToken(returnToken)
                .build();
    }

    public boolean isCancelled() {
        return CANCELLED.equals(returnStatus);
    }
}
