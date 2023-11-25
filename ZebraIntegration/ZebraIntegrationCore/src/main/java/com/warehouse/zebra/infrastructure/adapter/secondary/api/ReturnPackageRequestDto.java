package com.warehouse.zebra.infrastructure.adapter.secondary.api;

import lombok.Data;

@Data
public class ReturnPackageRequestDto {
    private ParcelDto parcel;
    private String reason;
    private ReturnStatusDto returnStatus;
    private String returnToken;
    private SupplierCodeDto supplierCode;
}
