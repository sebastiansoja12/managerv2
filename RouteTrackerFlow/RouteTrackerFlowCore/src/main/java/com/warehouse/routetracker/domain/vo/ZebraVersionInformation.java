package com.warehouse.routetracker.domain.vo;

import com.warehouse.routetracker.domain.model.ProcessType;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ZebraVersionInformation {
    String version;
    Long parcelId;
    ProcessType processType;
}
