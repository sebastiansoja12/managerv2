package com.warehouse.returning.domain.model;

import com.warehouse.returning.domain.enumeration.ReasonCode;
import com.warehouse.returning.domain.vo.DepartmentCode;
import com.warehouse.returning.domain.vo.ShipmentId;
import com.warehouse.returning.domain.vo.UserId;
import com.warehouse.returning.infrastructure.adapter.primary.api.dto.ReturnPackageRequestApi;

public class ReturnPackageRequest {
    private ShipmentId shipmentId;
    private String reason;
    private DepartmentCode departmentCode;
    private UserId userId;
    private ReasonCode reasonCode;

	public ReturnPackageRequest(final DepartmentCode departmentCode,
                                final String reason,
                                final ShipmentId shipmentId,
                                final UserId userId,
                                final ReasonCode reasonCode) {
        this.departmentCode = departmentCode;
        this.reason = reason;
        this.shipmentId = shipmentId;
        this.userId = userId;
        this.reasonCode = reasonCode;
    }

    public static ReturnPackageRequest from(final ReturnPackageRequestApi returnPackageRequest) {
        final DepartmentCode departmentCode = DepartmentCode.of(returnPackageRequest.departmentCode());
        final UserId userId = UserId.of(returnPackageRequest.userId());
        final ShipmentId shipmentId = ShipmentId.of(returnPackageRequest.shipmentId());
		return new ReturnPackageRequest(departmentCode, returnPackageRequest.reason(), shipmentId, userId,
				ReasonCode.valueOf(returnPackageRequest.reasonCode().value()));
    }

    public DepartmentCode getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(final DepartmentCode departmentCode) {
        this.departmentCode = departmentCode;
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
