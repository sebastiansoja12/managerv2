package com.warehouse.zebrareturn.infrastructure.adapter.secondary.api;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class ReturnPackageRequestDto {
    ParcelDto parcel;
    String reason;
    ReturnStatusDto returnStatus;
    String returnToken;
    SupplierCodeDto supplierCode;
}
