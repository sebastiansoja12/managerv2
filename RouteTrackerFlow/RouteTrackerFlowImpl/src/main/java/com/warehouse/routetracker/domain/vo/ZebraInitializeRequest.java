package com.warehouse.routetracker.domain.vo;

import com.warehouse.routetracker.domain.enumeration.ProcessType;
import lombok.Value;

@Value
public class ZebraInitializeRequest {
    Long parcelId;
    ProcessType processType;
}
