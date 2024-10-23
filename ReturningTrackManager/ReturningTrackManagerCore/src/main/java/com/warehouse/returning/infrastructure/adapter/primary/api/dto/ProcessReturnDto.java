package com.warehouse.returning.infrastructure.adapter.primary.api.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class ProcessReturnDto {
     Long shipmentId;
     Long returnId;
     String processStatus;
}
