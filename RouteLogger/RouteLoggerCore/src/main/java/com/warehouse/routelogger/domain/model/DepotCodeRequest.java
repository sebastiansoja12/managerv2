package com.warehouse.routelogger.domain.model;

import com.warehouse.commonassets.enumeration.ProcessType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DepotCodeRequest {
    private Long parcelId;
    private String depotCode;
    private ProcessType processType;
}
