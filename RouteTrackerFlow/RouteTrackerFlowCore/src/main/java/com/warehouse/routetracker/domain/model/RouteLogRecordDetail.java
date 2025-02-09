package com.warehouse.routetracker.domain.model;

import com.warehouse.commonassets.identificator.TerminalId;
import com.warehouse.routetracker.domain.enumeration.ParcelStatus;
import com.warehouse.routetracker.domain.enumeration.ProcessType;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class RouteLogRecordDetail {
    private Long id;
    private TerminalId terminalId;
    private String version;
    private String username;
    private String supplierCode;
    private String departmentCode;
    private ParcelStatus parcelStatus;
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

    public void saveSupplierCode(final String supplierCode) {
        this.supplierCode = supplierCode;
        markAsModified();
    }

    public void saveDescription(final String description) {
        this.description = description;
        markAsModified();
    }

    public void saveUsername(final String username) {
        this.username = username;
        markAsModified();
    }

    public void saveDepartmentCode(final String departmentCode) {
        this.departmentCode = departmentCode;
        markAsModified();
    }

    public void saveParcelStatus(final ParcelStatus parcelStatus) {
        this.parcelStatus = parcelStatus;
        markAsModified();
    }

    private void markAsModified() {
        this.timestamp = LocalDateTime.now();
    }

    public void updateDeviceInformation(final DeviceInformationRequest request) {
        this.departmentCode = request.getDepartmentCode().getValue();
        this.username = request.getUsername().value();
        this.terminalId = new TerminalId(request.getDeviceId().getValue());
        this.version = request.getVersion();
    }
}
