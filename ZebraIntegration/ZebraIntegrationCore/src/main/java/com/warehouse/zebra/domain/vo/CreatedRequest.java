package com.warehouse.zebra.domain.vo;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CreatedRequest {
    Long parcelId;
}
