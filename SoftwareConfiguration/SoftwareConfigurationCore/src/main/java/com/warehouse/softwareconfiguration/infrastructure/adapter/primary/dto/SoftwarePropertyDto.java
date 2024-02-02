package com.warehouse.softwareconfiguration.infrastructure.adapter.primary.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Jacksonized
@Builder
public class SoftwarePropertyDto {
    String name;
    String category;
    String value;
}
