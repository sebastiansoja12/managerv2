package com.warehouse.returning.application.port.primary.command;

import com.warehouse.commonassets.enumeration.ReturnStatus;
import com.warehouse.returning.domain.vo.ReturnPackageId;
import com.warehouse.commonassets.identificator.UserId;

public class ChangeReturnPackageStatusCommand {

    private final ReturnPackageId returnPackageId;
    private final ReturnStatus returnStatus;
    private final UserId processedBy;

    public ChangeReturnPackageStatusCommand(final ReturnPackageId returnPackageId,
                                            final ReturnStatus returnStatus,
                                            final UserId processedBy) {
        this.returnPackageId = returnPackageId;
        this.returnStatus = returnStatus;
        this.processedBy = processedBy;
    }

    public ReturnPackageId getReturnPackageId() {
        return returnPackageId;
    }

    public ReturnStatus getReturnStatus() {
        return returnStatus;
    }

    public UserId getProcessedBy() {
        return processedBy;
    }
}
