package com.warehouse.reroute.infrastructure.api.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class ParcelId {
    @NonNull
    Long value;
}
