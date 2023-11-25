package com.warehouse.returning.domain.model;


import lombok.Builder;
import lombok.Data;

import static com.warehouse.returning.domain.model.Status.CREATED;

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

    public boolean isCreated() {
        return CREATED.equals(returnStatus);
    }
}
