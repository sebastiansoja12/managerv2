package com.warehouse.routelogger.domain.model;

import java.util.List;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.commonassets.identificator.ShipmentId;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Request {
    private String request;
    private List<ShipmentId> shipmentIds;
    private ProcessType processType;
}
