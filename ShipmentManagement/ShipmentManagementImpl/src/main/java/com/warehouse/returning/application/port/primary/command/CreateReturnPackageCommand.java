package com.warehouse.returning.application.port.primary.command;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.returning.domain.enumeration.ReasonCode;

public class CreateReturnPackageCommand {
    private ShipmentId shipmentId;
    private String reason;
    private DepartmentId departmentId;
    private UserId userId;
    private ReasonCode reasonCode;

    public CreateReturnPackageCommand() {
    }

    public CreateReturnPackageCommand(final ShipmentId shipmentId,
                                      final String reason,
                                      final DepartmentId departmentId,
                                      final UserId userId,
                                      final ReasonCode reasonCode) {
        this.shipmentId = shipmentId;
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

    public ShipmentId getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(final ShipmentId shipmentId) {
        this.shipmentId = shipmentId;
    }

    public UserId getUserId() {
        return userId;
    }

    public void setUserId(final UserId userId) {
        this.userId = userId;
    }
}
