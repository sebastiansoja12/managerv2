package com.warehouse.returning.domain.model;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReturnPackage {
    private Long parcelId;
    private String reason;
    private ReturnStatus returnStatus;
    private String returnToken;
    private String supplierCode;
    private String depotCode;
    private String username;

    public void processReturn() {
        this.returnStatus = ReturnStatus.PROCESSING;
    }

    public void completeReturn() {
        this.returnStatus = ReturnStatus.COMPLETED;
    }

    public ReturnPackage revertStatus(ReturnStatus returnStatus) {
        return new ReturnPackage(parcelId, reason, returnStatus, returnToken, supplierCode, depotCode, username);
    }
}
