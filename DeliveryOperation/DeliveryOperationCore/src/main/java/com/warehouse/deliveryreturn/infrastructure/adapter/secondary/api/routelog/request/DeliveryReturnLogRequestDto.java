package com.warehouse.deliveryreturn.infrastructure.adapter.secondary.api.routelog.request;

import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.api.routelog.ProcessType;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.UUID;

@Value
@Builder
@Jacksonized
public class DeliveryReturnLogRequestDto {
    UUID id;
    Long parcelId;
    String deliveryStatus;
    String updateStatus;
    String username;
    String depotCode;
    ProcessType processType;
}
