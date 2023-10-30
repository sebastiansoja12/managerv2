package com.warehouse.zebra.infrastructure.adapter.secondary.api;

import com.warehouse.zebra.infrastructure.api.dto.ParcelDto;
import com.warehouse.zebra.infrastructure.api.dto.ReturnStatusDto;
import com.warehouse.zebra.infrastructure.api.dto.SupplierCodeDto;
import lombok.Data;

@Data
public class ReturnPackageRequestDto {
    private ParcelDto parcel;
    private String reason;
    private ReturnStatusDto returnStatus;
    private String returnToken;
    private SupplierCodeDto supplierCode;
}
