package com.warehouse.terminal.domain.model.request;


import com.warehouse.commonassets.enumeration.ReturnStatus;
import com.warehouse.terminal.domain.model.Shipment;

public class ReturnRequest {

	private Shipment shipment;

	private String reason;

	private ReturnStatus returnStatus;

	private String returnToken;

	private String supplierCode;

	public ReturnRequest(final Shipment shipment,
						 final String reason,
						 final ReturnStatus returnStatus,
						 final String returnToken,
						 final String supplierCode) {
		this.shipment = shipment;
		this.reason = reason;
		this.returnStatus = returnStatus;
		this.returnToken = returnToken;
		this.supplierCode = supplierCode;
	}

	public Shipment getShipment() {
		return shipment;
	}

	public void setShipment(final Shipment shipment) {
		this.shipment = shipment;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(final String reason) {
		this.reason = reason;
	}

	public ReturnStatus getReturnStatus() {
		return returnStatus;
	}

	public void setReturnStatus(final ReturnStatus returnStatus) {
		this.returnStatus = returnStatus;
	}

	public String getReturnToken() {
		return returnToken;
	}

	public void setReturnToken(final String returnToken) {
		this.returnToken = returnToken;
	}

	public String getSupplierCode() {
		return supplierCode;
	}

	public void setSupplierCode(final String supplierCode) {
		this.supplierCode = supplierCode;
	}
}
