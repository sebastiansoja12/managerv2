package com.warehouse.commonassets.request;

import com.warehouse.commonassets.model.Parcel;
import com.warehouse.commonassets.enumeration.ReturnStatus;


public class ReturnRequest {

	private Parcel parcel;

	private String reason;

	private ReturnStatus returnStatus;

	private String returnToken;

	private String supplierCode;

	public ReturnRequest(Parcel parcel, String reason, ReturnStatus returnStatus, String returnToken,
			String supplierCode) {
		this.parcel = parcel;
		this.reason = reason;
		this.returnStatus = returnStatus;
		this.returnToken = returnToken;
		this.supplierCode = supplierCode;
	}

	public Parcel getParcel() {
		return parcel;
	}

	public void setParcel(Parcel parcel) {
		this.parcel = parcel;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public ReturnStatus getReturnStatus() {
		return returnStatus;
	}

	public void setReturnStatus(ReturnStatus returnStatus) {
		this.returnStatus = returnStatus;
	}

	public String getReturnToken() {
		return returnToken;
	}

	public void setReturnToken(String returnToken) {
		this.returnToken = returnToken;
	}

	public String getSupplierCode() {
		return supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}

	public Long getParcelId() {
		return parcel != null ? parcel.getId() : null;
	}
	
}
