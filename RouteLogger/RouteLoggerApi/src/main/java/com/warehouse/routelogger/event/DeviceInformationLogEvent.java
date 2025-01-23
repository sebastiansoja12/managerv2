package com.warehouse.routelogger.event;

import com.warehouse.routelogger.RouteLogEvent;
import com.warehouse.routelogger.dto.DeviceDto;
import com.warehouse.routelogger.dto.ProcessTypeDto;
import com.warehouse.routelogger.dto.ShipmentIdDto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class DeviceInformationLogEvent extends RouteLogBaseEvent implements RouteLogEvent {

    private final DeviceDto device;

    private final ShipmentIdDto shipmentId;

    private final ProcessTypeDto processType;

    @Builder
    public DeviceInformationLogEvent(final DeviceDto device, final ShipmentIdDto shipmentId, final ProcessTypeDto processType) {
        super();
        this.device = device;
        this.shipmentId = shipmentId;
        this.processType = processType;
    }

    public DeviceDto getDevice() {
        return device;
    }

    public ShipmentIdDto getShipmentId() {
        return shipmentId;
    }

    public ProcessTypeDto getProcessType() {
        return processType;
    }
}
