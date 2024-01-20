package com.warehouse.softwareconfiguration.infrastructure.adapter.primary.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Jacksonized
@Builder
public class SoftwareDto {
    String name;
    String category;
    String value;
}
