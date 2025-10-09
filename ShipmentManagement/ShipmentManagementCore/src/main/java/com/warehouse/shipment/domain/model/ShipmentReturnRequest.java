package com.warehouse.shipment.domain.model;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.shipment.infrastructure.adapter.primary.api.ShipmentReturnRequestApi;

public class ShipmentReturnRequest {
    private ShipmentId shipmentId;
    private String reason;
    private DepartmentCode departmentCode;
    private UserId issuedBy;

	public ShipmentReturnRequest(final DepartmentCode departmentCode, final UserId issuedBy, final String reason,
			final ShipmentId shipmentId) {
        this.departmentCode = departmentCode;
        this.issuedBy = issuedBy;
        this.reason = reason;
        this.shipmentId = shipmentId;
    }

    public static ShipmentReturnRequest from(final ShipmentReturnRequestApi req) {
		return new ShipmentReturnRequest(req.departmentCode(), req.issuedBy(), req.reason(),
				new ShipmentId(req.shipmentId().getValue()));
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
}
