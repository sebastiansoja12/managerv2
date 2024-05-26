package com.warehouse.routelogger.domain.model;

import com.warehouse.commonassets.ProcessType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsernameLogRequest {
    private Long parcelId;
    private String username;
    private ProcessType processType;
}
