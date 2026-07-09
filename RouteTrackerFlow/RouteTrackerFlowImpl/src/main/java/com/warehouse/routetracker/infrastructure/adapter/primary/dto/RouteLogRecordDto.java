package com.warehouse.routetracker.infrastructure.adapter.primary.dto;

public class RouteLogRecordDto {
    private ProcessIdDto processId;
    private ShipmentIdDto shipmentId;
    private RouteLogRecordDetailsDto routeLogRecordDetails;
    private ReturnCodeDto returnCode;
    private FaultDescriptionDto faultDescription;

    public RouteLogRecordDto() {
    }

	public RouteLogRecordDto(final ProcessIdDto processId,
                             final ShipmentIdDto shipmentId,
                             final RouteLogRecordDetailsDto routeLogRecordDetails,
                             final ReturnCodeDto returnCode,
                             final FaultDescriptionDto faultDescription) {
        this.processId = processId;
        this.shipmentId = shipmentId;
        this.routeLogRecordDetails = routeLogRecordDetails;
        this.returnCode = returnCode;
        this.faultDescription = faultDescription;
    }

    public ProcessIdDto getProcessId() {
        return processId;
    }

    public ShipmentIdDto getShipmentId() {
        return shipmentId;
    }

    public RouteLogRecordDetailsDto getRouteLogRecordDetails() {
        return routeLogRecordDetails;
    }

    public ReturnCodeDto getReturnCode() {
        return returnCode;
    }

    public FaultDescriptionDto getFaultDescription() {
        return faultDescription;
    }
}
