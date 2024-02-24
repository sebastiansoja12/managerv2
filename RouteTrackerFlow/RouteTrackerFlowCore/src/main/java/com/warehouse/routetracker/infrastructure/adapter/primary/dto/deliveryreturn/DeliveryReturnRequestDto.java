package com.warehouse.routetracker.infrastructure.adapter.primary.dto.deliveryreturn;


import com.warehouse.routetracker.infrastructure.adapter.primary.dto.ProcessTypeDto;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.UUID;

@Value
@Jacksonized
@Builder
public class DeliveryReturnRequestDto {
    UUID id;
    Long parcelId;
    String deliveryStatus;
    String returnToken;
    String updateStatus;
    String username;
    String depotCode;
    ProcessTypeDto processType;
}
