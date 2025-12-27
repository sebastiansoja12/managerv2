package com.warehouse.shipment.domain.model;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.shipment.domain.enumeration.ReasonCode;
import com.warehouse.shipment.domain.enumeration.ReturnStatus;
import com.warehouse.shipment.infrastructure.adapter.primary.api.ShipmentReturnRequestApi;

public class ShipmentReturnCommand {
    private ShipmentId shipmentId;
    private String reason;
    private DepartmentCode departmentCode;
    private UserId issuedBy;
    private ReturnStatus returnStatus;
    private ReasonCode reasonCode;

    public ShipmentReturnCommand(final DepartmentCode departmentCode, final String reason,
                                 final ShipmentId shipmentId, final ReturnStatus returnStatus,
                                 final ReasonCode reasonCode) {
        this.departmentCode = departmentCode;
        this.reason = reason;
        this.shipmentId = shipmentId;
        this.returnStatus = returnStatus;
        this.reasonCode = reasonCode;
    }

    public static ShipmentReturnCommand from(final ShipmentReturnRequestApi req) {
		return new ShipmentReturnCommand(req.departmentCode(), req.reason(),
				new ShipmentId(req.shipmentId().getValue()), ReturnStatus.valueOf(req.returnStatus()),
                ReasonCode.valueOf(req.reasonCode().value()));
    }

    public DepartmentCode getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(final DepartmentCode departmentCode) {
        this.departmentCode = departmentCode;
    }

    public UserId getIssuedBy() {
        return issuedBy;
    }

    public void setIssuedBy(final UserId issuedBy) {
        this.issuedBy = issuedBy;
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

    public ReturnStatus getReturnStatus() {
        return returnStatus;
    }

    public void setReturnStatus(final ReturnStatus returnStatus) {
        this.returnStatus = returnStatus;
    }

    public ReasonCode getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(final ReasonCode reasonCode) {
        this.reasonCode = reasonCode;
    }
}
