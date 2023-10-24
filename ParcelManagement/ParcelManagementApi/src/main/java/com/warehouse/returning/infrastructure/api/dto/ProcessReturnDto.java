package com.warehouse.returning.infrastructure.api.dto;

import lombok.Value;

@Value
public class ProcessReturnDto {
     Long returnId;
     String processStatus;
}
