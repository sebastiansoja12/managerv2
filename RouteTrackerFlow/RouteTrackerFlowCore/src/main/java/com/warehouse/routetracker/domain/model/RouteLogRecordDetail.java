package com.warehouse.routetracker.domain.model;

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
    private Long zebraId;
    private String version;
    private String username;
    private String supplierCode;
    private String depotCode;
    private ParcelStatus parcelStatus;
    private String description;
    private LocalDateTime timestamp;
    private ProcessType processType;
    private String request;

    public void saveZebraIdInformation(Long zebraId) {
        this.zebraId = zebraId;
    }

    public void saveZebraVersionInformation(String version) {
        this.version = version;
    }

    public void updateRequest(String request) {
        this.request = request;
        this.timestamp = LocalDateTime.now();
    }

    public void saveSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public void saveDescription(String description) {
        this.description = description;
    }

    public void saveUsername(String username) {
        this.username = username;
    }

    public void saveDepotCode(String depotCode) {
        this.depotCode = depotCode;
    }

    public void saveParcelStatus(ParcelStatus parcelStatus) {
        this.parcelStatus = parcelStatus;
    }
}
