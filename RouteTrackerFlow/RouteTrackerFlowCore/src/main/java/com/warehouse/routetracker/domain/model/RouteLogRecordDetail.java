package com.warehouse.routetracker.domain.model;

import com.warehouse.routetracker.domain.enumeration.ParcelStatus;
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

    public void updateRequest(String request, LocalDateTime timestamp) {
        this.request = request;
        this.timestamp = timestamp;
    }


}
