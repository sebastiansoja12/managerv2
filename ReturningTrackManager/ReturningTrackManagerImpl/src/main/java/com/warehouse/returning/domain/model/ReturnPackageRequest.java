package com.warehouse.returning.domain.model;

import com.warehouse.returning.domain.vo.DepartmentId;
import com.warehouse.returning.domain.enumeration.ReasonCode;
import com.warehouse.returning.domain.vo.ShipmentId;
import com.warehouse.returning.domain.vo.UserId;

public class ReturnPackageRequest {
    private ShipmentId shipmentId;
    private String reason;
    private DepartmentId departmentId;
    private UserId userId;
    private ReasonCode reasonCode;

	public ReturnPackageRequest(final DepartmentId departmentId,
                                final String reason,
                                final ShipmentId shipmentId,
                                final UserId userId,
                                final ReasonCode reasonCode) {
        this.departmentId = departmentId;
        this.reason = reason;
        this.shipmentId = shipmentId;
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

    public ReasonCode getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(final ReasonCode reasonCode) {
        this.reasonCode = reasonCode;
    }
}
