package com.warehouse.routelogger.domain.model;

import com.warehouse.commonassets.enumeration.ProcessType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SupplierCodeRequest {
    private String supplierCode;
    private ProcessType processType;
    private Long parcelId;
}
