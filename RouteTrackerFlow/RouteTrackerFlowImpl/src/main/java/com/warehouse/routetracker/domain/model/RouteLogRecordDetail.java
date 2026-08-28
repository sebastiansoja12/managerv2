package com.warehouse.routetracker.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.routetracker.domain.enumeration.ShipmentStatus;
import com.warehouse.routetracker.domain.enumeration.ProcessType;
import com.warehouse.routetracker.domain.vo.TerminalId;

import lombok.*;

@Builder
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class RouteLogRecordDetail {
    private Long id;
    private UUID eventId;
    private TerminalId terminalId;
    private String version;
    private UserId userId;
    private SupplierId supplierId;
    private DepartmentId departmentId;
    private ShipmentStatus shipmentStatus;
    private String description;
    private LocalDateTime timestamp;
    private ProcessType processType;
    private String request;

    public void saveTerminalId(final TerminalId terminalId) {
        this.terminalId = terminalId;
        markAsModified();
    }

    public void saveZebraVersionInformation(final String version) {
        this.version = version;
        markAsModified();
    }

    public void updateRequest(final String request) {
        this.request = request;
        markAsModified();
    }

    public void saveSupplierId(final SupplierId supplierId) {
        this.supplierId = supplierId;
        markAsModified();
    }

    public void saveDescription(final String description) {
        this.description = description;
        markAsModified();
    }

    public void saveUserId(final UserId userId) {
        this.userId = userId;
        markAsModified();
    }

    public void saveDepartmentId(final DepartmentId departmentId) {
        this.departmentId = departmentId;
        markAsModified();
    }

    public void saveShipmentStatus(final ShipmentStatus shipmentStatus) {
        this.shipmentStatus = shipmentStatus;
        markAsModified();
    }

    private void markAsModified() {
        this.timestamp = LocalDateTime.now();
    }

    public void updateDeviceInformation(final DeviceInformationRequest request) {
        this.departmentId = request.getDepartmentId();
        this.userId = request.getUserId();
        this.terminalId = new TerminalId(request.getDeviceId().value());
        this.version = request.getVersion();
    }
}
