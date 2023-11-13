package com.warehouse.returning.domain.model;

import static com.warehouse.returning.domain.model.ReturnStatus.*;

import org.apache.commons.lang3.StringUtils;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReturnPackageRequest {
    private Parcel parcel;
    private String reason;
    private ReturnStatus returnStatus;
    private String returnToken;
    private String supplierCode;
    private String depotCode;
    private String username;

    public void processReturn(String reason) {
        this.reason = reason;
        this.returnStatus = PROCESSING;
    }

    public void completeReturn() {
        this.returnStatus = COMPLETED;
    }

    public void revertStatus(ReturnStatus returnStatus) {
        this.returnStatus = returnStatus;
    }

    public void updateDepot(String depotCode) {
        this.depotCode = depotCode;
    }

    public boolean isCancelled() {
        return CANCELLED.equals(returnStatus);
    }

    public boolean isProcessing() {
        return PROCESSING.equals(returnStatus);
    }

    public boolean isCompleted() {
        return COMPLETED.equals(returnStatus);
    }

    public void updateUser(String username) {
        this.username = username;
    }

    public boolean isReturnTokenAvailable() {
        return StringUtils.isNotEmpty(returnToken);
    }
}
