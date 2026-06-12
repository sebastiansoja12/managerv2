package com.warehouse.routetracker.infrastructure.adapter.primary.dto;


import java.time.LocalDateTime;

import com.warehouse.routetracker.domain.vo.TerminalId;


public class RouteLogRecordDetailDto {
    private Long id;
    private TerminalIdDto terminalId;
    private String version;
    private UserIdDto userId;
    private SupplierIdDto supplierId;
    private DepartmentIdDto departmentId;
    private ShipmentStatusDto shipmentStatus;
    private String description;
    private LocalDateTime timestamp;
    private ProcessTypeDto processType;
    private String request;

    public RouteLogRecordDetailDto() {
    }

    public RouteLogRecordDetailDto(final Long id, final TerminalIdDto terminalId, final String version,
                                   final UserIdDto userId, final SupplierIdDto supplierId,
                                   final DepartmentIdDto departmentId, final ShipmentStatusDto shipmentStatus,
                                   final String description, final LocalDateTime timestamp, final ProcessTypeDto processType,
                                   final String request) {
        this.id = id;
        this.terminalId = terminalId;
        this.version = version;
        this.userId = userId;
        this.supplierId = supplierId;
        this.departmentId = departmentId;
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

    public UserIdDto getUserId() {
        return userId;
    }

    public SupplierIdDto getSupplierId() {
        return supplierId;
    }

    public DepartmentIdDto getDepartmentId() {
        return departmentId;
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
        return terminalId != null ? new TerminalIdDto(terminalId.value()) : null;
    }
}
