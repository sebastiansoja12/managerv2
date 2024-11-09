package com.warehouse.routetracker.infrastructure.adapter.primary.dto.deliveryreturn;


import java.util.UUID;

import com.warehouse.routetracker.infrastructure.adapter.primary.dto.ProcessTypeDto;
import com.warehouse.routetracker.infrastructure.adapter.primary.dto.ShipmentIdDto;


public class DeliveryReturnRequestDto {
    private UUID id;
    private ShipmentIdDto shipmentId;
    private String deliveryStatus;
    private String returnToken;
    private String updateStatus;
    private String username;
    private String depotCode;
    private ProcessTypeDto processType;

    public DeliveryReturnRequestDto() {
    }

    public DeliveryReturnRequestDto(final UUID id,
                                    final ShipmentIdDto shipmentId,
                                    final String deliveryStatus,
                                    final String returnToken,
                                    final String updateStatus,
                                    final String username,
                                    final String depotCode,
                                    final ProcessTypeDto processType) {
        this.id = id;
        this.shipmentId = shipmentId;
        this.deliveryStatus = deliveryStatus;
        this.returnToken = returnToken;
        this.updateStatus = updateStatus;
        this.username = username;
        this.depotCode = depotCode;
        this.processType = processType;
    }

    public UUID getId() {
        return id;
    }

    public ShipmentIdDto getShipmentId() {
        return shipmentId;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public String getReturnToken() {
        return returnToken;
    }

    public String getUpdateStatus() {
        return updateStatus;
    }

    public String getUsername() {
        return username;
    }

    public String getDepotCode() {
        return depotCode;
    }

    public ProcessTypeDto getProcessType() {
        return processType;
    }
}
