package com.warehouse.returning.application.port.primary.command;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.returning.domain.enumeration.ReasonCode;

public class CreateReturnPackageCommand {
    private TrackingNumber trackingNumber;
    private String reason;
    private DepartmentId departmentId;
    private UserId userId;
    private ReasonCode reasonCode;

    public CreateReturnPackageCommand() {
    }

    public CreateReturnPackageCommand(final TrackingNumber trackingNumber,
                                      final String reason,
                                      final DepartmentId departmentId,
                                      final UserId userId,
                                      final ReasonCode reasonCode) {
        this.trackingNumber = trackingNumber;
        this.reason = reason;
        this.departmentId = departmentId;
        this.userId = userId;
        this.reasonCode = reasonCode;
    }

    public DepartmentId getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(final DepartmentId departmentId) {
        this.departmentId = departmentId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(final String reason) {
        this.reason = reason;
    }

    public ReasonCode getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(final ReasonCode reasonCode) {
        this.reasonCode = reasonCode;
    }

    public TrackingNumber getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(final TrackingNumber trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public UserId getUserId() {
        return userId;
    }

    public void setUserId(final UserId userId) {
        this.userId = userId;
    }
}
