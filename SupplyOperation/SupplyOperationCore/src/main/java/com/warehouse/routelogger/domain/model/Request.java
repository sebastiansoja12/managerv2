package com.warehouse.routelogger.domain.model;

import com.warehouse.commonassets.ProcessType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Request {
    private String request;
    private Long parcelId;
    private ProcessType processType;
}
