package com.warehouse.returning.domain.model;

import com.warehouse.returning.domain.vo.ReturnPackageId;

public class ChangeReturnStatusRequest {
    private ReturnPackageId returnPackageId;
    private ReturnStatus returnStatus;

    public ChangeReturnStatusRequest(final ReturnPackageId returnPackageId,
                                     final ReturnStatus returnStatus) {
        this.returnPackageId = returnPackageId;
        this.returnStatus = returnStatus;
    }

    public ReturnPackageId getReturnPackageId() {
        return returnPackageId;
    }

    public ReturnStatus getReturnStatus() {
        return returnStatus;
    }

    public void setReturnPackageId(final ReturnPackageId returnPackageId) {
        this.returnPackageId = returnPackageId;
    }

    public void setReturnStatus(final ReturnStatus returnStatus) {
        this.returnStatus = returnStatus;
    }
}
