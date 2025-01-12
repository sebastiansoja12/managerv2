package com.warehouse.deliveryreturn.domain.vo;

import com.warehouse.deliveryreturn.domain.enumeration.ProcessType;
import lombok.Value;

@Value
public class SupplierCodeRequest {
    String supplierCode;
    Long parcelId;
    ProcessType processType;
}
