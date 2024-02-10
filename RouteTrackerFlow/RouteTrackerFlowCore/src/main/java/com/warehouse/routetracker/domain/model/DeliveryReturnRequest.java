package com.warehouse.routetracker.domain.model;

import com.warehouse.routetracker.domain.enumeration.ProcessType;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeliveryReturnRequest {
    private UUID id;
    private Long parcelId;
    private String deliveryStatus;
    private String returnToken;
    private String updateStatus;
    private String depotCode;
    private String username;
    private ProcessType processType;
}
