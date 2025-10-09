package com.warehouse.returning.infrastructure.adapter.primary.api.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;


@Value
@Builder
@Jacksonized
public class ReturnModelDto {
    ShipmentIdDto shipmentId;
    ReasonDto reason;
    ReturnStatusDto returnStatus;
    ReturnTokenDto returnToken;
    SupplierCodeDto supplierCode;
    DepartmentCodeApi depotCode;
    UsernameApi username;
}
