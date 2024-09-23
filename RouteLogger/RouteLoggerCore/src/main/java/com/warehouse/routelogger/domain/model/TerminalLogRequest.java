package com.warehouse.routelogger.domain.model;

import com.warehouse.commonassets.enumeration.ProcessType;
import lombok.Data;

@Data
public class TerminalLogRequest {
    private Long parcelId;
    private ProcessType processType;
    private String terminalId;
}
