package com.warehouse.routetracker.infrastructure.adapter.primary.dto;

import com.warehouse.commonassets.identificator.TerminalId;

import java.time.LocalDateTime;


public class RouteLogRecordDetailDto {
    private Long id;
    private TerminalIdDto terminalId;
    private String version;
    private String username;
    private String supplierCode;
    private String depotCode;
    private ShipmentStatusDto shipmentStatus;
    private String description;
    private LocalDateTime timestamp;
    private ProcessTypeDto processType;
    private String request;

    public RouteLogRecordDetailDto() {
    }

	public RouteLogRecordDetailDto(final Long id, final TerminalIdDto terminalId, final String version, final String username,
                                   final String supplierCode, final String depotCode, final ShipmentStatusDto shipmentStatus,
                                   final String description, final LocalDateTime timestamp, final ProcessTypeDto processType,
                                   final String request) {
        this.id = id;
        this.terminalId = terminalId;
        this.version = version;
        this.username = username;
        this.supplierCode = supplierCode;
        this.depotCode = depotCode;
        this.shipmentStatus = shipmentStatus;
        this.description = description;
        this.timestamp = timestamp;
        this.processType = processType;
        this.request = request;
    }

    public Long getId() {
        return id;
    }

    public TerminalIdDto getTerminalId() {
        return terminalId;
    }

    public String getVersion() {
        return version;
    }

    public String getUsername() {
        return username;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public String getDepotCode() {
        return depotCode;
    }

    public ShipmentStatusDto getShipmentStatus() {
        return shipmentStatus;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public ProcessTypeDto getProcessType() {
        return processType;
    }

    public String getRequest() {
        return request;
    }

    public static TerminalIdDto from(final TerminalId terminalId) {
        return new TerminalIdDto(terminalId.getValue());
    }
}
